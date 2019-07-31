package com.mrntlu.friendsleaguetable.ui.leaguesactivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leaguecontroller.LeagueControllerOnClick;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.ui.leaguesactivity.leaguecontroller.FragmentLeagueController;
import com.mrntlu.friendsleaguetable.ui.leaguesactivity.leagues.FragmentLeagues;
import com.mrntlu.friendsleaguetable.utils.StatusBarGradient;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class LeaguesActivity extends DaggerAppCompatActivity implements LeagueControllerOnClick {

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            super.onBackPressed();
        });

        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.md_red_900));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.md_blue_grey_900));
    }

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;

    private BottomSheetDialog bottomSheetDialog;
    private League.LeagueStatus leagueStatus= League.LeagueStatus.ONGOING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarGradient.setStatusBarGradiant(this);
        setContentView(R.layout.activity_leagues);
        ButterKnife.bind(this);

        setSupportActionBar(bottomAppBar);
        setBottomSheetDialog();
        startTransaction(new FragmentLeagues(leagueStatus));

        fab.setOnClickListener((view)->{
            int allignment=bottomAppBar.getFabAlignmentMode();
            if (allignment==BottomAppBar.FAB_ALIGNMENT_MODE_CENTER){
                startTransaction(new FragmentLeagueController(this));
                bottomAppBar.setNavigationIcon(null);
                fab.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_black_24dp));
                bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            }else{
                startTransaction(new FragmentLeagues(leagueStatus));
                bottomAppBar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
                invalidateOptionsMenu();
                fab.setImageDrawable(getDrawable(R.drawable.ic_add_white_24dp));
                bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
            }
        });

        bottomAppBar.setNavigationOnClickListener(view -> {
            bottomSheetDialog.show();
        });
    }

    private void setBottomSheetDialog(){
        bottomSheetDialog=new BottomSheetDialog(LeaguesActivity.this);
        View bottomSheetDialogView=getLayoutInflater().inflate(R.layout.dialog_bottom_sheet,null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        View ongoingLeagues=bottomSheetDialogView.findViewById(R.id.ongoingLeagues);
        View finishedLeagues=bottomSheetDialogView.findViewById(R.id.finishedLeagues);

        ongoingLeagues.setOnClickListener((view -> {
            if (leagueStatus== League.LeagueStatus.FINISHED) {
                leagueStatus = League.LeagueStatus.ONGOING;
                startTransaction(new FragmentLeagues(leagueStatus));
            }else{
                Toast.makeText(this, "Already showing Ongoing Leagues.", Toast.LENGTH_SHORT).show();
            }
            bottomSheetDialog.dismiss();
        }));

        finishedLeagues.setOnClickListener((view -> {
            if (leagueStatus== League.LeagueStatus.ONGOING) {
                leagueStatus = League.LeagueStatus.FINISHED;
                startTransaction(new FragmentLeagues(leagueStatus));
            }else{
                Toast.makeText(this, "Already showing Finished Leagues.", Toast.LENGTH_SHORT).show();
            }
            bottomSheetDialog.dismiss();
        }));
    }

    private void startTransaction(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveClick() {
        fab.performClick();
    }
}
