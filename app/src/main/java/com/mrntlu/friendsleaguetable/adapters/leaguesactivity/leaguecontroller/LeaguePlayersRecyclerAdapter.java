package com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.models.Player;
import com.mrntlu.friendsleaguetable.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeaguePlayersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Player> players=new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_league_player,parent,false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlayerViewHolder) {
            PlayerViewHolder viewHolder=(PlayerViewHolder)holder;
            Player player = players.get(position);

            viewHolder.playerImageView.setImageResource(player.getPlayerImage().getImage());

            viewHolder.playerNameText.setText(player.getName());
            viewHolder.deleteButton.setOnClickListener((view -> {
                deletePlayer(position);
            }));
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    public void addPlayer(Player player){
        int position=players.size();
        players.add(player);
        notifyItemInserted(position);
    }

    private void deletePlayer(int position){
        players.remove(position);
        notifyDataSetChanged();
    }

    public List<Player> getPlayers() {
        return players;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.playerNameText)
        TextView playerNameText;

        @BindView(R.id.deleteButton)
        ImageButton deleteButton;

        @BindView(R.id.playerImageView)
        CircleImageView playerImageView;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
