package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"cn.patterncat","com.example.demo"})
@ComponentScan(basePackages = {"cn.patterncat","com.example.demo"})
public class JobEsStoreDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobEsStoreDemoApplication.class, args);
	}
}
