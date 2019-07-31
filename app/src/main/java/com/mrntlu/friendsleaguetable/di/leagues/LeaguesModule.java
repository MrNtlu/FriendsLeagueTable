package com.mrntlu.friendsleaguetable.di.leagues;

import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leagues.LeaguesRecyclerAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class LeaguesModule {

    @Provides
    static LeaguesRecyclerAdapter provideLeaguesRecyclerAdapter(){
        return new LeaguesRecyclerAdapter();
    }
}
