package com.dity.common.dbconfig;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


@Configuration
public class DataSourceConfig{
	@Bean(name="dataSource")
	@ConfigurationProperties(prefix="dity.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Value("${mybatis.config-location}")
    private String myBatisConfig;
	
	@Value("${mybatis.mapper-locations}")
    private String mapperLocations;
	
	@Bean("sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource) throws Exception{
		SqlSessionFactoryBean bean  =  new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();	
		bean.setConfigLocation(resolver.getResource(myBatisConfig));
		bean.setMapperLocations(resolver.getResources(mapperLocations));
		return bean.getObject();
		
	}
	@Bean("sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory")SqlSessionFactory sqlSessionFactory){
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	@Bean("transactionManager")
	 public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);    
	 }
}
