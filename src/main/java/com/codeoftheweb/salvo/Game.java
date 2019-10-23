package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date creationDate = new Date();

    @OneToMany(mappedBy="game")
    private Set<GamePlayer> participationsPerGame = new HashSet<>();

    //constructors
    public Game(){};
    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }

    //methods
    public Date getCreationDate() {
        return creationDate;
    }

    public Set getParticipationsPerGame() {
        return participationsPerGame;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void addParticipationPerGame(GamePlayer participation) {
        participationsPerGame.add(participation);
        participation.setGame(this);
    }

    @Override
    public String toString() {
        return "Game creation date: " + this.creationDate;
    }
}
