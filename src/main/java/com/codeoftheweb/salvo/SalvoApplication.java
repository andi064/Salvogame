package com.codeoftheweb.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import sun.security.krb5.internal.crypto.Des;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo, SalvoRepository salvoRep) {
        return (args) -> {
            // save a couple of players
            Player p1 = new Player("Jack_Bauer", "j.bauer@ctu.gov");
            Player p2 = new Player("Chloe_O'Brian", "c.obrian@ctu.gov"); // storing players on the player class
            Player p3 = new Player("t.almeida", "t.almeida@ctu.gov");
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
            Game g5 = new Game();
            Game g6 = new Game();
            Game g7 = new Game();
            Game g8 = new Game();

            gameRepo.save(g1);
            gameRepo.save(g2);
            gameRepo.save(g3);
            gameRepo.save(g4);
            gameRepo.save(g5);
            gameRepo.save(g6);
            gameRepo.save(g7);
            gameRepo.save(g8);

            // save a couple of ship Locations

//            ArrayList<String> Carrier = new ArrayList<String>();
//            Carrier.add("A1");//Adding object in arraylist
//            Carrier.add("B1");
//            Carrier.add("C1");
//            Carrier.add("D1");
//            Carrier.add("E1");

            ArrayList<String> Battleship = new ArrayList<String>();
            Battleship.add("H1");//Adding object in arraylist
            Battleship.add("H2");

            ArrayList<String> Submarine = new ArrayList<String>();
            Submarine.add("E1");//Adding object in arraylist
            Submarine.add("F1");
            Submarine.add("G1");

            ArrayList<String> Destroyer = new ArrayList<String>();
            Destroyer.add("B5");//Adding object in arraylist
            Destroyer.add("C5");
            Destroyer.add("D5");

            ArrayList<String> PatrolBoat = new ArrayList<String>();
            PatrolBoat.add("C6");//Adding object in arraylist
            PatrolBoat.add("C7");

//            ArrayList<String> Carrier_2 = new ArrayList<String>();
//            Carrier.add("A3");//Adding object in arraylist
//            Carrier.add("B3");
//            Carrier.add("C3");
//            Carrier.add("D3");
//            Carrier.add("E3");

            ArrayList<String> Battleship_2 = new ArrayList<String>();
            Battleship_2.add("H3");//Adding object in arraylist
            Battleship_2.add("H4");

            ArrayList<String> Submarine_2 = new ArrayList<String>();
            Submarine_2.add("A4");//Adding object in arraylist
            Submarine_2.add("A5");
            Submarine_2.add("A6");

            ArrayList<String> Destroyer_2 = new ArrayList<String>();
            Destroyer_2.add("B5");//Adding object in arraylist
            Destroyer_2.add("C5");
            Destroyer_2.add("D5");

            ArrayList<String> PatrolBoat_2 = new ArrayList<String>();
            PatrolBoat_2.add("F1");//Adding object in arraylist
            PatrolBoat_2.add("F2");


            //Ship sh1 = new Ship("Carrier_1", Carrier );
            Ship sh2 = new Ship("Battleship", Battleship);
            Ship sh3 = new Ship("Submarine", Submarine);
            Ship sh4 = new Ship("Destroyer", Destroyer);
            Ship sh5 = new Ship("PatrolBoat", PatrolBoat);
           // Ship sh6 = new Ship("p2_Carrier", Carrier_2);
            Ship sh7 = new Ship("Destroyer", Destroyer_2);
            Ship sh8 = new Ship("Submarine", Submarine_2);
            Ship sh9 = new Ship("Battleship", Battleship_2);
            Ship sh10 = new Ship("PatrolBoat", PatrolBoat_2);

            // save a couple of salvo fucks
            ArrayList<String> salvo1 = new ArrayList<String>();
            salvo1.add("B9");//Adding object in arraylist

            ArrayList<String> salvo2 = new ArrayList<String>();
            salvo2.add("F1");//Adding object in arraylist

            // save a couple of gameplayers

            GamePlayer gp1 = new GamePlayer(g1, p1);
            // call a method to get set the ships to the gameplayer
           // gp1.addShip(sh1);
            gp1.addShip(sh2);
            gp1.addShip(sh3);
            gp1.addShip(sh4);
            gp1.addShip(sh5);
            Salvo s1 = new Salvo(1,salvo1,gp1);

            gp1.addSalvo(s1);
            GamePlayer gp2 = new GamePlayer(g1, p2);
          //  gp2.addShip(sh6);
            gp2.addShip(sh7);
            gp2.addShip(sh8);
            gp2.addShip(sh9);
            gp2.addShip(sh10);
         //   GamePlayer gp3 = new GamePlayer(g2, p3);
         //   GamePlayer gp4 = new GamePlayer(g2, p4);

            gamePlayerRepo.save(gp1);
            gamePlayerRepo.save(gp2);
         //   gamePlayerRepo.save(gp3);
         //   gamePlayerRepo.save(gp4);
            salvoRep.save(s1);
           // shipRepo.save(sh1);
            shipRepo.save(sh2);
            shipRepo.save(sh3);
            shipRepo.save(sh4);
            shipRepo.save(sh5);
          //  shipRepo.save(sh6);
            shipRepo.save(sh7);
            shipRepo.save(sh8);
            shipRepo.save(sh9);
            shipRepo.save(sh10);



        };
    }

}

