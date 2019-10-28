package com.codeoftheweb.salvo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Date joinGameDate = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer")
    private List<Ship> boatFleet = new ArrayList<>();

    //constructors
    public GamePlayer() {}
    public GamePlayer(Date joinDate) {
        this.joinGameDate = joinDate;
    }
    public GamePlayer(Date joinDate, Player pla, Game gam) {
        this.joinGameDate = joinDate;
        this.player = pla;
        this.game = gam;
    }


    //methods
    public Date getJoinGameDate() {
        return joinGameDate;
    }
    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }
    public Long getId() { return id;}
    public List<Ship> getBoatFleet() { return boatFleet;}

    public void setJoinGameDate(Date date){
        this.joinGameDate = date;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setGame(Game game) {
        this.game = game;
    }

}
