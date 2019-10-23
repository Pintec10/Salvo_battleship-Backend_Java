package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(GameRepository repository) {
		return (args) -> {
			repository.save(new Game(new Date()));
			repository.save(new Game(
					Date.from(new Date().toInstant().plusSeconds(3600))
			) );
			Date d3 = new Date();
			d3 = Date.from(d3.toInstant().plusSeconds(7200));
			repository.save(new Game(d3) );
		};
	}
}

