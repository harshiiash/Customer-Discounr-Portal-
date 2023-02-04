package com.customer.discount.demo;

import com.customer.discount.demo.api.CustomerDBRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackageClasses ={ CustomerDBRepository.class})
@ComponentScan({"com.customer.discount.demo"})
@OpenAPIDefinition(info = @Info(title = "Swagger H&G  BE",
	description = "Documentation H&G BE"))
public class HGApplication {

	public static void main(String[] args) {
		SpringApplication.run(HGApplication.class, args);
//		new SpringApplicationBuilder(HGApplication.class)
//			.web(WebApplicationType.REACTIVE)
//			.run(args);
	}

}
