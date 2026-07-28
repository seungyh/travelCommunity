package com.travel.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TravelCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelCommunityApplication.class, args);
	}

}
