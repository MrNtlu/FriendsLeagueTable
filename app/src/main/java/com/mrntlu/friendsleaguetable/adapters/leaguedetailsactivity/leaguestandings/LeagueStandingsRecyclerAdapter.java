package com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguestandings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.LoadingItemViewHolder;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;
import com.mrntlu.friendsleaguetable.models.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeagueStandingsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private League league;
    private List<Player> players;
    private boolean isAdapterSet=false;
    private RequestManager requestManager;

    private final int LEAGUE_HEADER=0,STANDING_TEAM_HOLDER=1,LOADING_HOLDER=2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==LEAGUE_HEADER){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_league_standings_header,parent,false);
            return new LeagueStandingsHeaderViewHolder(view);
        }else if(viewType== LOADING_HOLDER) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_loading_item,parent,false);
            return new LoadingItemViewHolder(view);
        }else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_league_standings,parent,false);
            return new LeagueStandingsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeagueStandingsHeaderViewHolder){

        }else if (holder instanceof LeagueStandingsViewHolder){
            LeagueStandingsViewHolder viewHolder=(LeagueStandingsViewHolder)holder;
            Player player=players.get(position-1);

            viewHolder.positionText.setText(String.valueOf(position));
            requestManager.load(player.getPlayerImage().getImage()).into(viewHolder.playerImage);
            viewHolder.playerText.setText(player.getName());
            viewHolder.winText.setText(String.valueOf(player.getWin()));
            viewHolder.loseText.setText(String.valueOf(player.getLose()));
            viewHolder.drawText.setText(String.valueOf(player.getDraw()));
            viewHolder.scoreForText.setText(String.valueOf(player.getGoalForOrKill()));
            viewHolder.scoreAgainstText.setText(String.valueOf(player.getGoalAgainstOrDeath()));
        }
    }

    @Override
    public int getItemCount() {
        return isAdapterSet?(players.size()+1):1;
    }

    @Override
    public int getItemViewType(int position) {
        return isAdapterSet?(position==0?LEAGUE_HEADER:STANDING_TEAM_HOLDER):LOADING_HOLDER;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public void setLeaguePlayers(League league,List<Player> players) {
        isAdapterSet=true;
        this.league = league;
        this.players=players;
        Collections.sort(players, (player, t1) -> {
            if (player.getWin()==t1.getWin())
                return Integer.compare(t1.getDraw(),player.getDraw());
            else if (player.getWin()==t1.getWin() && player.getDraw()==player.getDraw())
                return Integer.compare((t1.getGoalForOrKill()-t1.getGoalAgainstOrDeath()),(player.getGoalForOrKill()-player.getGoalAgainstOrDeath()));
            else
                return Integer.compare(t1.getWin(),player.getWin());
        });
        notifyDataSetChanged();
    }

    public List<Player> getPlayers() {
        return players;
    }

    class LeagueStandingsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.positionText)
        TextView positionText;

        @BindView(R.id.playerImage)
        CircleImageView playerImage;

        @BindView(R.id.playerText)
        TextView playerText;

        @BindView(R.id.winText)
        TextView winText;

        @BindView(R.id.drawText)
        TextView drawText;

        @BindView(R.id.loseText)
        TextView loseText;

        @BindView(R.id.scoreForText)
        TextView scoreForText;

        @BindView(R.id.scoreAgainstText)
        TextView scoreAgainstText;

        public LeagueStandingsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class LeagueStandingsHeaderViewHolder extends RecyclerView.ViewHolder{

        LeagueStandingsHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
