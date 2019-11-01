package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {



    @Autowired
    private GameRepository gamerepo;

    @Autowired
    private GamePlayerRepository gprepo;

    @RequestMapping("/games")
    public List<Object> getAllGamesInfo() {
        return gamerepo.findAll().stream().map(oneGame -> gameMapper(oneGame)).collect(Collectors.toList());
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
        return output;
    }

    private Map<String, Object> playerMapper (Player onePlayer) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("id", onePlayer.getId());
        output.put("username", onePlayer.getUserName());
        return output;
    }


    @RequestMapping("game_view/{gamePlayerID}")
    public Map<String, Object> getGameViewInfo(@PathVariable Long gamePlayerID) {
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
        return gameView;
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
        output.put("player", oneSalvo.getGamePlayer().getPlayer().getId());
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


    /*
    Set<GamePlayer> bothGamePlayers = currentGamePlayer.getGame().getParticipationsPerGame();
        //Set<Set> bothSalvoLists = bothGamePlayers.stream().map(oneGP -> oneGP.getFiredSalvoes()).collect(Collectors.toSet());
        //bothSalvoLists.stream().map(oneSalvoList -> oneSalvoList.stream().map(oneSalvo.get))

        List<Object> salvoView = bothGamePlayers.stream().map(oneGP -> mapGpForSalvo(oneGP)).collect(Collectors.toList());


    //salvoView.add(salvoMapper(ONESALVO));



    Map<String, Object> mapGpForSalvo(GamePlayer oneGp) {
        Map<String, Object> output = new HashMap<>();
        output.put("id", oneGp.getId()); //to remove
        output.put("salvoes", getAllFiredSalvoes(oneGp.getFiredSalvoes()) );
        return output;
    }

    public List<Object> getAllFiredSalvoes(Set<Salvo> salvoList) {
        return salvoList.stream().map(oneSalvo -> salvoMapper(oneSalvo)).collect(Collectors.toList());
    }
     */


}