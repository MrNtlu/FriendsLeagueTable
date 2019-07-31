package com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.ui.leaguedetailsactivity.leaguehub.FragmentLeagueHub;
import com.mrntlu.friendsleaguetable.utils.StatusBarGradient;

import java.util.ArrayList;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class LeagueDetailsActivity extends DaggerAppCompatActivity {

    @Override
    public void onBackPressed() {
        FragmentManager fm=getSupportFragmentManager();
        if (fm.getBackStackEntryCount()>0){
          fm.popBackStack();
        } else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarGradient.setStatusBarGradiant(this);
        setContentView(R.layout.activity_league_details);
        ButterKnife.bind(this);

        League league = this.getIntent().getParcelableExtra("league");
        ArrayList<Player> players = this.getIntent().getParcelableArrayListExtra("players");

        FragmentLeagueHub fragmentLeagueHub=new FragmentLeagueHub(league,players);
        startTransaction(fragmentLeagueHub);
    }

    private void startTransaction(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}
