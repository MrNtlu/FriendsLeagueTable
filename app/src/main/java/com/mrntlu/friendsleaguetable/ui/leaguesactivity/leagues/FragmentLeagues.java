package com.mrntlu.friendsleaguetable.ui.leaguesactivity.leagues;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leagues.LeaguesRecyclerAdapter;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leagues.LeagueDeleteCallback;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leagues.LeagueSelectedCallback;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.LeagueDetailsActivity;
import com.mrntlu.friendsleaguetable.utils.Constants;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FragmentLeagues extends DaggerFragment implements LeagueSelectedCallback, LeagueDeleteCallback {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    LeaguesRecyclerAdapter adapter;

    @BindView(R.id.leaguesRV)
    RecyclerView leaguesRV;

    private LeaguesViewModel viewModel;
    private League.LeagueStatus leagueStatus;
    //Pagination
    private int queryLimit=14;
    private boolean isLoading=false;
    private int lastItem=0;

    public FragmentLeagues(League.LeagueStatus leagueStatus){
        this.leagueStatus=leagueStatus;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_leagues, container, false);
        ButterKnife.bind(this,v);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel= ViewModelProviders.of(this,viewModelProviderFactory).get(LeaguesViewModel.class);
        //view.getContext().deleteDatabase(LeagueDatabase.DATABASE_NAME);
        initRecyclerView();
        requestLeaguesAndPlayers();
    }

    private void requestLeaguesAndPlayers(){
        viewModel.getLeaguesAndPlayersByStatus(leagueStatus,queryLimit).observe(getViewLifecycleOwner(),(leaguePlayers) -> {
            if (isLoading) {
                adapter.setLeagues(leaguePlayers,lastItem);
                if (leaguePlayers.size()>14) isLoading=false;
            }else{
                adapter.setLeagues(leaguePlayers,0);
            }
            lastItem=leaguePlayers.size();
        });
    }

    private void initRecyclerView(){
        adapter.setRequestManager(requestManager);
        adapter.setSelectedCallback(this);
        adapter.setLeagueDeleteCallback(this);
        GridLayoutManager gridLayout=new GridLayoutManager(getContext(),2);
        gridLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position)==adapter.NO_LEAGUES_HOLDER || adapter.getItemViewType(position)==adapter.LOADING_HOLDER){
                    return 2;
                }else {
                    return 1;
                }
            }
        });
        leaguesRV.setLayoutManager(gridLayout);
        leaguesRV.setAdapter(adapter);

        leaguesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (gridLayout.findLastCompletelyVisibleItemPosition()==adapter.getItemCount()-1 && !isLoading && queryLimit==adapter.getItemCount()){
                    isLoading=true;
                    queryLimit+=14;
                    requestLeaguesAndPlayers();
                }
            }
        });
    }

    @Override
    public void onLeagueSelected(LeaguePlayers leaguePlayers) {
        Intent intent=new Intent(getActivity(), LeagueDetailsActivity.class);
        intent.putExtra("league",leaguePlayers.league);
        intent.putParcelableArrayListExtra("players",(ArrayList<? extends Parcelable>) leaguePlayers.players);
        startActivity(intent);
    }

    @Override
    public void onDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to delete?");

        builder.setPositiveButton("YES", (dialog, which) -> {
            LeaguePlayers leaguePlayers=adapter.removeLeague(position);
            if (leaguePlayers!=null) viewModel.deleteLeague(leaguePlayers.league).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {}

                        @Override
                        public void onComplete() {
                            Toast.makeText(getContext(), "Sucessfully deleted.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {}
                    });
            dialog.dismiss();
        });

        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.md_red_900));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.md_blue_grey_900));
    }
}
