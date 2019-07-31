package com.mrntlu.friendsleaguetable.utils;


import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Player;

import java.util.ArrayList;
import java.util.Date;

public class Constants {
    public static final String TAG="TestTag";

    public final static League TEST_LEAGUE=new League("Test League", League.LeagueType.SPORT, League.LeagueStatus.ONGOING,new Date());
    public final static League TEST_LEAGUE2=new League("League Num 2", League.LeagueType.FPS, League.LeagueStatus.FINISHED,new Date());

    public final static Player TEST_PLAYER1=new Player("Burak", Player.PlayerImage.Archery);
    public final static Player TEST_PLAYER2=new Player("Bugra", Player.PlayerImage.Joystick);
    public final static Player TEST_PLAYER3=new Player("Harun", Player.PlayerImage.Foosball);

    public static ArrayList<Player> setPlayers() {
        return (new ArrayList<Player>(){{
            add(TEST_PLAYER1);
            add(TEST_PLAYER2);
        }});
    }
}
