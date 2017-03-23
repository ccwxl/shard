package org.gpstable.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.plugin.Interceptor;
import org.gpstable.shared.plugin.StrategyInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:db.properties" })
// 接口所在的包,相当于配置MapperScannerConfigurer
@MapperScan(basePackages = { "org.gpstable.mapper" })
public class DataBaseConfig {

	private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
	private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
	private static final String PROPERTY_NAME_DATABASE_PORT = "db.port";
	private static final String PROPERTY_NAME_DATABASE_NAME = "db.databasename";
	private static final String PROPERTY_NAME_DATABASE_SERVER_NAME = "db.servername";
	private static final String PROPERTY_MAX_POOL_SIZE = "db.maxpoolsize";
	private static final String PROPERTY_CONNECTIONTIMEOUT = "db.connectiontimeout";
	private static final String PROPERTY_AUTOCOMMIT = "db.autocommit";
	private static final String PROPERTY_IDLETIMEOUT = "db.idletimeout";
	private static final String PROPERTY_MAXLIFETIME = "db.maxlifetime";
	private static final String PROPERTY_LEAKDETECTIONTHRESHOLDDB = "db.leakdetectionthreshold";
	private static final String PROPERTY_REGISTERMBEANS = "db.registermbeans";

	@Resource
	private Environment environment;

	/**
	 * 数据源
	 *
	 * @return
	 */
	@Bean
	public HikariDataSource dataSource() {
		Properties datasourceProperties = new Properties();
		datasourceProperties.setProperty("dataSourceClassName",
				environment.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
		datasourceProperties.setProperty("dataSource.user",
				environment.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
		datasourceProperties.setProperty("dataSource.password",
				environment.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
		datasourceProperties.setProperty("dataSource.portNumber",
				environment.getRequiredProperty(PROPERTY_NAME_DATABASE_PORT));
		datasourceProperties.setProperty("dataSource.databaseName",
				environment.getRequiredProperty(PROPERTY_NAME_DATABASE_NAME));
		datasourceProperties.setProperty("dataSource.serverName",
				environment.getRequiredProperty(PROPERTY_NAME_DATABASE_SERVER_NAME));
		HikariDataSource dataSource = new HikariDataSource(new HikariConfig(datasourceProperties));
		dataSource.setConnectionTimeout(environment.getRequiredProperty(PROPERTY_CONNECTIONTIMEOUT, Long.TYPE));
		dataSource.setAutoCommit(environment.getRequiredProperty(PROPERTY_AUTOCOMMIT, Boolean.TYPE));
		dataSource.setIdleTimeout(environment.getRequiredProperty(PROPERTY_IDLETIMEOUT, Long.TYPE));
		dataSource.setMaxLifetime(environment.getRequiredProperty(PROPERTY_MAXLIFETIME, Long.TYPE));
		dataSource.setLeakDetectionThreshold(
				environment.getRequiredProperty(PROPERTY_LEAKDETECTIONTHRESHOLDDB, Long.TYPE));
		dataSource.setMaximumPoolSize(environment.getRequiredProperty(PROPERTY_MAX_POOL_SIZE, Integer.TYPE));
		dataSource.setPoolName("postgresql");
		dataSource.setRegisterMbeans(environment.getRequiredProperty(PROPERTY_REGISTERMBEANS, Boolean.TYPE));
		return dataSource;
	}

	@Bean(name = "sqlSessionFactoryBean")
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());

		sqlSessionFactoryBean.setTypeAliasesPackage("org.gpstable.domain,org.gpstable.utils");
		
		sqlSessionFactoryBean.setTypeHandlersPackage("org.gpstable.utils");

		// mapper文件location
		org.springframework.core.io.Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:org/gpstbale/sqlmap/**/*Mapper.xml");
		sqlSessionFactoryBean.setMapperLocations(resources);
		// mybatis的一些配置 相当于SqlMapConfig.xml
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(configuration);

		// 插件
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("dialect", "postgresql");
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "check");
		properties.setProperty("pageSizeZero", "count=countSql");
		pageHelper.setProperties(properties);

		StrategyInterceptor hashStrategyInterceptor = new StrategyInterceptor();
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { hashStrategyInterceptor, pageHelper });

		return sqlSessionFactoryBean;
	}

	/**
	 * jdbctemplate注入
	 *
	 * @return
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource(), true);
	}
}
