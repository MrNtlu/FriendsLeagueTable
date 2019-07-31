package com.mrntlu.friendsleaguetable.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.models.converters.PlayerImageConverter;
import java.util.UUID;


@Entity(
        tableName = "players",
        foreignKeys = @ForeignKey(
                entity = League.class,
                parentColumns = "id",
                childColumns = "league_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        indices = @Index("league_id")
)
public class Player implements Parcelable {

    public enum PlayerImage{
        FootballField(R.drawable._football_field),
        Archery(R.drawable._archery),
        Collage(R.drawable._collage),
        CraneField(R.drawable._crane_field),
        Dices(R.drawable._dices),
        Foosball(R.drawable._foosball),
        Headphone(R.drawable._headphone),
        Joystick(R.drawable._joystick),
        Medal(R.drawable._medal),
        Pacman(R.drawable._pacman),
        PingPong(R.drawable._ping_pong),
        Podium(R.drawable._podium),
        RacingCar(R.drawable._racing_car),
        Snooker(R.drawable._snooker),
        SteeringWheel(R.drawable._steering_wheel),
        Swords(R.drawable._swords),
        Tower(R.drawable._tower),
        Target(R.drawable._target);

        private int drawableId;

        PlayerImage(int drawableId){
            this.drawableId=drawableId;
        }

        public int getImage(){
            return drawableId;
        }
    }

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NonNull
    private String id;

    @ColumnInfo(name = "league_id")
    private String leagueId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "win")
    private int win;

    @ColumnInfo(name = "lose")
    private int lose;

    @ColumnInfo(name = "draw")
    private int draw;

    @ColumnInfo(name = "goal_for_kill")
    private int goalForOrKill;

    @ColumnInfo(name = "goal_against_death")
    private int goalAgainstOrDeath;

    @ColumnInfo(name="player_image")
    @TypeConverters(PlayerImageConverter.class)
    private PlayerImage playerImage;

    public Player(String name, PlayerImage playerImage) {
        this.id= UUID.randomUUID().toString();
        this.name = name;
        this.win = 0;
        this.lose = 0;
        this.draw = 0;
        this.goalForOrKill = 0;
        this.goalAgainstOrDeath = 0;
        this.playerImage=playerImage;
    }

    @Ignore//For testing only
    public Player(Player player) {
        id=UUID.randomUUID().toString();
        leagueId=player.leagueId;
        name=player.name;
        win=player.win;
        lose=player.lose;
        draw =player.draw;
        goalForOrKill=player.goalForOrKill;
        goalAgainstOrDeath=player.goalAgainstOrDeath;
        playerImage=player.playerImage;
    }

    @Ignore
    public Player() {
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getGoalForOrKill() {
        return goalForOrKill;
    }

    public void setGoalForOrKill(int goalForOrKill) {
        this.goalForOrKill = goalForOrKill;
    }

    public int getGoalAgainstOrDeath() {
        return goalAgainstOrDeath;
    }

    public void setGoalAgainstOrDeath(int goalAgainstOrDeath) {
        this.goalAgainstOrDeath = goalAgainstOrDeath;
    }

    public PlayerImage getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(PlayerImage playerImage) {
        this.playerImage = playerImage;
    }

    protected Player(Parcel in) {
        id = in.readString();
        leagueId = in.readString();
        name = in.readString();
        win=in.readInt();
        lose=in.readInt();
        draw =in.readInt();
        goalForOrKill=in.readInt();
        goalAgainstOrDeath=in.readInt();
        playerImage=PlayerImage.valueOf(in.readString());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(leagueId);
        parcel.writeString(name);
        parcel.writeInt(win);
        parcel.writeInt(lose);
        parcel.writeInt(draw);
        parcel.writeInt(goalForOrKill);
        parcel.writeInt(goalAgainstOrDeath);
        parcel.writeString(playerImage.name());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", leagueId='" + leagueId + '\'' +
                ", name='" + name + '\'' +
                ", win=" + win +
                ", lose=" + lose +
                ", draw=" + draw +
                ", goalForOrKill=" + goalForOrKill +
                ", goalAgainstOrDeath=" + goalAgainstOrDeath +
                ", playerImage=" + playerImage +
                '}';
    }
}
