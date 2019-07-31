package com.mrntlu.friendsleaguetable.models.converters;

import androidx.room.TypeConverter;

import com.mrntlu.friendsleaguetable.models.League;

import static com.mrntlu.friendsleaguetable.models.League.LeagueType.FPS;
import static com.mrntlu.friendsleaguetable.models.League.LeagueType.MOBA;
import static com.mrntlu.friendsleaguetable.models.League.LeagueType.SPORT;

public class LeagueTypeConverter {

    @TypeConverter
    public static League.LeagueType toStatus(int status){
        if (status== SPORT.getCode()){
            return SPORT;
        }else if (status==FPS.getCode()){
            return FPS;
        }else if (status==MOBA.getCode()){
            return MOBA;
        }else{
            throw new IllegalArgumentException("Couldn't recognize status");
        }
    }

    @TypeConverter
    public static int toInteger(League.LeagueType status) {
        return status.getCode();
    }
}
