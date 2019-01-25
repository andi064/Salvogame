package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController

@RequestMapping("/api")
public class AppController {

    @Autowired
    private PlayerRepository playerRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private GamePlayerRepository gamePlayerRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/players")
    public List<Player> getPlayer() {
        return playerRepo.findAll();
    }

    @RequestMapping("/games")
    public List<Object> getGame() {
        return gameRepo.findAll().stream().map(Game -> gameDTO(Game)).collect(toList());
    }

    private Map<String, Object> gameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayerDTO(gamePlayer))
                .collect(toList()));
//        dto.put("scores", game.getGamePlayers()
//                .stream()
//                .filter(gamePlayer -> gamePlayer.getPlayer().getScore(game)!=null)
//                .map(gamePlayer ->scoreDTO(gamePlayer.getScores()))
//                .collect(toList()));
        return dto;
    }

    private Map<String, Object> playerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("email", player.getEmail());
        return dto;
    }

    private Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", playerDTO(gamePlayer.getPlayer()));
        if(gamePlayer.getScores() != null) {
            dto.put("score", gamePlayer.getScores().getScore());
        }else{
            dto.put("score", null);
        }
       // dto.put("score", gamePlayer.getScores().getScore()); // add the score here to have easy access
        return dto;
    }

    @RequestMapping("/gamePlayers")
    public List<GamePlayer> getGamePlayer() {
        return gamePlayerRepo.findAll();
    }

    @RequestMapping("/game_view/{gameId}")
    public Map<String, Object> gameViewDTO(@PathVariable Long gameId) {
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gameId);

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getDate());
        dto.put("GamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gamePlayer1 -> gamePlayerDTO(gamePlayer1)).collect(Collectors.toList()));
        dto.put("Ships", gamePlayer.getShips().stream().map(ship -> shipDTO(ship)).collect(Collectors.toList()));
        dto.put("Salvos", gamePlayer.getSalvos().stream().map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
        if (thyEnemy(gamePlayer)!=null) {
            dto.put("thyEnemySalvoes", thyEnemy(gamePlayer).getSalvos().stream().map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
        }

        return dto;
    }

    @RequestMapping("/leaderboard")
    public Map<String, Object> leaderboardDTO(GamePlayer gamePlayer) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        List<GamePlayer> gamePlayers = gamePlayerRepo.findAll();

        for (GamePlayer gp: gamePlayers) {
            Map<String, Object> scores = new LinkedHashMap<String, Object>();

            if (!scores.containsKey(gp.getPlayer().getUserName())){
                scores.put("wins", gp.getPlayer().getScores().stream().filter(score -> score.getScore() == 1).count());
                scores.put("draws", gp.getPlayer().getScores().stream().filter(score -> score.getScore() == 0.5).count());
                scores.put("losses", gp.getPlayer().getScores().stream().filter(score -> score.getScore() == 0).count());
                scores.put("total", gp.getPlayer().getScores().stream().mapToDouble(score ->score.getScore()).sum());
                dto.put(gp.getPlayer().getUserName(), scores);
            }

        }

        return dto;
    }

    @RequestMapping("/ships")
    public Map<String, Object> shipDTO(Ship ship) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("Ship_Id", ship.getId());
        dto.put("Type", ship.getType());
        dto.put("Ship_Location", ship.getMyLocation());

        return dto;
    }

    @RequestMapping("/scores")
    public Map<String, Object> scoreDTO(Score score) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("Score", score.getScore());
        dto.put("FinishDate", score.getDate());
        dto.put("Player", playerDTO(score.getPlayer()));
        return dto;
    }

    @RequestMapping("/salvos")
    public Map<String, Object> salvoDTO(Salvo salvo) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("Salvo_Id", salvo.getId());
        dto.put("Turn", salvo.getTurn());
        dto.put("Salvo_Location", salvo.getSalvoLocation());
        return dto;
    }

    public GamePlayer thyEnemy (GamePlayer gamePlayer){
        return gamePlayer.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1.getId() !=gamePlayer.getId()).findAny().orElse(null);
    }
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
           @RequestParam String userName, @RequestParam String email, @RequestParam String password) {

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(userName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


