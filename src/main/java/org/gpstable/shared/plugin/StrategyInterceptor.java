package org.gpstable.shared.plugin;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.gpstable.config.ContextHelper;
import org.gpstable.config.DataBaseConfig;
import org.gpstable.shared.Strategy;
import org.gpstable.shared.StrategyManager;
import org.gpstable.shared.TableSplit;
import org.gpstable.shared.converter.SqlConverterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;

/**
 * @author xielong.wang
 *         <p>
 *         分表拦截器
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class StrategyInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseConfig.class);

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";
    private static final String DELEGATE_BOUNDSQL = "delegate.boundSql";
    private static final String DELEGATE_BOUNDSQL_PARAMETEROBJECT = "delegate.boundSql.parameterObject";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue(DELEGATE_MAPPED_STATEMENT);

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        logger.info("sql具体操作==[{}]", sqlCommandType.name());

        //获取具体mapper接口的权限定名进行反射是否需要分表
        String mapperReference = mappedStatement.getId();
        String mapperClassName = mapperReference.substring(0, mapperReference.lastIndexOf("."));
        Class<?> classObj = Class.forName(mapperClassName);
        //是否有分表注解
        TableSplit tableSplit = classObj.getAnnotation(TableSplit.class);
        if (tableSplit != null && tableSplit.split()) {
            //分表
            doSplitTable(metaStatementHandler, mappedStatement, tableSplit);
        }
        return invocation.proceed();
    }

    /**
     * 分表处理
     *
     * @param metaStatementHandler
     */
    private void doSplitTable(MetaObject metaStatementHandler, MappedStatement mappedStatement, TableSplit tableSplit) {
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue(DELEGATE_BOUNDSQL);
        String originalSql = boundSql.getSql();//原sql

        Object value = getTableColumnValue(mappedStatement, boundSql,tableSplit);//分表键的值
        logger.info("分表列==[{}]", value);

        if (originalSql != null && !originalSql.equals("")) {
            logger.info("分表前sql==[{}]", originalSql);
            //sql参数
            Object parameterObject = metaStatementHandler.getValue(DELEGATE_BOUNDSQL_PARAMETEROBJECT);
            logger.info("传入的对象==[{}]", parameterObject);
            //获取分表策略来处理分表
            StrategyManager strategyManager = ContextHelper.getStrategyManager();
            Strategy strategy = strategyManager.getStrategy(tableSplit.strategy());
            //原表名
            String baseTableName = tableSplit.value();
            //分表后的表名
            String convertTableName = strategy.convert2(tableSplit.value(), value);
            //根据原 sql 和生成的表名生成一个 sql
            //SqlConverterFactory sqlConverterFactory =SqlConverterFactory.getInstance();
            //String convertedSql =sqlConverterFactory.convert(originalSql,baseTableName,convertTableName);//sql
            Map<String, String> mapping = Collections.singletonMap(baseTableName, convertTableName);
            //使用druid 转换表名
            String convertedSql=SQLUtils.refactor(originalSql,JdbcConstants.POSTGRESQL,mapping);
            //替换sql
            metaStatementHandler.setValue("delegate.boundSql.sql", convertedSql);
            logger.info("分表后sql==[{}]", convertedSql);
        }

    }

    /**
     * 获取分表key的值
     *
     * @param mappedStatement
     * @param boundSql
     * @return
     */
    private Object getTableColumnValue(MappedStatement mappedStatement, BoundSql boundSql, TableSplit tableSplit) {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String propertyName = parameterMapping.getProperty();
                if (propertyName.equals(tableSplit.ShardColumn())) {
                    if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                        return boundSql.getAdditionalParameter(propertyName);
                    } else if (boundSql.getParameterObject() == null) {
                        return null;
                    } else if (mappedStatement.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(boundSql.getParameterObject().getClass())) {
                        return boundSql.getParameterObject();
                    } else {
                        MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(boundSql.getParameterObject());
                        return metaObject.getValue(propertyName);
                    }
                }

            }
        }
        return null;
    }


    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
