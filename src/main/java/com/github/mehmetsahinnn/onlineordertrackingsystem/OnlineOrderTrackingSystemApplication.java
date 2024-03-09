package com.github.mehmetsahinnn.onlineordertrackingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OnlineOrderTrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineOrderTrackingSystemApplication.class, args);
	}

}
