package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"cn.patterncat","com.example.demo"})
@ComponentScan(basePackages = {"cn.patterncat","com.example.demo"})
public class JobMongoStoreDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobMongoStoreDemoApplication.class, args);
	}
}
