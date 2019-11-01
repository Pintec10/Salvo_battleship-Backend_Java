package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Salvo {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gamePlayer;

    private Integer turn;

    @ElementCollection
    @Column(name="locations")
    private Set<String> locations = new HashSet<>();


    //constructors
    public Salvo(){};
    public Salvo(GamePlayer gamePlayer, Integer turn, Set<String> locations) {
        this.gamePlayer = gamePlayer;
        this.turn = turn;
        this.locations = locations;
    }


    //methods
    public Long getId() { return id;}
    public GamePlayer getGamePlayer() { return gamePlayer;}
    public Integer getTurn() { return turn;}
    public Set<String> getLocations() { return locations;}

    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}
    public void setTurn(Integer turn) {this.turn = turn;}
    public void setLocations(Set<String> locations) { this.locations = locations; }

    @Override
    public String toString() {
        return super.toString();
    }
}
