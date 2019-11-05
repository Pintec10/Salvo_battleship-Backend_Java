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
        return participationsPerPlayer; }
    public Set<Game> getGames() {
        return this.getParticipationsPerPlayer().stream()
                .map(gp -> gp.getGame()).collect(Collectors.toSet()); }
    public Set<Score> getScoresPerPlayer() { return scoresPerPlayer; }
    public Score getOneScore(Game game) {
        return this.getScoresPerPlayer().stream()
                .filter(oneScore -> oneScore.getGame().getId().equals(game.getId()))
                //.findFirst().orElse(null);
                .findFirst().orElse(null);
    }
    public double[] getScoreValueList() {
        if (this.getScoresPerPlayer() != null) {
        return this.getScoresPerPlayer().stream()
                .mapToDouble(oneScore -> oneScore.getScoreValue()).toArray();
        } else return new double[]{};
    }

    public double getTotalScore() {
        return this.getScoresPerPlayer().stream()
                .collect(Collectors.summingDouble(Score::getScoreValue));
    }

    public long getScoreOccurrences(double score) {
        return this.getScoresPerPlayer().stream()
                .filter(oneScore -> oneScore.getScoreValue() == score)
                .count();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void addParticipationPerPlayer(GamePlayer participation) {
        participationsPerPlayer.add(participation);
        participation.setPlayer(this);
    }


    @Override
    public String toString() {
        return "username: " + userName;
    }
}
