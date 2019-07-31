package com.mrntlu.friendsleaguetable.models.converters;

import androidx.room.TypeConverter;

import com.mrntlu.friendsleaguetable.models.Player;

import static com.mrntlu.friendsleaguetable.models.Player.PlayerImage.*;

public class PlayerImageConverter {

    @TypeConverter
    public static Player.PlayerImage toImage(int drawableId){
        if (drawableId==FootballField.getImage()){
            return FootballField;
        }else if (drawableId==Archery.getImage()){
            return Archery;
        }else if (drawableId==Collage.getImage()){
            return Collage;
        }else if (drawableId==CraneField.getImage()){
            return CraneField;
        }else if (drawableId==Dices.getImage()){
            return Dices;
        }else if (drawableId==Foosball.getImage()){
            return Foosball;
        }else if (drawableId==Headphone.getImage()){
            return Headphone;
        }else if (drawableId==Joystick.getImage()){
            return Joystick;
        }else if (drawableId==Medal.getImage()){
            return Medal;
        }else if (drawableId==Pacman.getImage()){
            return Pacman;
        }else if (drawableId==PingPong.getImage()){
            return PingPong;
        }else if (drawableId==Podium.getImage()){
            return Podium;
        }else if (drawableId==RacingCar.getImage()){
            return RacingCar;
        }else if (drawableId==SteeringWheel.getImage()){
            return SteeringWheel;
        }else if (drawableId==Swords.getImage()){
            return Swords;
        }else if (drawableId==Snooker.getImage()){
            return Snooker;
        }else if (drawableId==Tower.getImage()){
            return Tower;
        }else if (drawableId==Target.getImage()){
            return Target;
        }else{
            throw new IllegalArgumentException("Couldn't recognize image");
        }
    }

    @TypeConverter
    public static int toInteger(Player.PlayerImage image){
        return image.getImage();
    }
}
