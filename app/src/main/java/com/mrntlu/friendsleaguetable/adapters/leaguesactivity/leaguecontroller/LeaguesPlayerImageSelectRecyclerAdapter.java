package com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leaguecontroller.PlayerImageSelectedCallback;
import com.mrntlu.friendsleaguetable.models.Player;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.mrntlu.friendsleaguetable.models.Player.PlayerImage.*;

public class LeaguesPlayerImageSelectRecyclerAdapter extends RecyclerView.Adapter<LeaguesPlayerImageSelectRecyclerAdapter.ImageSelectViewHolder> {

    private List<Player.PlayerImage> playerImages=new ArrayList<Player.PlayerImage>(){{
        add(FootballField);
        add(Archery);
        add(Collage);
        add(CraneField);
        add(Dices);
        add(Foosball);
        add(Headphone);
        add(Joystick);
        add(Medal);
        add(Pacman);
        add(PingPong);
        add(Podium);
        add(RacingCar);
        add(Snooker);
        add(SteeringWheel);
        add(Swords);
        add(Tower);
        add(Target);
    }};

    private PlayerImageSelectedCallback callback;

    public void setCallback(PlayerImageSelectedCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ImageSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_dialog_player_image,parent,false);
        return new ImageSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSelectViewHolder holder, int position) {
        holder.playerImage.setImageResource(playerImages.get(position).getImage());

        holder.playerImage.setOnClickListener((view -> {
            callback.onSelected(playerImages.get(position));
        }));
    }

    @Override
    public int getItemCount() {
        return playerImages.size();
    }

    class ImageSelectViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.playerImage)
        CircleImageView playerImage;

        ImageSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
