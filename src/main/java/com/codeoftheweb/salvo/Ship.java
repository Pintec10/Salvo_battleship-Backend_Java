package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private GamePlayer gameplayer;

    @ElementCollection
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();


    //constructors
    public Ship(){};
    public Ship(String type, GamePlayer gameplayer, List<String> locations) {
        this.type = type;
        this.gameplayer = gameplayer;
        this.locations = locations;
    }


    //methods
    public Long getId() { return id; }
    public String getType() { return type; }
    public GamePlayer getGameplayer() { return gameplayer; }
    public List<String> getLocations() { return locations; }

    public void setType(String type) {
        this.type = type;
    }
    public void setGameplayer(GamePlayer gameplayer) {
        this.gameplayer = gameplayer;
    }
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
