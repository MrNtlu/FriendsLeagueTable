package com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leagues;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.LoadingItemViewHolder;
import com.mrntlu.friendsleaguetable.adapters.NoItemViewHolder;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leagues.LeagueDeleteCallback;
import com.mrntlu.friendsleaguetable.callbacks.leagueactivity.leagues.LeagueSelectedCallback;
import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeaguesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LeaguePlayers> leagues=new ArrayList<>();
    private RequestManager requestManager;
    private boolean isAdapterSet=false;

    private LeagueSelectedCallback selectedCallback;
    private LeagueDeleteCallback leagueDeleteCallback;

    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 500;

    public final int NO_LEAGUES_HOLDER=0,LEAGUES_HOLDER=1,LOADING_HOLDER=2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==LEAGUES_HOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_leagues, parent, false);
            return new LeaguesViewHolder(view);
        }else if(viewType== LOADING_HOLDER) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_loading_item,parent,false);
            return new LoadingItemViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_no_item, parent, false);
            return new NoItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeaguesViewHolder) {
            LeaguesViewHolder viewHolder=(LeaguesViewHolder)holder;
            League league = leagues.get(position).league;

            switch (league.getType()){
                case SPORT:{
                    requestManager.load(R.drawable.ic_sport_balls).into(viewHolder.leagueTypeImage);
                    break;
                }case FPS:{
                    requestManager.load(R.drawable.ic_gun_target).into(viewHolder.leagueTypeImage);
                    break;
                }case MOBA:{
                    requestManager.load(R.drawable.ic_moba).into(viewHolder.leagueTypeImage);
                    break;
                }
            }
            viewHolder.leagueName.setText(league.getName());

            viewHolder.itemView.setOnClickListener(view -> {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (selectedCallback!=null) selectedCallback.onLeagueSelected(leagues.get(position));
            });

            viewHolder.actionButton.setOnClickListener((view -> createPopupMenu(view,viewHolder,position)));
        }else if (holder instanceof NoItemViewHolder){
            NoItemViewHolder viewHolder=(NoItemViewHolder)holder;

            viewHolder.noItemText.setText("No League Found!");
        }
    }

    private void createPopupMenu(View view,LeaguesViewHolder viewHolder, int position){
        PopupMenu popup=new PopupMenu(view.getContext(),viewHolder.actionButton);
        popup.inflate(R.menu.league_item_menu);

        popup.setOnMenuItemClickListener((menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.deleteLeague:{
                    leagueDeleteCallback.onDelete(position);
                    return true;
                }
                default:
                    return false;
            }
        }));

        popup.show();
    }

    @Override
    public int getItemCount() {
        return isAdapterSet?(leagues.size()==0?1:leagues.size()):1;
    }

    @Override
    public int getItemViewType(int position) {
        return isAdapterSet?(leagues.size()==0?NO_LEAGUES_HOLDER:LEAGUES_HOLDER):LOADING_HOLDER;
    }

    public LeaguePlayers removeLeague(int position){
        LeaguePlayers leaguePlayers=leagues.get(position);
        leagues.remove(position);
        notifyItemRemoved(position);
        return leaguePlayers;
    }

    public void setLeagues(List<LeaguePlayers> leagues,int lastItem){
        isAdapterSet=true;
        this.leagues=leagues;
        if (lastItem==0) notifyDataSetChanged();
        else notifyItemRangeInserted(lastItem,leagues.size()-lastItem);
    }

    public void setRequestManager(RequestManager requestManager){
        this.requestManager=requestManager;
    }

    public void setSelectedCallback(LeagueSelectedCallback selectedCallback) {
        this.selectedCallback = selectedCallback;
    }

    public void setLeagueDeleteCallback(LeagueDeleteCallback leagueDeleteCallback) {
        this.leagueDeleteCallback = leagueDeleteCallback;
    }

    public class LeaguesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cellLeagueTypeImage)
        CircleImageView leagueTypeImage;

        @BindView(R.id.cellNoItem)
        TextView leagueName;

        @BindView(R.id.cellActionButton)
        ImageButton actionButton;

        @BindView(R.id.foregroundCell)
        ConstraintLayout foregroundCell;

        LeaguesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
