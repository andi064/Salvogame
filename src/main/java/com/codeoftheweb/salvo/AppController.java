package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired SalvoRepository salvoRepository;

    @RequestMapping("/games")
    public Map<String, Object> getGame(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if( isGuest(authentication)){
            dto.put("players", null);
        }else{
            dto.put("player", playerDTO(isLoged(authentication)));
        }
        dto.put("games", gameRepo.findAll().stream().map(Game -> gameDTO(Game)).collect(toList()));
        return dto;
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
    public Map<String, Object> gameViewDTO(@PathVariable Long gameId, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gameId);
        if(gamePlayer.getPlayer().getId() == isLoged(authentication).getId()) {

            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("id", gamePlayer.getGame().getId());
            dto.put("created", gamePlayer.getGame().getDate());
            dto.put("GamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gamePlayer1 -> gamePlayerDTO(gamePlayer1)).collect(Collectors.toList()));
            dto.put("Ships", gamePlayer.getShips().stream().map(ship -> shipDTO(ship)).collect(Collectors.toList()));
            dto.put("Salvos", gamePlayer.getSalvos().stream().map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
            if (thyEnemy(gamePlayer) != null) {
                dto.put("thyEnemySalvoes", thyEnemy(gamePlayer).getSalvos().stream().map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
            }

            return dto;
        }else {
            return sentInfo("cheater",HttpStatus.FORBIDDEN);
        }
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
    public ResponseEntity<Object> register( @RequestBody Player player){

        if (player.getUserName().isEmpty() || player.getEmail().isEmpty() || player.getPassword().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByEmail(player.getEmail()) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(player.getUserName(), player.getEmail(),player.getPassword()));
        return new ResponseEntity<>(sentInfo("player",player.getUserName()),HttpStatus.CREATED); // player created mssg
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player isLoged (Authentication authentication){
        return playerRepo.findByEmail(authentication.getName());
    }

    private Map<String, Object> sentInfo(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication){
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(game ,isLoged(authentication));
        game.addGamePlayer(gamePlayer);
        gameRepo.save(game);
        gamePlayerRepo.save(gamePlayer);
        return new ResponseEntity<>(sentInfo("gpID", gamePlayer.getId()),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(Authentication authentication, @PathVariable Long nn){
        if(isLoged(authentication)==null){ // if player is logged in !!
            return new ResponseEntity<>( "Not Logged in" ,HttpStatus.FORBIDDEN);
        }
        if(gameRepo.getOne(nn)==null){
            return new ResponseEntity<>( "No game with that shit" ,HttpStatus.FORBIDDEN);
        }

        if(gameRepo.getOne(nn).getGamePlayers().size()==2){
            return new ResponseEntity<>( "Game is full" ,HttpStatus.FORBIDDEN);
        }

        Game currentGame = gameRepo.getOne(nn);
        Player currentPlayer = isLoged(authentication);

        GamePlayer joiningGP = new GamePlayer(currentGame, currentPlayer);
        gamePlayerRepo.save(joiningGP);
        return new ResponseEntity<>(sentInfo("gamePlayerID", joiningGP.getId()),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> placeShips(Authentication authentication, @PathVariable Long gamePlayerId,@RequestBody Set<Ship> ships){
       GamePlayer gamePlayer= gamePlayerRepo.getOne(gamePlayerId);
        if(isLoged(authentication)==null){ // if player is logged in !!
            return new ResponseEntity<>(sentInfo( "Error","Not Logged in" ),HttpStatus.UNAUTHORIZED);
        }
        if( gamePlayer == null){
            return new ResponseEntity<>(sentInfo( "Error","No GamePlayer found" ),HttpStatus.UNAUTHORIZED);
        }
        if(isLoged(authentication).getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(sentInfo( "Error","Unauthorized" ) ,HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getShips().size()!= 0){
            return new ResponseEntity<>(sentInfo( "Error","No ships" ) ,HttpStatus.FORBIDDEN);
        }if(ships.size() !=  5){
            return new ResponseEntity<>(sentInfo( "Error","Has to be 5 ships" ) ,HttpStatus.FORBIDDEN);
        }
        for (Ship ship : ships){
            gamePlayer.addShip(ship);
            shipRepository.save(ship);
        }
        return new ResponseEntity<>(sentInfo("gamePlayerID", gamePlayer.getId()),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Object> placeSalvos(Authentication authentication, @PathVariable Long gamePlayerId,@RequestBody Salvo salvos){
        GamePlayer gamePlayer= gamePlayerRepo.getOne(gamePlayerId);
        if(isLoged(authentication)==null){ // if player is logged in !!
            return new ResponseEntity<>(sentInfo( "Error","Not Logged in" ),HttpStatus.UNAUTHORIZED);
        }
        if( gamePlayer == null){
            return new ResponseEntity<>(sentInfo( "Error","No GamePlayer found" ),HttpStatus.UNAUTHORIZED);
        }
        if(isLoged(authentication).getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(sentInfo( "Error","Unauthorized" ) ,HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getSalvos().size()!= 0) {
            return new ResponseEntity<>(sentInfo("Error", "No Salvos"), HttpStatus.FORBIDDEN);
        }
            gamePlayer.addSalvo(salvos);
            salvoRepository.save(salvos);

        return new ResponseEntity<>(sentInfo("gamePlayerID", gamePlayer.getId()),HttpStatus.CREATED);
    }
}


