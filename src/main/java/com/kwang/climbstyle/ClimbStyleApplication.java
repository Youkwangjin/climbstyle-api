package com.kwang.climbstyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClimbStyleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClimbStyleApplication.class, args);
	}

}
