package cn.patterncat.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.patterncat")
public class JobAdminUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobAdminUiApplication.class, args);
	}
}
