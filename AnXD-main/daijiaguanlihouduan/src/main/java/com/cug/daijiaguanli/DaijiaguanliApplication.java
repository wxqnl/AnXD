package com.cug.daijiaguanli;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@MapperScan("com.cug.daijiaguanli.mapper")
public class DaijiaguanliApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaijiaguanliApplication.class, args);
	}

}
