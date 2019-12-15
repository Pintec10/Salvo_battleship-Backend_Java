package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @Autowired
    private ShipRepository shiprepo;

    @Autowired
    private SalvoRepository salvorepo;

    @Autowired
    private ScoreRepository scorerepo;


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
        return gpList.stream().sorted(Comparator.comparing(GamePlayer::getId)).map(oneItem -> gamePlayerMapper(oneItem)).collect(Collectors.toList());
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


    // ---------- PLACE NEW SHIPS ----------
    @RequestMapping(value="/games/players/{gamePlayerID}/ships", method = RequestMethod.POST)
    public ResponseEntity<String> placeShips(@PathVariable Long gamePlayerID, @RequestBody List<Ship> shipList,
                                                 Authentication authentication) {
        GamePlayer currentGamePlayer = gprepo.findById(gamePlayerID).orElse(null);

        if ((authenticatedUserMapper(authentication).get("id") == null) ||
                (currentGamePlayer == null) ||
                (authenticatedUserMapper(authentication).get("id") != currentGamePlayer.getPlayer().getId())) {
            return new ResponseEntity<>("No valid user logged in ", HttpStatus.UNAUTHORIZED);
        } else {
            Set<Ship> boatFleet = currentGamePlayer.getBoatFleet();
            if (boatFleet.size() != 0) {
                return new ResponseEntity<>("Ships have been placed already!", HttpStatus.FORBIDDEN);
            } else {
                shipList.stream().forEach(oneShip -> {
                    oneShip.setGamePlayer(currentGamePlayer);
                    shiprepo.save(oneShip);});
                return new ResponseEntity<>("ships successfully added", HttpStatus.CREATED);
            }
        }
    }


    // ---------- PLACE NEW SALVOES ----------
    @RequestMapping(value="/games/players/{gamePlayerID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<String> placeSalvoes(@PathVariable Long gamePlayerID, @RequestBody Salvo newSalvo,
                                             Authentication authentication) {
        GamePlayer currentGamePlayer = gprepo.findById(gamePlayerID).orElse(null);

        if ((authenticatedUserMapper(authentication).get("id") == null) ||
                (currentGamePlayer == null) ||
                (authenticatedUserMapper(authentication).get("id") != currentGamePlayer.getPlayer().getId())) {
            return new ResponseEntity<>("No valid user logged in ", HttpStatus.UNAUTHORIZED);
        } else {
            newSalvo.setTurn(currentGamePlayer.getGame().getCurrentTurn());
            Set<Salvo> previousSalvoes = currentGamePlayer.getFiredSalvoes();
            if ((previousSalvoes.stream().anyMatch
                    (onePrevSalvo -> onePrevSalvo.getTurn() == newSalvo.getTurn())) ||
            currentGamePlayer.getGame().getCurrentTurn() == 0) {
                return new ResponseEntity<>("Cannot fire a Salvo now!", HttpStatus.FORBIDDEN);
            }
            else if (currentGamePlayer.getGame().isGameOver()) {
                return new ResponseEntity<>("Game has finished already!", HttpStatus.FORBIDDEN);
            }
            else if (newSalvo.getLocations().size() > 5) {
                return new ResponseEntity<>("Maximum five shots allowed!", HttpStatus.FORBIDDEN);
            }
            else {
            newSalvo.setGamePlayer(currentGamePlayer);
            //newSalvo.setTurn(currentGamePlayer.getGame().getCurrentTurn());
            salvorepo.save(newSalvo);
            return new ResponseEntity<>("Salvo successfully fired!", HttpStatus.CREATED);
            }
        }

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
        Set<Map<String, Object>> salvoView = currentGamePlayer.getGame().getParticipationsPerGame()
                .stream()
                .map(oneGamePlayer -> GPStreamerForSalvo(oneGamePlayer))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        gameView.put("salvoes", salvoView);

        //battle status info
        Set<Map<String, Object>> battleStatusView = currentGamePlayer.getGame().getParticipationsPerGame()
                .stream()
                .map(oneGamePlayer -> GPMapperforBattleStatus(currentGamePlayer, oneGamePlayer))
                .collect(Collectors.toSet());
        gameView.put("battleStatus", battleStatusView);

        //check if boat setup is complete
        Boolean setupComplete = true;
        if(currentGame.getCurrentTurn() == 0) {
            Boolean allFleetsDeployed =
                    currentGame.getParticipationsPerGame().stream().allMatch(oneGP -> oneGP.getBoatFleet().size() > 0);
            if (allFleetsDeployed && currentGame.getParticipationsPerGame().size() == 2) {
                currentGame.setCurrentTurn(1);
                gamerepo.save(currentGame);
            } else {setupComplete = false;}
        }
        gameView.put("setupComplete", setupComplete);

        //turn updater
        Boolean turnCompleted = currentGame.getParticipationsPerGame().stream().map(oneGP -> oneGP.getFiredSalvoes())
                .flatMap(Collection::stream)
                .filter(oneSalvo -> oneSalvo.getTurn() == currentGame.getCurrentTurn()).count() == 2;
        if (turnCompleted) {
            currentGame.setCurrentTurn(currentGame.getCurrentTurn() + 1);
            gamerepo.save(currentGame);
        }
        gameView.put("currentTurn", currentGame.getCurrentTurn());

        //check if game is over and assign scores (REMEMBER UPDATE SCORES BEFORE SETTING GAME OVER)
        Boolean viewerWon = isAnnihilated(getOpponent(currentGamePlayer));
        Boolean opponentWon = isAnnihilated(currentGamePlayer);
        Date endTime = new Date();
        if (turnCompleted && !currentGame.isGameOver()) {
            Double viewerPoints = 0.0;
            Double opponentPoints = 0.0;
            if (viewerWon && opponentWon) {
                viewerPoints = 0.5;
                opponentPoints = 0.5;
            } else if (viewerWon) {
                viewerPoints = 1.0;
            } else if (opponentWon) {
                opponentPoints = 1.0;
            }

            if(viewerWon || opponentWon) {
                scorerepo.save(new Score(currentGame, currentGamePlayer.getPlayer(), viewerPoints, endTime));
                scorerepo.save(new Score(currentGame, getOpponent(currentGamePlayer).getPlayer(), opponentPoints, endTime));
                currentGame.setGameOver(true);
                gamerepo.save(currentGame);
            }
        }

        gameView.put("gameOver", currentGame.isGameOver());
        gameView.put("viewerVictory", viewerWon);
        gameView.put("opponentVictory", opponentWon);


        //checking if Gp in request URL is actually the current authenticated player
        if (currentGamePlayer != null &&
                authenticatedUserMapper(authentication).get("id") == currentGamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>(gameView, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(makeMap("error", "nice try!"), HttpStatus.UNAUTHORIZED);
        }

    }


// -----> methods for battleStatus

    private GamePlayer getOpponent (GamePlayer oneGP) {
        return oneGP.getGame().getParticipationsPerGame().stream()
                .filter(gp -> !gp.getId().equals(oneGP.getId()))
                .findFirst().orElse(null);
    }

    private Set<String> getAllShots (GamePlayer oneGP ) {
        if (oneGP != null) {
        return oneGP.getFiredSalvoes().stream().map(oneSalvo -> oneSalvo.getLocations())
                .flatMap(Collection::stream).collect(Collectors.toSet());}
        else return null;
    }

    private Set<String> getAllShipLocations (GamePlayer oneGP) {
        return oneGP.getBoatFleet().stream().map(oneShip -> oneShip.getLocation())
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Map<String, Object> GPMapperforBattleStatus(GamePlayer viewer, GamePlayer oneGP) {

        Map<String, Object> output = new LinkedHashMap<>();
        output.put("gamePlayer", oneGP.getId());
        output.put("hitsReceived", hitsReceivedCalculator(oneGP, true));
        output.put("missReceived", hitsReceivedCalculator(oneGP,false));
        output.put("fleetStatus", fleetStatusChecker(viewer, oneGP));
        return output;
    }

    private Set<String> hitsReceivedCalculator (GamePlayer oneGP, Boolean gettingSuccessfulHits) {
        Set<String> ownerShipLocations = getAllShipLocations(oneGP);
        Set<String> opponentShots = getAllShots(getOpponent(oneGP));
        if (opponentShots != null ) {
            Set<String> output;
            if(gettingSuccessfulHits) {
                output = opponentShots.stream().filter(ownerShipLocations::contains)
                                .collect(Collectors.toSet());
            } else {
                output = opponentShots.stream().filter(oneShot -> !ownerShipLocations.contains(oneShot))
                                .collect(Collectors.toSet());
            }
            return output;
        } else return null;
    }

    private Set<Map<String, Object>> fleetStatusChecker(GamePlayer viewer, GamePlayer oneGP) {
        Set<Map<String, Object>> output = oneGP.getBoatFleet().stream()
                .sorted(Comparator.comparing(oneShip -> oneShip.getLocation().size()))
                .map(oneShip -> shipStatusMapper(viewer, oneShip))
                .collect(Collectors.toSet());
        return output;
    }

    // gpList.stream().sorted(Comparator.comparing(GamePlayer::getId)).map(

    private Map<String, Object> shipStatusMapper (GamePlayer viewer, Ship oneShip) {
        //GamePlayer shipOwner = oneShip.getGamePlayer();
        //Set<String> opponentShots = getAllShots(getOpponent(shipOwner));
        //Boolean isSunk = false;
        //Object totalDamage = 0;
        /*if (opponentShots != null) {
            isSunk = oneShip.getLocation().stream().allMatch(opponentShots::contains);

            if (viewer.getId() == oneShip.getGamePlayer().getId()) {
                totalDamage = oneShip.getLocation().stream().filter(opponentShots::contains).count();
            } else {totalDamage = null;}
        }*/

        Map output = new LinkedHashMap();
        output.put("type", oneShip.getType());
        output.put("isSunk", isSunk(oneShip));
        output.put("totalDamage", getTotalDamage(oneShip, viewer));
        output.put("maxHP", oneShip.getLocation().size());

        return output;
    }

    private Boolean isSunk (Ship oneShip) {
        GamePlayer shipOwner = oneShip.getGamePlayer();
        Set<String> opponentShots = getAllShots(getOpponent(shipOwner));
        if (opponentShots != null) {
            return oneShip.getLocation().stream().allMatch(opponentShots::contains);
        } else return false;
    }

    private Long getTotalDamage (Ship oneShip, GamePlayer viewer) {
        GamePlayer shipOwner = oneShip.getGamePlayer();
        Set<String> opponentShots = getAllShots(getOpponent(shipOwner));
        if (opponentShots != null) {
            if (viewer.getId() == oneShip.getGamePlayer().getId()) {
                return oneShip.getLocation().stream().filter(opponentShots::contains).count();
            } else {return null;}
        } else return null;
    }

    private Boolean isAnnihilated (GamePlayer checkedPlayer) {
        if (checkedPlayer != null) {
            return checkedPlayer.getBoatFleet().stream()
                    .allMatch(oneShip -> isSunk(oneShip));
        } else return false;
    }


// -----> methods for salvoes
    private List<Map<String, Object>> GPStreamerForSalvo(GamePlayer oneGamePlayer) {
        List<Map<String, Object>> output = firedSalvoesStreamer(oneGamePlayer.getFiredSalvoes());
        return output;
    }

    private List<Map<String, Object>> firedSalvoesStreamer(Set<Salvo> salvoesList) {
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


// -----> methods for ships
    private Map<String, Object> shipMapper(Ship oneShip) {
        Map <String, Object> output = new LinkedHashMap<>();
        output.put("type", oneShip.getType());
        output.put("location", oneShip.getLocation());
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

    @RequestMapping(value="/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (authenticatedUserMapper(authentication).get("id") != null) {
            Date now = new Date();
            Game newGame = new Game(now, 0);
            Player currentPlayer = plrepo.findByUserName(authentication.getName());
            GamePlayer newGp = new GamePlayer(now, currentPlayer, newGame);
            gamerepo.save(newGame);
            gprepo.save(newGp);
            return new ResponseEntity<>(makeMap("gpid", newGp.getId().toString()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("error", "no logged in user"), HttpStatus.UNAUTHORIZED);
        }
    }


    // JOIN AN EXISTING GAME

    @RequestMapping(value="/game/{gameID}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameID, Authentication authentication) {
        Game requestedGame = gamerepo.findById(gameID).orElse(null);
        if (authenticatedUserMapper(authentication).get("id") == null) {
            return new ResponseEntity<>(makeMap("error", "No logged in user"), HttpStatus.UNAUTHORIZED);
        } else if (requestedGame == null) {
            return new ResponseEntity<>(makeMap("error", "The requested game does not exist"), HttpStatus.FORBIDDEN);
        } else if (requestedGame.getParticipationsPerGame().size() > 1) {
            return new ResponseEntity<>(makeMap("error", "This game is full"), HttpStatus.FORBIDDEN);
        } else {
            Date now = new Date();
            Player currentPlayer = plrepo.findByUserName(authentication.getName());
            GamePlayer newGp = new GamePlayer(now, currentPlayer, requestedGame);
            gprepo.save(newGp);
            return new ResponseEntity<>(makeMap("gpid", newGp.getId().toString()), HttpStatus.CREATED);
        }
    }

}

