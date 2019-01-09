package com.codeoftheweb.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo) {
		return (args) -> {
			// save a couple of players
			Player p1 = new Player("Jack_Bauer", "j.bauer@ctu.gov");
			Player p2 = new Player("Chloe_O'Brian", "c.obrian@ctu.gov"); // storing players on the player class
			Player p3 = new Player("kim_Bauer", "kim_bauer@gmail.com");
			Player p4 = new Player("Andi064", "love_nurse@hospital69.com");

			playerRepo.save(p1);
			playerRepo.save(p2);
			playerRepo.save(p3);
			playerRepo.save(p4);

			// save a couple of ship Locations

			ArrayList<String> cruiser=new ArrayList<String>();
			cruiser.add("A1");//Adding object in arraylist
			cruiser.add("B1");

			ArrayList<String> destroyer=new ArrayList<String>();
			destroyer.add("H1");//Adding object in arraylist
			destroyer.add("J1");
			destroyer.add("K1");
			destroyer.add("L1");


			Ship sh1 = new Ship ("cruiser",cruiser);
			Ship sh2 = new Ship ("destroyer",destroyer);

			shipRepo.save(sh1);
			shipRepo.save(sh2);


			// save a couple of game dates

			Game g1 = new Game();
			Game g2 = new Game();
			Game g3 = new Game();
			Game g4 = new Game();

			gameRepo.save(g1);
			gameRepo.save(g2);

			// save a couple of gameplayers

			gamePlayerRepo.save(new GamePlayer(g1, p1));
			gamePlayerRepo.save(new GamePlayer(g1, p2));
			gamePlayerRepo.save(new GamePlayer(g2, p3));
			gamePlayerRepo.save(new GamePlayer(g2, p4));


		};
	}

}

