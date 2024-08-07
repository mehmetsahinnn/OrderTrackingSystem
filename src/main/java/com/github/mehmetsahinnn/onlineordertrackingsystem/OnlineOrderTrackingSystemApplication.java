package com.github.mehmetsahinnn.onlineordertrackingsystem;

import com.github.mehmetsahinnn.onlineordertrackingsystem.producers.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaAuditing
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Configuration
@EnableElasticsearchRepositories
public class OnlineOrderTrackingSystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(OnlineOrderTrackingSystemApplication.class, args);
	}


}
