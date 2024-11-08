package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = " Spring Boot Blog app Rest API",
				description = "Spring boot Blog app Rest API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Dhananjay",
						email = "dhananjayjoshi1999@gmail.com"
				)
//				license = @License(
//						name = "",
//						url="")
		)
//		externalDocs = @ExternalDocumentation(
//				description="",
//				url="")

)
public class SpringBootBlogRestApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		return  new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBlogRestApiApplication.class, args);
	}

}
