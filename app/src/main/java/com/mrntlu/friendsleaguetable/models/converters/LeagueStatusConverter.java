package com.mrntlu.friendsleaguetable.models.converters;

import androidx.room.TypeConverter;

import com.mrntlu.friendsleaguetable.models.League;

import static com.mrntlu.friendsleaguetable.models.League.LeagueStatus.*;

public class LeagueStatusConverter {

    @TypeConverter
    public static League.LeagueStatus toStatus(int status){
        if (status== ONGOING.getCode()){
            return ONGOING;
        }else if (status==FINISHED.getCode()){
            return FINISHED;
        }else{
            throw new IllegalArgumentException("Couldn't recognize status");
        }
    }

    @TypeConverter
    public static int toInteger(League.LeagueStatus status) {
        return status.getCode();
    }
}
