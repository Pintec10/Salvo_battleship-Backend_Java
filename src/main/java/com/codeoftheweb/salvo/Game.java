package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Date creationDate = new Date();

    @OneToMany(mappedBy="game")
    private Set<GamePlayer> participationsPerGame = new HashSet<>();

    @OneToMany(mappedBy="game")
    private Set<Score> scoresPerGame = new HashSet<>();

    //constructors
    public Game(){};
    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }


    //methods
    public Date getCreationDate() {
        return creationDate;
    }

    public Set<GamePlayer> getParticipationsPerGame() {
        return participationsPerGame;
    }

    public Long getId() {
        return id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void addParticipationPerGame(GamePlayer participation) {
        participationsPerGame.add(participation);
        participation.setGame(this);
    }

    @JsonIgnore
    public Set<Player> getPlayers() {
        return this.participationsPerGame.stream().map( gp -> gp.getPlayer()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Game creation date: " + this.creationDate;
    }
}
