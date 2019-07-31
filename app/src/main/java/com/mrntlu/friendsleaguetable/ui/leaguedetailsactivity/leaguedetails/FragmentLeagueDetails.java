package com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguedetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller.LeaguePlayersRecyclerAdapter;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.ui.BaseLeagueController;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguehub.FragmentLeagueHub;
import com.mrntlu.friendsleaguetable.utils.CommonUsages;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FragmentLeagueDetails extends BaseLeagueController {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LeaguePlayersRecyclerAdapter adapter;

    @BindView(R.id.leagueNameText)
    AppCompatEditText leagueNameText;

    @BindView(R.id.selectImageButton)
    ImageButton selectImageButton;

    @BindView(R.id.playerNameText)
    AppCompatEditText playerNameText;

    @BindView(R.id.addButton)
    ImageButton addButton;

    @BindView(R.id.playersRV)
    RecyclerView playersRV;

    @BindView(R.id.editButton)
    Button editButton;

    @BindView(R.id.cancelButton)
    Button cancelButton;

    @BindView(R.id.editableView)
    View view;

    @BindView(R.id.viewStateText)
    TextView viewStateText;

    @BindView(R.id.leagueDetailsToolbar)
    Toolbar toolbar;

    private final int VIEW_STATE=0,EDIT_STATE=1;
    private int state=VIEW_STATE;

    private Unbinder unbinder;
    private League league;
    private List<Player> players;
    private List<Player> backupPlayers;

    public FragmentLeagueDetails(League league) {
        this.league=league;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_league_details, container, false);
        unbinder=ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this,viewModelProviderFactory).get(LeaguesViewModel.class);

        CommonUsages.setToolbar(toolbar,"League Details",getFragmentManager());


        setupObserver();
        setListeners();
    }

    private void setupObserver() {
        viewModel.getPlayersAndLeagueByLeagueName(league.getName()).observe(getViewLifecycleOwner(),leaguePlayers -> {
            this.players=leaguePlayers.players;
            backupPlayers=new ArrayList<>(leaguePlayers.players);
            setUI();
            initRecyclerView(playersRV, adapter);
        });
    }

    private void setUI(){
        players=new ArrayList<>(backupPlayers);
        leagueNameText.setText(league.getName());
        adapter.setPlayers(players);
    }

    @Override
    protected void setListeners() {
        editButton.setOnClickListener(view -> {
            stateController();
        });

        addButton.setOnClickListener(view -> {
            addButtonController(playerNameText,selectImageButton,adapter);
        });

        selectImageButton.setOnClickListener(view -> {
            showImageDialog(this);
        });

        cancelButton.setOnClickListener(view -> {
            setUI();
            isEditable(false);
            state=VIEW_STATE;
        });
    }

    @Override
    protected Map<Boolean, String> checkSaveRules() {
        Map<Boolean,String> returnMap=new HashMap<>();

        if (leagueNameText.getText()!=null && leagueNameText.getText().toString().isEmpty()){
            returnMap.put(false,"Please don't leave League Name empty.");
            return returnMap;
        }
        if (adapter.getPlayers().size()<2){
            returnMap.put(false,"Please add at least 2 players.");
            return returnMap;
        }
        returnMap.put(true,"Success!");
        return returnMap;
    }

    @Override
    public void onSelected(Player.PlayerImage playerImage) {
        this.playerImage=playerImage;
        selectImageButton.setImageResource(playerImage.getImage());
        if (selectImageDialog.isShowing()) selectImageDialog.dismiss();
    }

    private void stateController(){
        if (state==VIEW_STATE){
            isEditable(true);
            state=EDIT_STATE;
        }else if (state==EDIT_STATE){
            if ((boolean)checkSaveRules().keySet().toArray()[0]) {
                if (!backupPlayers.equals(adapter.getPlayers()) || !league.getName().equals(String.valueOf(leagueNameText.getText()))) {
                    if (!league.getName().equals(String.valueOf(leagueNameText.getText()))) league.setName(String.valueOf(leagueNameText.getText()));

                    List<Player> tempPlayers=new ArrayList<>(backupPlayers);
                    backupPlayers.removeAll(adapter.getPlayers());
                    adapter.getPlayers().removeAll(tempPlayers);
                    viewModel.updateLeague(league,backupPlayers,adapter.getPlayers())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onComplete() {
                                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "Nothing has changed.", Toast.LENGTH_SHORT).show();
                }
                isEditable(false);
                state = VIEW_STATE;
            }else{
                Toast.makeText(getActivity(), String.valueOf(checkSaveRules().values().toArray()[0]), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void isEditable(boolean isEditable){
        view.setVisibility(isEditable?View.GONE:View.VISIBLE);
        cancelButton.setVisibility(isEditable?View.VISIBLE:View.GONE);
        editButton.setText(isEditable?"Save":"Edit");
        viewStateText.setText(isEditable?"Edit":"View");
        viewStateText.setTextColor(isEditable? ContextCompat.getColor(viewStateText.getContext(),R.color.md_red_700) :ContextCompat.getColor(viewStateText.getContext(),R.color.md_black_1000));
    }

    @Override
    public void onDestroyView() {
        if (unbinder!=null) unbinder.unbind();
        super.onDestroyView();
    }
}
