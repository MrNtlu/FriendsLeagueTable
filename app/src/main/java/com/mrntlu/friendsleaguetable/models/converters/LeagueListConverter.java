package com.mrntlu.friendsleaguetable.models.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrntlu.friendsleaguetable.models.Player;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class LeagueListConverter {

    @TypeConverter
    public static List<Player> fromString(String value) {
        if (value==null){
            return Collections.emptyList();
        }
        Gson gson=new Gson();
        Type type=new TypeToken<List<Player>>() {}.getType();
        return gson.fromJson(value,type);
    }

    @TypeConverter
    public static String fromList(List<Player> list) {
        Gson gson=new Gson();
        return gson.toJson(list);
    }
}
