package com.mrntlu.friendsleaguetable.di.leaguedetails;

import android.app.Application;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguehub.LeagueHubPagerAdapter;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguematches.LeagueMatchesRecyclerAdapter;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguestandings.LeagueStandingsRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class LeagueDetailsModule {

    @Provides
    static LeagueHubPagerAdapter provideLeagueHubPagerAdapter(Application application) {
        return new LeagueHubPagerAdapter(application);
    }

    @Provides
    static LeagueMatchesRecyclerAdapter provideLeagueMatchesRecyclerAdapter(){
        return new LeagueMatchesRecyclerAdapter();
    }

    @Provides
    static LeagueStandingsRecyclerAdapter provideLeagueStandingsRecyclerAdapter(){
        return new LeagueStandingsRecyclerAdapter();
    }
}
