package com.team21;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:messages.properties")
public class UserMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMsApplication.class, args);
	}

}
