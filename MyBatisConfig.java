package com.cug.daijiaguanli.config;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
//@MapperScan("com.cug.daijiaguanli.mapper")
public class MyBatisConfig {

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
//        fb.setDataSource(dataSource);
//        // fb.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
//        // fb.setMapperLocations(new PathMatchingResourcePatternResolver()
//        //         .getResources("classpath*:mapper/*.xml"));
//        return fb.getObject();
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
}
