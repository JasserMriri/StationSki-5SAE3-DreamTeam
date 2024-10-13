package tn.esprit.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"tn.esprit.spring.entities", "tn.esprit.spring.repositories", "tn.esprit.spring.services", "tn.esprit.spring.controllers"})

@EnableScheduling
public class GestionStationSkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionStationSkiApplication.class, args);
	}

}
