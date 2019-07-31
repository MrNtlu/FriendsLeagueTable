package com.mrntlu.friendsleaguetable.ui.leaguesactivity.leaguecontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller.LeaguePlayersRecyclerAdapter;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leaguecontroller.LeagueControllerOnClick;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.ui.BaseLeagueController;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FragmentLeagueController extends BaseLeagueController {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LeaguePlayersRecyclerAdapter adapter;

    @BindView(R.id.leagueNameText)
    AppCompatEditText leagueNameText;

    @BindView(R.id.selectImageButton)
    ImageButton selectImageButton;

    @BindView(R.id.leagueTypeSpinner)
    Spinner leagueTypeSpinner;

    @BindView(R.id.playerNameText)
    AppCompatEditText playerNameText;

    @BindView(R.id.addButton)
    ImageButton addButton;

    @BindView(R.id.playersRV)
    RecyclerView playersRV;

    private LeagueControllerOnClick onClick;
    private League.LeagueType leagueType;

    public FragmentLeagueController(LeagueControllerOnClick leagueControllerOnClick){
        this.onClick=leagueControllerOnClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_league_controller, container, false);
        ButterKnife.bind(this,v);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this,viewModelProviderFactory).get(LeaguesViewModel.class);

        initRecyclerView(playersRV,adapter);
        setListeners();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.league_controller_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveNewLeague) {
            if ((boolean) checkSaveRules().keySet().toArray()[0]) {
                League league = new League(String.valueOf(leagueNameText.getText()), leagueType, League.LeagueStatus.ONGOING, new Date());
                createNewLeague(league, adapter.getPlayers());
            } else {
                Toast.makeText(getActivity(), String.valueOf(checkSaveRules().values().toArray()[0]), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Map<Boolean, String> checkSaveRules() {
        Map<Boolean,String> returnMap=new HashMap<>();

        if (leagueNameText.getText()!=null && leagueNameText.getText().toString().isEmpty()){
            returnMap.put(false,"Please don't leave League Name empty.");
            return returnMap;
        }
        if (leagueType==null){
            returnMap.put(false,"Please select League Type");
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
    protected void setListeners() {
        leagueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0){
                    leagueType= League.LeagueType.valueOf((String)adapterView.getItemAtPosition(i));
                }if (leagueType!=null && i==0) leagueTypeSpinner.setSelection(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        addButton.setOnClickListener((view1)->{
            addButtonController(playerNameText,selectImageButton,adapter);
        });

        selectImageButton.setOnClickListener((view1 -> showImageDialog(this)));
    }

    private void createNewLeague(League league, List<Player> players){
        viewModel.insertNewLeague(league, players)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {
                        setHasOptionsMenu(false);
                        onClick.onSaveClick();
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    @Override
    public void onSelected(Player.PlayerImage playerImage) {
        this.playerImage=playerImage;
        selectImageButton.setImageResource(playerImage.getImage());
        if (selectImageDialog.isShowing()) selectImageDialog.dismiss();
    }
}
