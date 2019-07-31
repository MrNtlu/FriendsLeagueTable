package com.mrntlu.friendsleaguetable.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mrntlu.friendsleaguetable.models.converters.DateConverter;

import java.util.Date;
import java.util.UUID;

@Entity(
        tableName = "matches",
        foreignKeys = @ForeignKey(
                entity = League.class,
                parentColumns = "id",
                childColumns = "league_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        indices = @Index("league_id")
)
public class Match implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NonNull
    private String id;

    @ColumnInfo(name = "league_id")
    private String leagueId;

    @Embedded(prefix = "player_1")
    private Player player1;

    @Embedded(prefix = "player_2")
    private Player player2;

    @ColumnInfo(name = "player_1_score")
    private int player1_score;

    @ColumnInfo(name = "player_2_score")
    private int player2_score;

    @ColumnInfo(name = "created_at")
    @TypeConverters(DateConverter.class)
    private Date createdAt;

    public Match(String leagueId, Player player1, Player player2, int player1_score, int player2_score, Date createdAt) {
        this.id = UUID.randomUUID().toString();
        this.leagueId = leagueId;
        this.player1 = player1;
        this.player2 = player2;
        this.player1_score = player1_score;
        this.player2_score = player2_score;
        this.createdAt = createdAt;
    }

    @Ignore
    public Match() {
    }

    protected Match(Parcel in) {
        id = in.readString();
        leagueId = in.readString();
        player1 = in.readParcelable(Player.class.getClassLoader());
        player2 = in.readParcelable(Player.class.getClassLoader());
        player1_score = in.readInt();
        player2_score = in.readInt();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getPlayer1_score() {
        return player1_score;
    }

    public void setPlayer1_score(int player1_score) {
        this.player1_score = player1_score;
    }

    public int getPlayer2_score() {
        return player2_score;
    }

    public void setPlayer2_score(int player2_score) {
        this.player2_score = player2_score;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(leagueId);
        parcel.writeParcelable(player1,i);
        parcel.writeParcelable(player2,i);
        parcel.writeInt(player1_score);
        parcel.writeInt(player2_score);
        parcel.writeSerializable(createdAt);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", leagueId='" + leagueId + '\'' +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", player1_score=" + player1_score +
                ", player2_score=" + player2_score +
                ", createdAt=" + createdAt +
                '}';
    }
}
