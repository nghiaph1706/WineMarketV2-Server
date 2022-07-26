package com.winemarketv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Winemarketv2Application {

	public static void main(String[] args) {
		SpringApplication.run(Winemarketv2Application.class, args);
	}

}
