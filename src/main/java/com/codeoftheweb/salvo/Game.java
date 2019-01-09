package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date date;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers=new HashSet<>(); // proper way to initialize the set

    public void addGamePlayer(GamePlayer gamePlayer) { //gameRepository ???
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    //public Game() { }

    public Game() {//(String date localtime libr
      //  this.date =  LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.date =  new Date();

    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
    //    this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.date = new Date();

    }

    public Long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers(){
        return this.gamePlayers;
    }

}
