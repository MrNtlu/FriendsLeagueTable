package com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguematches;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguematches.LeagueMatchesRecyclerAdapter;
import com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguematches.RecyclerMatchesTouchHelper;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Match;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.utils.CommonUsages;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FragmentLeagueMatches extends DaggerFragment implements RecyclerMatchesTouchHelper.RecyclerItemTouchHelperListener {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LeagueMatchesRecyclerAdapter adapter;

    @BindView(R.id.leagueMatchesToolbar)
    Toolbar toolbar;

    @BindView(R.id.leagueMatchesRV)
    RecyclerView leagueMatchesRV;

    @BindView(R.id.addMatchButton)
    FloatingActionButton addMatchButton;

    private LeaguesViewModel viewModel;
    private League league;
    private List<Player> players;

    private Dialog matchDialog;
    private Player player1;
    private Player player2;
    private int player1Score;
    private int player2Score;
    //Pagination
    private int queryLimit=10;
    private boolean isLoading=false;
    private int lastItem=0;

    public FragmentLeagueMatches(League league) {
        this.league=league;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_league_matches, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this,viewModelProviderFactory).get(LeaguesViewModel.class);
        matchDialog=new Dialog(view.getContext());

        setupObservers();
        initRecyclerView();
        CommonUsages.setToolbar(toolbar,"Match Results",getFragmentManager());
        requestMatches();

        if (league.getStatus()== League.LeagueStatus.ONGOING) addMatchButton.setOnClickListener((view1 -> showMatchDialog()));

        else if (league.getStatus()== League.LeagueStatus.FINISHED) addMatchButton.hide();

    }

    private void setupObservers(){
        viewModel.getPlayersAndLeagueByLeagueName(league.getName()).observe(getViewLifecycleOwner(),leaguePlayer -> {
            this.players=leaguePlayer.players;
        });
    }

    private void showMatchDialog(){
        matchDialog.setContentView(R.layout.dialog_league_match);
        if (matchDialog.getWindow()!=null) matchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Spinner player1Spinner=matchDialog.findViewById(R.id.player1Spinner);
        Spinner player2Spinner=matchDialog.findViewById(R.id.player2Spinner);
        Spinner player1ScoreSpinner=matchDialog.findViewById(R.id.player1ScoreSpinner);
        Spinner player2ScoreSpinner=matchDialog.findViewById(R.id.player2ScoreSpinner);

        setDialogAdapters(player1ScoreSpinner,player2ScoreSpinner,player1Spinner,player2Spinner);

        player1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                player1=players.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        player2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                player2=players.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        player1ScoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                player1Score=Integer.valueOf((String)adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        player2ScoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                player2Score=Integer.valueOf((String)adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        matchDialog.findViewById(R.id.saveButton).setOnClickListener((view -> {
            viewModel.insertNewMatch(new Match(league.getId(), player1, player2, player1Score, player2Score, new Date())).subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            });
            matchDialog.dismiss();
        }));

        matchDialog.setCanceledOnTouchOutside(true);
        matchDialog.show();
    }

    private void setDialogAdapters(Spinner player1ScoreSpinner,Spinner player2ScoreSpinner,Spinner player1Spinner,Spinner player2Spinner){
        List<String> scoreAdapterList=new ArrayList<>();
        for (int i=0;i<100;i++){
            scoreAdapterList.add(String.valueOf(i));
        }
        ArrayAdapter<String> scoreAdapter=new ArrayAdapter<>(matchDialog.getContext(),
                android.R.layout.simple_spinner_item,scoreAdapterList);
        scoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        player1ScoreSpinner.setAdapter(scoreAdapter);
        player2ScoreSpinner.setAdapter(scoreAdapter);

        List<String> playerList=new ArrayList<>();
        for (Player player:players){
            playerList.add(player.getName());
        }
        ArrayAdapter<String> playerAdapter=new ArrayAdapter<>(matchDialog.getContext(),
                android.R.layout.simple_spinner_item,playerList);
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        player1Spinner.setAdapter(playerAdapter);
        player2Spinner.setAdapter(playerAdapter);
        player2Spinner.setSelection(1);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        leagueMatchesRV.setLayoutManager(linearLayout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(leagueMatchesRV.getContext(), linearLayout.getOrientation());
        leagueMatchesRV.addItemDecoration(dividerItemDecoration);
        leagueMatchesRV.setAdapter(adapter);

        if (league.getStatus()== League.LeagueStatus.ONGOING) {
            ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerMatchesTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(leagueMatchesRV);
        }

        leagueMatchesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (league.getStatus() == League.LeagueStatus.ONGOING) {
                    if (dy > 0)
                        addMatchButton.hide();
                    else
                        addMatchButton.show();
                }
                if (linearLayout.findLastCompletelyVisibleItemPosition()==adapter.getItemCount()-1 && !isLoading && queryLimit==adapter.getItemCount()){
                    isLoading=true;
                    queryLimit+=10;
                    requestMatches();
                }
            }
        });
    }

    private void requestMatches(){
        viewModel.getMatchesByLeagueId(league.getId(),queryLimit).observe(getViewLifecycleOwner(),(matches -> {
            if (isLoading) {
                adapter.setMatches(matches, lastItem);
                if (matches.size() > 10) isLoading = false;
            }else{
                adapter.setMatches(matches,0);
            }
            lastItem = matches.size();
        }));
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof LeagueMatchesRecyclerAdapter.MatchViewHolder){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

            builder.setTitle("Are you sure?");
            builder.setMessage("Do you want to delete?");

            builder.setPositiveButton("YES", (dialog, which) -> {
                Match match=adapter.removeMatch(position);
                Player player1 = null;
                Player player2 = null;
                for (Player player:players){
                    if (player.getId().equals(match.getPlayer1().getId())) player1=player;
                    if (player.getId().equals(match.getPlayer2().getId())) player2=player;
                }
                if (match!=null)
                    viewModel.deleteMatch(match,player1,player2)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        });
                dialog.dismiss();
            });

            builder.setNegativeButton("NO", (dialog, which) -> {
                dialog.dismiss();
                adapter.notifyItemChanged(position);
            });

            builder.setOnDismissListener(dialogInterface -> adapter.notifyDataSetChanged());

            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.md_red_900));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.md_blue_grey_900));
        } }
}
