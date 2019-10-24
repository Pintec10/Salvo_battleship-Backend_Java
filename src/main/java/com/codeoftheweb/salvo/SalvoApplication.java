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
	public CommandLineRunner initParticipationData(PlayerRepository pRepository, GameRepository gRepository, GamePlayerRepository gpRepository) {
		return (args) -> {
			Player pla1 = new Player("j.bauer@ctu.gov");
			Player pla2 = new Player("c.obrian@ctu.gov");
			Player pla3 = new Player("kim_bauer@gmail.com ");
			Player pla4 = new Player("t.almeida@ctu.gov");
			pRepository.save(pla1);
			pRepository.save(pla2);
			pRepository.save(pla3);
			pRepository.save(pla4);

			Date d1 = new Date();
			Date d2 = Date.from(d1.toInstant().plusSeconds(3600));
			Date d3 = Date.from(d2.toInstant().plusSeconds(3600));
			Date d4 = Date.from(d3.toInstant().plusSeconds(3600));
			Date d5 = Date.from(d4.toInstant().plusSeconds(3600));
			Date d6 = Date.from(d5.toInstant().plusSeconds(3600));
			Date d7 = Date.from(d6.toInstant().plusSeconds(3600));
			Date d8 = Date.from(d7.toInstant().plusSeconds(3600));
			Game gam1 = new Game(d1);
			Game gam2 = new Game(d2);
			Game gam3 = new Game(d3);
			Game gam4 = new Game(d4);
			Game gam5 = new Game(d5);
			Game gam6 = new Game(d6);
			Game gam7 = new Game(d7);
			Game gam8 = new Game(d8);
			gRepository.save(gam1);
			gRepository.save(gam2);
			gRepository.save(gam3);
			gRepository.save(gam4);
			gRepository.save(gam5);
			gRepository.save(gam6);
			gRepository.save(gam7);
			gRepository.save(gam8);

			GamePlayer par01 = new GamePlayer(d1);
			par01.setPlayer(pla1);
			par01.setGame(gam1);
			gpRepository.save(par01);

			GamePlayer par02 = new GamePlayer(d1);
			par02.setPlayer(pla2);
			par02.setGame(gam1);
			gpRepository.save(par02);

			GamePlayer par03 = new GamePlayer(d2, pla1, gam2);
			GamePlayer par04 = new GamePlayer(d2, pla2, gam2);
			GamePlayer par05 = new GamePlayer(d3, pla2, gam3);
			GamePlayer par06 = new GamePlayer(d3, pla4, gam3);
			GamePlayer par07 = new GamePlayer(d4, pla2, gam4);
			GamePlayer par08 = new GamePlayer(d4, pla1, gam4);
			GamePlayer par09 = new GamePlayer(d5, pla4, gam5);
			GamePlayer par10 = new GamePlayer(d5, pla1, gam5);
			GamePlayer par11 = new GamePlayer(d6, pla3, gam6);
			GamePlayer par12 = new GamePlayer(d7, pla4, gam7);
			GamePlayer par13 = new GamePlayer(d8, pla3, gam8);
			GamePlayer par14 = new GamePlayer(d8, pla4, gam8);
			gpRepository.save(par03);
			gpRepository.save(par04);
			gpRepository.save(par05);
			gpRepository.save(par06);
			gpRepository.save(par07);
			gpRepository.save(par08);
			gpRepository.save(par09);
			gpRepository.save(par10);
			gpRepository.save(par11);
			gpRepository.save(par12);
			gpRepository.save(par13);
			gpRepository.save(par14);
		};
	}
}

