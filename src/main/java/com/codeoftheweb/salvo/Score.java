package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private double scoreValue;

    private Date finishDate;


    //constructors
    public Score(){};
    public Score(Game game, Player player, double scoreValue, Date finishDate) {
        this.game = game;
        this.player = player;
        this.scoreValue = scoreValue;
        this.finishDate = finishDate;
    }


    //methods

    public Long getId() {return id;}
    public Game getGame() {return game;}
    public Player getPlayer() {return player;}
    public double getScoreValue() {return scoreValue;}
    public Date getFinishDate() {return finishDate;}

    public void setGame(Game game) {this.game = game;}
    public void setPlayer(Player player) {this.player = player;}
    public void setScoreValue(double scoreValue) {this.scoreValue = scoreValue;}
    public void setFinishDate(Date finishDate) {this.finishDate = finishDate;}

    @Override
    public String toString() {
        return super.toString();
    }
}
