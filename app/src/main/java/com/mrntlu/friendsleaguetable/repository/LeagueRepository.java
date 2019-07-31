package com.mrntlu.friendsleaguetable.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;
import com.mrntlu.friendsleaguetable.models.Match;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.persistence.LeagueDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class LeagueRepository {

    @NonNull
    private final LeagueDao leagueDao;

    @Inject
    public LeagueRepository(@NonNull LeagueDao leagueDao){
        this.leagueDao=leagueDao;
    }

//Gets
    public LiveData<List<LeaguePlayers>> getLeaguesAndPlayersByStatus(League.LeagueStatus status,int limit){
        return leagueDao.getLeaguesAndPlayersByStatus(status,limit);
    }

    public LiveData<List<Match>> getMatchesByLeagueId(String leagueId, int limit){
        return leagueDao.getMatchesByLeagueId(leagueId, limit);
    }

    public LiveData<LeaguePlayers> getPlayersAndLeagueByLeagueName(String leagueName){
        return leagueDao.getPlayersAndLeagueByLeagueName(leagueName);
    }

//Insert
    public Completable insertNewLeague(League league, List<Player> players){
        return Completable.fromRunnable(()-> leagueDao.insertNewLeague(league,players))
                .subscribeOn(Schedulers.io());
    }

    public Completable insertNewMatch(Match match){
        return Completable.fromRunnable(()->leagueDao.insertNewMatch(match))
                .subscribeOn(Schedulers.io());
    }

//Update
    public Completable updateLeague(League league,List<Player> deleteList,List<Player> addList){
        return Completable.fromRunnable(()-> leagueDao.updateLeague(league,deleteList,addList))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateLeagueStatus(League league){
        return Completable.fromRunnable(()-> leagueDao.updateLeague(league))
                .subscribeOn(Schedulers.io());
    }
//Delete
    public Completable deleteMatch(Match match,Player player1,Player player2){
        return Completable.fromRunnable(() -> leagueDao.deleteMatchAndUpdateLeague(match,player1,player2))
                .subscribeOn(Schedulers.io());
    }

    public void deleteAll(){
        leagueDao.deleteAllLeagues();
    }

    public Completable deleteLeague(League league){
        return Completable.fromRunnable(()->leagueDao.deleteLeague(league))
                .subscribeOn(Schedulers.io());
    }
}
