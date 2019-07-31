package com.mrntlu.friendsleaguetable.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller.LeaguePlayersRecyclerAdapter;
import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller.LeaguesPlayerImageSelectRecyclerAdapter;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leaguecontroller.PlayerImageSelectedCallback;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;

import java.util.Map;

import dagger.android.support.DaggerFragment;

public abstract class BaseLeagueController extends DaggerFragment implements PlayerImageSelectedCallback {

    protected LeaguesViewModel viewModel;
    protected Dialog selectImageDialog;
    protected Player.PlayerImage playerImage;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectImageDialog=new Dialog(view.getContext());
    }

    protected abstract void setListeners();

    protected abstract Map<Boolean,String> checkSaveRules();

    protected void initRecyclerView(RecyclerView playersRV,LeaguePlayersRecyclerAdapter adapter){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        playersRV.setLayoutManager(linearLayoutManager);
        playersRV.setAdapter(adapter);
    }

    protected void showImageDialog(PlayerImageSelectedCallback callback){
        selectImageDialog.setContentView(R.layout.dialog_select_player_image);
        if (selectImageDialog.getWindow()!=null) selectImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView imagesRV=selectImageDialog.findViewById(R.id.imagesRV);
        LeaguesPlayerImageSelectRecyclerAdapter adapter=new LeaguesPlayerImageSelectRecyclerAdapter();
        adapter.setCallback(callback);
        imagesRV.setLayoutManager(new GridLayoutManager(getContext(),3));
        imagesRV.setAdapter(adapter);

        selectImageDialog.setCanceledOnTouchOutside(true);
        selectImageDialog.show();
    }

    protected void addButtonController(AppCompatEditText playerNameText, ImageButton selectImageButton,LeaguePlayersRecyclerAdapter adapter){
        if (playerNameText.getText()!=null && !playerNameText.getText().toString().isEmpty() && playerImage!=null) {
            adapter.addPlayer(new Player(playerNameText.getText().toString(),playerImage));
            playerNameText.setText("");
            playerImage=null;
            selectImageButton.setImageResource(R.drawable.ic_photo_select);
        }else{
            String errorMessage;
            if (playerNameText.getText()!=null && !playerNameText.getText().toString().isEmpty()) errorMessage="Player name shouldn't be empty.";
            else if (playerImage!=null) errorMessage="Please select Player image.";
            else errorMessage="Error! Please check again.";
            Toast.makeText(getContext(),errorMessage , Toast.LENGTH_SHORT).show();
        }
    }
}
