package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {


    @Bean
    public PasswordEncoder newPlayerPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    private GameRepository gamerepo;

    @Autowired
    private GamePlayerRepository gprepo;

    @Autowired
    private PlayerRepository plrepo;


    // ---------- CURRENT AUTHENTICATED USER METHODS ----------

    public Map<String, Object> authenticatedUserMapper(Authentication authentication) {
        Map<String, Object> output = new LinkedHashMap<>();
        if (isGuest(authentication) == false){
            output.put("id", plrepo.findByUserName(authentication.getName()).getId());
            output.put("name", plrepo.findByUserName(authentication.getName()).getUserName());
        } else {
            output.put("id", null);
            output.put("name", null);
        }
        return output;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }



    // ---------- COMPLETE GAMES LIST INFO ----------

    @RequestMapping(value="/games", method = RequestMethod.GET)
    public Map<String, Object> getAllGamesInfo(Authentication authentication) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("current_user", authenticatedUserMapper(authentication));
        output.put ("games_info", gamerepo.findAll().stream().map(oneGame -> gameMapper(oneGame)).collect(Collectors.toList()));
        output.put ("scores_info", plrepo.findAll().stream().map(onePlayer -> playerMapperForScores(onePlayer)).collect(Collectors.toList()));
        return output;
    }

    private Map<String, Object> playerMapperForScores(Player onePlayer) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("player_id", onePlayer.getId());
        output.put("player_name", onePlayer.getUserName());
        output.put("scores_list", onePlayer.getScoreValueList());
        output.put("total_score", onePlayer.getTotalScore());
        output.put("count_won", onePlayer.getScoreOccurrences(1));
        output.put("count_lost", onePlayer.getScoreOccurrences(0));
        output.put("count_draw", onePlayer.getScoreOccurrences(0.5));
        output.put("count_played", onePlayer.getPlayedGames());
        return output;
    }

    private Map<String, Object> gameMapper(Game game) {
    Map<String, Object> output = new LinkedHashMap<>();
    output.put("id", game.getId());
    output.put("created", game.getCreationDate());
    output.put("gameplayers", getAllGamePlayers(game.getParticipationsPerGame()));
    return output;
    }

    private List<Object> getAllGamePlayers(Set<GamePlayer> gpList) {
        return gpList.stream().map(oneItem -> gamePlayerMapper(oneItem)).collect(Collectors.toList());
    }

    private Map<String, Object> gamePlayerMapper (GamePlayer oneGamePlayer) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("id", oneGamePlayer.getId());
        output.put("player", playerMapper(oneGamePlayer.getPlayer()) );
       if (oneGamePlayer.getScore() != null) {
           output.put("score", oneGamePlayer.getScore().getScoreValue());
       } else output.put("score", null);
        return output;
    }

    private Map<String, Object> playerMapper (Player onePlayer) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("id", onePlayer.getId());
        output.put("username", onePlayer.getUserName());
        return output;
    }



    // ---------- GAME VIEW CODE ----------

    @RequestMapping("game_view/{gamePlayerID}")
    public ResponseEntity<Map<String, Object>>  getGameViewInfo(@PathVariable Long gamePlayerID, Authentication authentication) {
        GamePlayer currentGamePlayer = gprepo.findById(gamePlayerID).orElse(null);
        Game currentGame = currentGamePlayer.getGame();
        Map<String, Object> gameView = new LinkedHashMap<>();

        //game and player info
        gameView.putAll(gameMapper(currentGame));

        //ship info
        List<Object> shipView = currentGamePlayer.getBoatFleet().stream()
                .map(item -> shipMapper(item)).collect(Collectors.toList());
        gameView.put("ships", shipView);

        //salvo info
        Set<Object> salvoView = currentGamePlayer.getGame().getParticipationsPerGame()
                .stream()
                .map(oneGamePlayer -> GPStreamerForSalvo(oneGamePlayer))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        gameView.put("salvoes", salvoView);

        //checking if Gp in request URL is actually the current authenticated player
        if (currentGamePlayer != null &&
                authenticatedUserMapper(authentication).get("id") == currentGamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>(gameView, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(makeMap("error", "nice try!"), HttpStatus.UNAUTHORIZED);
        }
    }

    private List<Object> GPStreamerForSalvo(GamePlayer oneGamePlayer) {
        List<Object> output = firedSalvoesStreamer(oneGamePlayer.getFiredSalvoes());
        return output;
    }

    private List<Object> firedSalvoesStreamer(Set<Salvo> salvoesList) {
        return salvoesList.stream()
                .map(oneSalvo -> salvoMapper(oneSalvo))
                .collect(Collectors.toList());
    }

    private Map<String, Object> salvoMapper(Salvo oneSalvo){
        Map <String, Object> output = new LinkedHashMap<>();
        output.put("gamePlayer", oneSalvo.getGamePlayer().getId());
        output.put("turn", oneSalvo.getTurn());
        output.put("locations", oneSalvo.getLocations());
        return output;
    }


    private Map<String, Object> shipMapper(Ship oneShip) {
        Map <String, Object> output = new LinkedHashMap<>();
        output.put("type", oneShip.getType());
        output.put("location", oneShip.getLocations());
        return output;
    }



    // ---------- NEW USER CREATION ----------

    @RequestMapping(value="/players", method= RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Player player) {
        String username = player.getUserName().trim();
        String password = player.getPassword();

        if (username.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No email given"), HttpStatus.FORBIDDEN);
        }

        if (username.contains(" ")) {
            return new ResponseEntity<>(makeMap("error", "No white spaces allowed"), HttpStatus.FORBIDDEN);
        }

        if (!username.contains("@")) {
            return new ResponseEntity<>(makeMap("error", "Email must contain a '@' sign"), HttpStatus.FORBIDDEN);
        }

        Player existingPlayer = plrepo.findByUserName(username);
        if (existingPlayer != null) {
            return new ResponseEntity<>(makeMap("error", "Email already in use"), HttpStatus.FORBIDDEN);
        }

        Player newUser = new Player(username, newPlayerPasswordEncoder().encode(password));
        plrepo.save(newUser);
        return new ResponseEntity<>(makeMap("username", newUser.getUserName()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, String value) {
        Map<String, Object> output = new HashMap<>();
        output.put(key, value);
        return output;
    }


    // NEW GAME CREATION

    @RequestMapping(value="/games", method= RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (authenticatedUserMapper(authentication).get("id") != null) {
            Date now = new Date();
            Game newGame = new Game(now);
            Player currentPlayer = plrepo.findByUserName(authentication.getName());
            GamePlayer newGp = new GamePlayer(now, currentPlayer, newGame);
            gamerepo.save(newGame);
            gprepo.save(newGp);
            return new ResponseEntity<>(makeMap("gpid", newGp.getId().toString()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("error", "no logged in user"), HttpStatus.UNAUTHORIZED);
        }
    }
}

//gamerepo, gprepo, plrepo