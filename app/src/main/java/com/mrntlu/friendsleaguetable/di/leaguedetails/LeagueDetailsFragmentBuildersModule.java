package com.mrntlu.friendsleaguetable.di.leaguedetails;

import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguedetails.FragmentLeagueDetails;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguehub.FragmentLeagueHub;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguematches.FragmentLeagueMatches;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguestandings.FragmentLeagueStandings;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LeagueDetailsFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract FragmentLeagueHub contributeFragmentLeagueHub();

    @ContributesAndroidInjector
    abstract FragmentLeagueMatches contributeFragmentLeagueMatches();

    @ContributesAndroidInjector
    abstract FragmentLeagueStandings contributeFragmentLeagueStandings();

    @ContributesAndroidInjector
    abstract FragmentLeagueDetails contributeFragmentLeagueDetails();
}
