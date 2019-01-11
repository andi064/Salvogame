package com.codeoftheweb.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

            // save a couple of game dates

            Game g1 = new Game();
            Game g2 = new Game();
            Game g3 = new Game();
            Game g4 = new Game();

            gameRepo.save(g1);
            gameRepo.save(g2);

            // save a couple of ship Locations

            ArrayList<String> cruiser = new ArrayList<String>();
            cruiser.add("A1");//Adding object in arraylist
            cruiser.add("B1");

            ArrayList<String> cruiser1 = new ArrayList<String>();
            cruiser1.add("C1");//Adding object in arraylist
            cruiser1.add("D1");

            ArrayList<String> destroyer = new ArrayList<String>();
            destroyer.add("H1");//Adding object in arraylist
            destroyer.add("J1");
            destroyer.add("K1");
            destroyer.add("L1");

            ArrayList<String> battleshit = new ArrayList<String>();
            battleshit.add("H1");//Adding object in arraylist
            battleshit.add("J1");
            battleshit.add("K1");
            battleshit.add("L1");
            battleshit.add("M1");
            battleshit.add("N1");

            Ship sh1 = new Ship("p1_cruiser", cruiser);
            Ship sh2 = new Ship("p1_destroyer", destroyer);
            Ship sh3 = new Ship("p1_destroyer", cruiser1);
            Ship sh4 = new Ship("p1_battleshit", battleshit);

            Ship sh5 = new Ship("p2_cruiser", cruiser);
            Ship sh6 = new Ship("p2_destroyer", destroyer);
            Ship sh7 = new Ship("p2_destroyer", cruiser1);
            Ship sh8 = new Ship("p2_battleshit", battleshit);

            // save a couple of gameplayers

            GamePlayer gp1 = new GamePlayer(g1, p1);
            // call a method to get set the ships to the gameplayer
            gp1.addShip(sh1);
            gp1.addShip(sh2);
            gp1.addShip(sh3);
            gp1.addShip(sh4);
            GamePlayer gp2 = new GamePlayer(g1, p2);
            gp2.addShip(sh5);
            gp2.addShip(sh6);
            gp2.addShip(sh7);
            gp2.addShip(sh8);
            GamePlayer gp3 = new GamePlayer(g2, p3);
            GamePlayer gp4 = new GamePlayer(g2, p4);

            gamePlayerRepo.save(gp1);
            gamePlayerRepo.save(gp2);
            gamePlayerRepo.save(gp3);
            gamePlayerRepo.save(gp4);

            shipRepo.save(sh1);
            shipRepo.save(sh2);
            shipRepo.save(sh3);
            shipRepo.save(sh4);
            shipRepo.save(sh5);
            shipRepo.save(sh6);
            shipRepo.save(sh7);
            shipRepo.save(sh8);

        };
    }

}

