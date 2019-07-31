package com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguestandings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguestandings.LeagueStandingsRecyclerAdapter;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.utils.CommonUsages;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class FragmentLeagueStandings extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    LeagueStandingsRecyclerAdapter adapter;

    @BindView(R.id.leagueStandings)
    RecyclerView leagueStandingsRV;

    @BindView(R.id.leagueStandingsToolbar)
    Toolbar toolbar;

    private LeaguesViewModel viewModel;
    private League league;

    public FragmentLeagueStandings(League league) {
        this.league=league;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_league_standings, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this,viewModelProviderFactory).get(LeaguesViewModel.class);

        CommonUsages.setToolbar(toolbar,"Standings",getFragmentManager());
        setupObservers();
        initRecyclerView();
    }


    private void initRecyclerView(){
        adapter.setRequestManager(requestManager);
        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext());
        leagueStandingsRV.setLayoutManager(linearLayout);
        leagueStandingsRV.setAdapter(adapter);
    }

    private void setupObservers(){
        viewModel.getPlayersAndLeagueByLeagueName(league.getName()).observe(getViewLifecycleOwner(), leaguePlayers -> {
            adapter.setLeaguePlayers(leaguePlayers.league,leaguePlayers.players);
            league=leaguePlayers.league;
        });
    }
}
