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
/*
	@Bean
	public CommandLineRunner initPlayerData(PlayerRepository repository) {
		return (args) -> {
			repository.save(new Player("j.bauer@ctu.gov"));
			repository.save(new Player("c.obrian@ctu.gov"));
			repository.save(new Player("kim_bauer@gmail.com"));
			repository.save(new Player("t.almeida@ctu.gov"));

			Player pla1 = new Player("j.bauer@ctu.gov");
			Player pla2 = new Player("c.obrian@ctu.gov");
			repository.save(pla1);
			repository.save(pla2);
			pla1.addParticipationPerPlayer(part1);
			pla2.addParticipationPerPlayer(part1);
		};
	}

	@Bean
	public CommandLineRunner initGameData(GameRepository repository) {
		return (args) -> {
			Date d1 = new Date();
			repository.save(new Game(d1));
			Date d2 = Date.from(d1.toInstant().plusSeconds(3600));
			repository.save(new Game(d2));
			Date d3 = Date.from(d2.toInstant().plusSeconds(3600));
			repository.save(new Game(d3));
			Date d4 = Date.from(d3.toInstant().plusSeconds(3600));
			repository.save(new Game(d4));
			Date d5 = Date.from(d4.toInstant().plusSeconds(3600));
			repository.save(new Game(d5));
			Date d6 = Date.from(d5.toInstant().plusSeconds(3600));
			repository.save(new Game(d6));
			Date d1 = new Date();
			//Date d2 = Date.from(d1.toInstant().plusSeconds(3600));
			Game gam1 = new Game(d1);
			repository.save(gam1);
		};
	}
*/
	@Bean
	public CommandLineRunner initParticipationData(GamePlayerRepository repository) {
		return (args) -> {
			Player pla1 = new Player("j.bauer@ctu.gov");
			Player pla2 = new Player("c.obrian@ctu.gov");
			Date d1 = new Date();
			Game gam1 = new Game(d1);

			GamePlayer part1 = new GamePlayer(d1);
			part1.setPlayer(pla1);
			part1.setGame(gam1);
			repository.save(part1);

			GamePlayer part2 = new GamePlayer(d1);
			part1.setPlayer(pla2);
			part1.setGame(gam1);
			repository.save(part2);


		};
	}
}

