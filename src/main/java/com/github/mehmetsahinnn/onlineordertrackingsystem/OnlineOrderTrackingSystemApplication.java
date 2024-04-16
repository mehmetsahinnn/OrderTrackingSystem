package com.github.mehmetsahinnn.onlineordertrackingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaAuditing
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableSwagger2 //<-- add these annotation
@EnableWebMvc
public class OnlineOrderTrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineOrderTrackingSystemApplication.class, args);
	}

}
