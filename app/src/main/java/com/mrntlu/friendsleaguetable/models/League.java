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

import com.mrntlu.friendsleaguetable.models.converters.DateConverter;
import com.mrntlu.friendsleaguetable.models.converters.LeagueListConverter;
import com.mrntlu.friendsleaguetable.models.converters.LeagueStatusConverter;
import com.mrntlu.friendsleaguetable.models.converters.LeagueTypeConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(
        tableName = "leagues",
        indices = @Index(value = "name",unique = true)
)
public class League implements Parcelable {

    public enum LeagueType{
        SPORT(0),
        FPS(1),
        MOBA(2);

        private int code;

        LeagueType(int code){
            this.code=code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum LeagueStatus{
        FINISHED(0),
        ONGOING(1);

        private int code;

        LeagueStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    @TypeConverters(LeagueTypeConverter.class)
    private LeagueType type;

    @ColumnInfo(name = "status")
    @TypeConverters(LeagueStatusConverter.class)
    private LeagueStatus status;

    @ColumnInfo(name = "created_at")
    @TypeConverters(DateConverter.class)
    private Date createdAt;

    public League(String name, LeagueType type, LeagueStatus status, Date createdAt) {
        this.id= UUID.randomUUID().toString();
        this.name = name;
        //this.players = players;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Ignore
    public League() {
    }

    @Ignore
    public League(League league) {
        id=league.id;
        name=league.name;
        type=league.type;
        status=league.status;
        createdAt=league.createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LeagueType getType() {
        return type;
    }

    public void setType(LeagueType type) {
        this.type = type;
    }

    public LeagueStatus getStatus() {
        return status;
    }

    public void setStatus(LeagueStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    protected League(Parcel in) {
        id = in.readString();
        name = in.readString();
        //players=in.readParcelableList(new ArrayList<Player>(),Player.class.getClassLoader());
        type=LeagueType.valueOf(in.readString());
        status=LeagueStatus.valueOf(in.readString());
        createdAt=(Date) in.readSerializable();
    }

    public static final Creator<League> CREATOR = new Creator<League>() {
        @Override
        public League createFromParcel(Parcel in) {
            return new League(in);
        }

        @Override
        public League[] newArray(int size) {
            return new League[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        //parcel.writeParcelableList(players,i);
        parcel.writeString(type.name());
        parcel.writeString(status.name());
        parcel.writeSerializable(createdAt);
    }
}
