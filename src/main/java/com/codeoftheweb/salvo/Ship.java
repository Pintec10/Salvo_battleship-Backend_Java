package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="location")
    private List<String> location = new ArrayList<>();


    //constructors
    public Ship(){};
    public Ship(String type, GamePlayer gamePlayer, List<String> location) {
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.location = location;
    }


    //methods
    public Long getId() { return id; }
    public String getType() { return type; }
    public GamePlayer getGamePlayer() { return gamePlayer; }
    public List<String> getLocation() { return location; }

    public void setType(String type) {
        this.type = type;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
    public void setLocation(List<String> location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
