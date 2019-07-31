package com.mrntlu.friendsleaguetable.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;
import com.mrntlu.friendsleaguetable.models.Match;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.repository.LeagueRepository;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Completable;

public class LeaguesViewModel extends ViewModel {

    private final LeagueRepository leagueRepository;

    @Inject
    public LeaguesViewModel(LeagueRepository leagueRepository){
        this.leagueRepository=leagueRepository;
    }

//Gets
    public LiveData<List<LeaguePlayers>> getLeaguesAndPlayersByStatus(League.LeagueStatus status,int limit){
        return leagueRepository.getLeaguesAndPlayersByStatus(status,limit);
    }

    public LiveData<List<Match>> getMatchesByLeagueId(String leagueId, int limit){
        return leagueRepository.getMatchesByLeagueId(leagueId,limit);
    }

    public LiveData<LeaguePlayers> getPlayersAndLeagueByLeagueName(String leagueName){
        return leagueRepository.getPlayersAndLeagueByLeagueName(leagueName);
    }

//Insert
    public Completable insertNewLeague(League league, List<Player> players){
        return leagueRepository.insertNewLeague(league,players);
    }

    public Completable insertNewMatch(Match match){
        return leagueRepository.insertNewMatch(match);
    }

//Update
    public Completable updateLeague(League league,List<Player> deleteList,List<Player> addList) {
        return leagueRepository.updateLeague(league,deleteList,addList);
    }

    public Completable updateLeagueStatus(League league) {
        return leagueRepository.updateLeagueStatus(league);
    }
//Delete
    public Completable deleteMatch(Match match,Player player1,Player player2){
        return leagueRepository.deleteMatch(match,player1,player2);
    }

    public void deleteAllLeagues(){
        leagueRepository.deleteAll();
    }

    public Completable deleteLeague(League league){
        return leagueRepository.deleteLeague(league);
    }
}
