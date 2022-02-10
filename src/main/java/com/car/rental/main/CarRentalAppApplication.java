package com.car.rental.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages={"com.car"})
@Configuration
@EnableJpaRepositories(basePackages={"com.car.rental.repository"})
@EntityScan(basePackages={"com.car.rental.entity"})
public class CarRentalAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalAppApplication.class, args);
	}

}
