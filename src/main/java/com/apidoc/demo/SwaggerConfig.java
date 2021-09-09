package com.apidoc.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

//	@Bean
//	public Docket docket(ApiInfo apiInfo) {
//		return new Docket(DocumentationType.SWAGGER_2).groupName("user-api").useDefaultResponseMessages(false)
//				.apiInfo(apiInfo).select().apis(RequestHandlerSelectors.basePackage("com.swagger2doc"))
//				.paths(PathSelectors.any()).build();
//	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Demo API Documentation").description("Demo API Documentation")
				.version("1.0.0").build();
	}
}
