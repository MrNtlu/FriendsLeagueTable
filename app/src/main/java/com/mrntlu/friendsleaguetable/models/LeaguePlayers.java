package com.mrntlu.friendsleaguetable.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class LeaguePlayers {

    @Embedded
    public League league;

    @Relation(parentColumn = "id",entityColumn = "league_id",entity = Player.class)
    public List<Player> players;
}
