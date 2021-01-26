package com.apidoc.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
	
	/*
	 * Api Documentation available:
	 * Api docs JSON: localhost:8080/v2/api-docs
	 * Api Documentation page (with Swagger UI): localhost:8080/swagger-ui/
	 *
	 */

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}
	
	 @Bean
	 public ApiInfo apiInfo() {
		 return new ApiInfoBuilder().title("Demo API Documentation").description("Demo API Documentation").version("1.0.0").build();
	 }
	  
}
