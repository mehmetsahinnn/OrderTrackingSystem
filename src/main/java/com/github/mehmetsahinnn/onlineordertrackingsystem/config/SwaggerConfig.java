package com.github.mehmetsahinnn.onlineordertrackingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for Swagger documentation.
 * This class configures Swagger to document API endpoints for the online order tracking system.
 */

@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {


    /**
     * Configures the Docket bean for Swagger API documentation.
     *
     * @return the configured Docket bean
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.github.mehmetsahinnn.onlineordertrackingsystem.controllers"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfoMetaData());
    }

    /**
     * Constructs metadata for API documentation.
     *
     * @return the API information
     */
    private ApiInfo apiInfoMetaData() {

        return new ApiInfoBuilder().title("com.github.mehmetsahinnn.onlineordertrackingsystem")
                .description("API Endpoint Decoration")
                .contact(new Contact("Mehmet Sahin", "https://www.msahin.com.tr/", "mehmetsahinn2002@gmail.com"))
                .version("1.0.0")
                .build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addResourceHandlers( ResourceHandlerRegistry registry )
            {
                registry.addResourceHandler( "swagger-ui.html" );
                registry.addResourceHandler( "/webjars/**" );
            }
        };
    }

}