package com.mrntlu.friendsleaguetable.di;

import com.mrntlu.friendsleaguetable.di.leaguedetails.LeagueDetailsFragmentBuildersModule;
import com.mrntlu.friendsleaguetable.di.leaguedetails.LeagueDetailsModule;
import com.mrntlu.friendsleaguetable.di.leagues.LeaguesFragmentBuildersModule;
import com.mrntlu.friendsleaguetable.di.leagues.LeaguesModule;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.LeagueDetailsActivity;
import com.mrntlu.friendsleaguetable.ui.leaguesactivity.LeaguesActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {LeaguesModule.class, LeaguesFragmentBuildersModule.class,}
    )
    abstract LeaguesActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {LeagueDetailsModule.class, LeagueDetailsFragmentBuildersModule.class}
    )
    abstract LeagueDetailsActivity contributeLeagueDetails();
}
