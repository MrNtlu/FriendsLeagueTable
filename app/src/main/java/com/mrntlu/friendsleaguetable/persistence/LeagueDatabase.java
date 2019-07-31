package com.mrntlu.friendsleaguetable.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Match;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.models.converters.LeagueStatusConverter;

@Database(entities = {League.class, Player.class, Match.class}, version = 3)
@TypeConverters({LeagueStatusConverter.class})
public abstract class LeagueDatabase extends RoomDatabase {

    public static final String DATABASE_NAME="leagues_db";

    private static LeagueDatabase instance;

    public abstract LeagueDao getLeagueDao();
}
