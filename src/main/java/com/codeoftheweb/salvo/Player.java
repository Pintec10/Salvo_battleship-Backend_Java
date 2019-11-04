package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String userName ="";

    @OneToMany(mappedBy="player")
    private Set<GamePlayer> participationsPerPlayer = new HashSet<>();

    @OneToMany(mappedBy="player")
    private Set<Score> scoresPerPlayer = new HashSet();


    //constructors
    public Player(){};
    public Player(String userName) {
        this.userName = userName;
    }

    //methods
    public String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    public Set<GamePlayer> getParticipationsPerPlayer() {
        return participationsPerPlayer;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addParticipationPerPlayer(GamePlayer participation) {
        participationsPerPlayer.add(participation);
        participation.setPlayer(this);
    }

    public Set<Game> getGames() {
        return this.getParticipationsPerPlayer().stream().map(gp -> gp.getGame()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "username: " + userName;
    }
}
