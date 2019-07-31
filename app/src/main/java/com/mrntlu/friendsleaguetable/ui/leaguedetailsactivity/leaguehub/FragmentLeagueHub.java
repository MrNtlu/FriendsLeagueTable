package com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.RequestManager;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguehub.LeagueHubPagerAdapter;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguestandings.LeagueStandingsRecyclerAdapter;
import com.mrntlu.friendsleaguetable.callbacks.leaguedetailsactivity.LeagueHubPagerOnClick;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguedetails.FragmentLeagueDetails;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguematches.FragmentLeagueMatches;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguestandings.FragmentLeagueStandings;
import com.mrntlu.friendsleaguetable.ui.leaguesactivity.LeaguesActivity;
import com.mrntlu.friendsleaguetable.utils.CommonUsages;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FragmentLeagueHub extends DaggerFragment implements LeagueHubPagerOnClick {

    @Inject
    LeagueHubPagerAdapter pagerAdapter;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    LeagueStandingsRecyclerAdapter adapter;

    @Nullable
    @BindView(R.id.backButton)
    ImageButton backButton;

    @Nullable
    @BindView(R.id.finishLeagueButton)
    Button finishLeagueButton;

    @Nullable
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Nullable
    @BindView(R.id.leagueTitle)
    TextView leagueTitle;

    @Nullable
    @BindView(R.id.leagueStatusText)
    TextView leagueStatus;

//LeagueStats
    @Nullable
    @BindView(R.id.leagueStatsToolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.championPlayerText)
    TextView championPlayerText;

    @Nullable
    @BindView(R.id.mostScoredPlayerText)
    TextView mostScoredPlayerText;

    @Nullable
    @BindView(R.id.mostPlayedPlayerText)
    TextView mostPlayedPlayerText;

    @Nullable
    @BindView(R.id.leastScoredPlayerText)
    TextView leastScoredPlayerText;

    @Nullable
    @BindView(R.id.leastPlayedPlayerText)
    TextView leastPlayedPlayerText;

    @Nullable
    @BindView(R.id.leagueStandingsRV)
    RecyclerView standingsRV;

    @Nullable
    @BindView(R.id.resumeLeagueButton)
    Button resumeLeagueButton;

    @Nullable
    @BindView(R.id.seeAllMatchesButton)
    Button seeAllMatchesButton;

    private LeaguesViewModel viewModel;
    private League league;
    private List<Player> players;
    private Unbinder unbinder;
    private FragmentTransaction fragmentTransaction;
    //LeagueStats
    private Player championPlayer;
    private Player mostScoredPlayer;
    private Player mostPlayedPlayer;
    private Player leastScoredPlayer;
    private Player leastPlayedPlayer;

    public FragmentLeagueHub(League league, List<Player> players){
        this.league=league;
        if (players!=null) this.players=players;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        if (league!=null && league.getStatus()== League.LeagueStatus.FINISHED){
            v = inflater.inflate(R.layout.fragment_league_stats, container, false);
            unbinder = ButterKnife.bind(this, v);
        }else{
            v = inflater.inflate(R.layout.fragment_league_hub, container, false);
            unbinder = ButterKnife.bind(this, v);
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this,viewModelProviderFactory).get(LeaguesViewModel.class);
        fragmentTransaction=((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();

        leagueStatusController(view);
        if (league.getStatus()== League.LeagueStatus.ONGOING) {
            leagueTitle.setText(league.getName());
            setViewPager();
        }

        setListeners();
    }

    private void leagueStatusController(View view) {
        if (league.getStatus()== League.LeagueStatus.ONGOING){
            leagueStatus.setText(league.getStatus().toString());
            leagueStatus.setTextColor(league.getStatus()== League.LeagueStatus.ONGOING?
                    ContextCompat.getColor(view.getContext(),R.color.md_green_600):
                    ContextCompat.getColor(view.getContext(),R.color.md_red_600));

            finishLeagueButton.setVisibility(View.VISIBLE);
        }else if (league.getStatus()== League.LeagueStatus.FINISHED){
            CommonUsages.setToolbar(toolbar,"League Stats",getFragmentManager()).setNavigationOnClickListener(view1 -> {
                Intent intent = new Intent(getActivity(), LeaguesActivity.class);
                startActivity(intent);
            });

            initRecyclerView();
            setPlayersByStats();

            championPlayerText.setText(championPlayer.getName());
            mostPlayedPlayerText.setText(mostPlayedPlayer.getName());
            mostScoredPlayerText.setText(mostScoredPlayer.getName());
            leastPlayedPlayerText.setText(leastPlayedPlayer.getName());
            leastScoredPlayerText.setText(leastScoredPlayer.getName());
        }
    }

    private void initRecyclerView(){
        adapter.setLeaguePlayers(league,players);
        adapter.setRequestManager(requestManager);
        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext());
        standingsRV.setLayoutManager(linearLayout);
        standingsRV.setAdapter(adapter);
    }

    private void setPlayersByStats(){
        championPlayer=players.get(0);
        mostPlayedPlayer=players.get(0);
        mostScoredPlayer=players.get(0);
        leastPlayedPlayer=players.get(0);
        leastScoredPlayer=players.get(0);

        for (Player player:players){
            if (player.getGoalForOrKill()<leastScoredPlayer.getGoalForOrKill()){
                leastScoredPlayer=player;
            }
            if (player.getGoalForOrKill()>mostScoredPlayer.getGoalForOrKill()){
                mostScoredPlayer=player;
            }
            if ((player.getWin()+player.getLose()+player.getDraw())>(mostPlayedPlayer.getWin()+mostPlayedPlayer.getDraw()+mostPlayedPlayer.getLose())){
                mostPlayedPlayer=player;
            }
            if ((player.getWin()+player.getLose()+player.getDraw())<(leastPlayedPlayer.getWin()+leastPlayedPlayer.getDraw()+leastPlayedPlayer.getLose())){
                leastPlayedPlayer=player;
            }
        }
    }

    private void setListeners(){
        if (league.getStatus()== League.LeagueStatus.ONGOING) {
            backButton.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), LeaguesActivity.class);
                startActivity(intent);
            });

            finishLeagueButton.setOnClickListener(view -> {
                viewModel.getPlayersAndLeagueByLeagueName(league.getName()).observe(getViewLifecycleOwner(),leaguePlayers -> {
                    this.league=leaguePlayers.league;
                    this.players=leaguePlayers.players;
                    updateLeagueStatus(League.LeagueStatus.FINISHED);
                });
            });
        }else{
            resumeLeagueButton.setOnClickListener(view -> {
                updateLeagueStatus(League.LeagueStatus.ONGOING);
            });

            seeAllMatchesButton.setOnClickListener(view -> {
                CommonUsages.startTransaction(getFragmentManager(),new FragmentLeagueMatches(league));
            });
        }
    }

    private void updateLeagueStatus(League.LeagueStatus leagueStatus){
        league.setStatus(leagueStatus);
        viewModel.updateLeagueStatus(league).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                CommonUsages.startTransaction(getFragmentManager(),new FragmentLeagueHub(league,players));
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    private void setViewPager(){
        pagerAdapter.setPagerOnClick(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPadding(150,0,150,0);
        viewPager.setCurrentItem(1);
    }

    private void startTransaction(Fragment fragment){
        fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewPagerClick(int position) {
        Fragment fragment;
        if (position==0){
            fragment=new FragmentLeagueMatches(league);
        }else if(position==1){
            fragment=new FragmentLeagueStandings(league);
        }else{
            fragment=new FragmentLeagueDetails(league);
        }
        startTransaction(fragment);
    }

    @Override
    public void onResume() {
        if (viewPager!=null) viewPager.setCurrentItem(1);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (standingsRV!=null) standingsRV.setAdapter(null);
        if (viewPager!=null) viewPager.setAdapter(null);//To prevent memory leak
        unbinder.unbind();
        super.onDestroyView();
    }
}
