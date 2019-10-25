package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initParticipationData(PlayerRepository pRepository, GameRepository gRepository,
			GamePlayerRepository gpRepository, ShipRepository sRepository) {
		return (args) -> {
			Player pla1 = new Player("j.bauer@ctu.gov");
			Player pla2 = new Player("c.obrian@ctu.gov");
			Player pla3 = new Player("kim_bauer@gmail.com");
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


			List<String> loc01 = Arrays.asList("H2", "H3", "H4");
			List<String> loc02 = Arrays.asList("E1", "F1", "G1");
			List<String> loc03 = Arrays.asList("B4", "B5");
			List<String> loc04 = Arrays.asList("B5", "C5", "D5");
			List<String> loc05 = Arrays.asList("F1", "F2");
			List<String> loc06 = Arrays.asList("B5", "C5", "D5");
			List<String> loc07 = Arrays.asList("C6", "C7");
			List<String> loc08 = Arrays.asList("A2", "A3", "A4");
			List<String> loc09 = Arrays.asList("G6", "H6");
			List<String> loc10 = Arrays.asList("B5", "C5", "D5");
			List<String> loc11 = Arrays.asList("C6", "C7");
			List<String> loc12 = Arrays.asList("A2", "A3", "A4");
			List<String> loc13 = Arrays.asList("G6", "H6");
			List<String> loc14 = Arrays.asList("B5", "C5", "D5");
			List<String> loc15 = Arrays.asList("C6", "C7");
			List<String> loc16 = Arrays.asList("A2", "A3", "A4");
			List<String> loc17 = Arrays.asList("G6", "H6");
			List<String> loc18 = Arrays.asList("B5", "C5", "D5");
			List<String> loc19 = Arrays.asList("C6", "C7");
			List<String> loc20 = Arrays.asList("A2", "A3", "A4");
			List<String> loc21 = Arrays.asList("G6", "H6");
			List<String> loc22 = Arrays.asList("B5", "C5", "D5");
			List<String> loc23 = Arrays.asList("C6", "C7");
			List<String> loc24 = Arrays.asList("B5", "C5", "D5");
			List<String> loc25 = Arrays.asList("C6", "C7");
			List<String> loc26 = Arrays.asList("A2", "A3", "A4");
			List<String> loc27 = Arrays.asList("G6", "H6");
			sRepository.save(new Ship("Destroyer", par01, loc01));
			sRepository.save(new Ship("Submarine", par01, loc02));
			sRepository.save(new Ship("Patrol Boat", par01, loc03));
			sRepository.save(new Ship("Destroyer", par02, loc04));
			sRepository.save(new Ship("Patrol Boat", par02, loc05));
			sRepository.save(new Ship("Destroyer", par03, loc06));
			sRepository.save(new Ship("Patrol Boat", par03, loc07));
			sRepository.save(new Ship("Submarine", par04, loc08));
			sRepository.save(new Ship("Patrol Boat", par04, loc09));
			sRepository.save(new Ship("Destroyer", par05, loc10));
			sRepository.save(new Ship("Patrol Boat", par05, loc11));
			sRepository.save(new Ship("Submarine", par06, loc12));
			sRepository.save(new Ship("Patrol Boat", par06, loc13));
			sRepository.save(new Ship("Destroyer", par07, loc14));
			sRepository.save(new Ship("Patrol Boat", par07, loc15));
			sRepository.save(new Ship("Submarine", par08, loc16));
			sRepository.save(new Ship("Patrol Boat", par08, loc17));
			sRepository.save(new Ship("Destroyer", par09, loc18));
			sRepository.save(new Ship("Patrol Boat", par09, loc19));
			sRepository.save(new Ship("Submarine", par10, loc20));
			sRepository.save(new Ship("Patrol Boat", par10, loc21));
			sRepository.save(new Ship("Destroyer", par11, loc22));
			sRepository.save(new Ship("Patrol Boat", par11, loc23));
			sRepository.save(new Ship("Destroyer", par13, loc24));
			sRepository.save(new Ship("Patrol Boat", par13, loc25));
			sRepository.save(new Ship("Submarine", par14,loc26));
			sRepository.save(new Ship("Patrol Boat", par14, loc27));

		};
	}
}

