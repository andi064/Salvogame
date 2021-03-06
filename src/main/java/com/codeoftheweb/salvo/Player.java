package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String userName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>(); // proper way to initialize the set

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    public void addGamePlayer(GamePlayer gamePlayer) { //gameRepository ???
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Player() {
    }

    public Player(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Score getScore(Game game) {
        return scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    public Set<Score> getScores() {
        return scores;
    }

    public String toString() {
        return userName + " " + email;
    }
}
