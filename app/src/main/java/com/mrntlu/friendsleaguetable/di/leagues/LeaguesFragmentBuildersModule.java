package com.mrntlu.friendsleaguetable.di.leagues;

import com.mrntlu.friendsleaguetable.ui.leaguesactivity.leaguecontroller.FragmentLeagueController;
import com.mrntlu.friendsleaguetable.ui.leaguesactivity.leagues.FragmentLeagues;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LeaguesFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract FragmentLeagues contributeFragmentLeagues();

    @ContributesAndroidInjector
    abstract FragmentLeagueController contributeFragmentLeagueController();
}
