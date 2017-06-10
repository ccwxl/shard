package org.gpstable.shared.converter;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * @author xielongwang
 * @create 2017-03-22 上午9:30
 * @email xielong.wang@nvr-china.com
 */
@Deprecated
public class SqlConverterFactory {
    private static final Log log = LogFactory.getLog(SqlConverterFactory.class);
    private static SqlConverterFactory factory = new SqlConverterFactory();
    private Map<String, SqlConverter> converterMap = new HashMap<String, SqlConverter>();
    private CCJSqlParserManager pm = new CCJSqlParserManager();

    public static SqlConverterFactory getInstance() {
        return factory;
    }

    private SqlConverterFactory() {
        this.register();
    }

    private void register() {
        this.converterMap.put(Select.class.getName(), new SelectSqlConverter());
        this.converterMap.put(Insert.class.getName(), new InsertSqlConverter());
        this.converterMap.put(Update.class.getName(), new UpdateSqlConverter());
        this.converterMap.put(Delete.class.getName(), new DeleteSqlConverter());
    }

    public String  convert(String sql,String baseTableName,String convertTableName){
        Statement statement = null;

        try {
            statement = this.pm.parse(new StringReader(sql));
        } catch (JSQLParserException var6) {
            log.error(var6.getMessage(), var6);
        }

        SqlConverter converter = this.converterMap.get(statement.getClass().getName());
        return converter != null?converter.convert(statement,baseTableName,convertTableName):sql;
    }
}
