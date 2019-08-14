package com.mrntlu.friendsleaguetable.persistence;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;
import com.mrntlu.friendsleaguetable.models.Match;
import com.mrntlu.friendsleaguetable.models.Player;
import java.util.List;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class LeagueDao {
//Gets
    @Query("select * from leagues where status like :status LIMIT :limit")
    public abstract LiveData<List<LeaguePlayers>> getLeaguesAndPlayersByStatus(League.LeagueStatus status,int limit);

    @Query("select * from leagues where name like :leagueName")
    public abstract LiveData<LeaguePlayers> getPlayersAndLeagueByLeagueName(String leagueName);

    @Query("select * from matches where league_id like :leagueId order by created_at desc LIMIT :limit")
    public abstract LiveData<List<Match>> getMatchesByLeagueId(String leagueId, int limit);

//Insert
    @Transaction
    public void insertNewLeague(League league, List<Player> players){
        insertLeague(league);

        if (players!=null) {
            for (Player player : players) {
                player.setLeagueId(league.getId());
                insertPlayer(player);
            }
        }
    }

    @Transaction
    public void insertNewMatch(Match match){
        Player player1=match.getPlayer1();
        Player player2=match.getPlayer2();

        setPlayersInsertOrDelete(1,match,player1,player2);

        updatePlayer(player1);
        updatePlayer(player2);
        insertMatch(match);
    }

    @Insert(onConflict = REPLACE)
    public abstract long insertMatch(Match match);

    @Insert(onConflict = REPLACE)
    public abstract long insertLeague(League league);

    @Transaction
    public void insertNewPlayer(League league, Player player){
        player.setLeagueId(league.getId());
        insertPlayer(player);
    }

    @Insert(onConflict = REPLACE)
    public abstract void insertPlayer(Player player);

//Delete
    @Transaction
    public void deleteMatchAndUpdateLeague(Match match,Player player1,Player player2){
        /*Player player1=match.getPlayer1();
        Player player2=match.getPlayer2();*/

        setPlayersInsertOrDelete(-1,match,player1,player2);

        deleteMatch(match);
        if (player1!=null) updatePlayer(player1);
        if (player2!=null) updatePlayer(player2);
    }

    @Query("DELETE FROM leagues")
    public abstract void deleteAllLeagues();

    @Delete
    public abstract Integer deleteLeague(League league);

    @Delete
    public abstract Integer deletePlayer(Player player);

    @Delete
    public abstract Integer deleteMatch(Match match);

//Update
    @Transaction
    public void updateLeague(League league,List<Player> deletePlayers,List<Player> addPlayers){
        updateLeague(league);

        for (Player player:deletePlayers){
            deletePlayer(player);
        }
        for (Player player:addPlayers){
            insertNewPlayer(league,player);
        }
    }

    @Update
    public abstract Integer updateLeague(League league);

    @Update
    public abstract Integer updatePlayer(Player player);

    private void setPlayersInsertOrDelete(int number, Match match, @Nullable Player player1,@Nullable Player player2){
        if (player1!=null){
            if (match.getPlayer1_score()>match.getPlayer2_score()) player1.setWin(player1.getWin()+number);
            else if (match.getPlayer1_score()<match.getPlayer2_score()) player1.setLose(player1.getLose()+number);
            else player1.setDraw(player1.getDraw()+number);

            player1.setGoalForOrKill(number>0?player1.getGoalForOrKill()+match.getPlayer1_score():player1.getGoalForOrKill()-match.getPlayer1_score());
            player1.setGoalAgainstOrDeath(number>0?player1.getGoalAgainstOrDeath()+match.getPlayer2_score():player1.getGoalAgainstOrDeath()-match.getPlayer2_score());
        }
        if (player2!=null){
            if (match.getPlayer1_score()>match.getPlayer2_score()) player2.setWin(player2.getWin()+number);
            else if (match.getPlayer1_score()<match.getPlayer2_score()) player2.setLose(player2.getLose()+number);
            else player2.setDraw(player2.getDraw()+number);

            player2.setGoalForOrKill(number>0?player2.getGoalForOrKill()+match.getPlayer1_score():player2.getGoalForOrKill()-match.getPlayer1_score());
            player2.setGoalAgainstOrDeath(number>0?player2.getGoalAgainstOrDeath()+match.getPlayer2_score():player2.getGoalAgainstOrDeath()-match.getPlayer2_score());
        }
    }
}
