package com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguematches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.LoadingItemViewHolder;
import com.mrntlu.friendsleaguetable.adapters.NoItemViewHolder;
import com.mrntlu.friendsleaguetable.models.Match;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeagueMatchesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Match> matches=new ArrayList<>();
    private boolean isAdapterSet=false;

    private final int NO_MATCH_HOLDER=0,MATCH_HOLDER=1, LOADING_HOLDER =2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MATCH_HOLDER){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_league_match,parent,false);
            return new MatchViewHolder(view);
        }else if(viewType== LOADING_HOLDER) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_loading_item,parent,false);
            return new LoadingItemViewHolder(view);
        }else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_no_item,parent,false);
            return new NoItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MatchViewHolder){
            MatchViewHolder viewHolder=(MatchViewHolder)holder;
            Match match=matches.get(position);

            viewHolder.player1Name.setText(match.getPlayer1().getName());
            viewHolder.player2Name.setText(match.getPlayer2().getName());
            viewHolder.player1Score.setText(String.valueOf(match.getPlayer1_score()));
            viewHolder.player2Score.setText(String.valueOf(match.getPlayer2_score()));

            viewHolder.player1Image.setImageResource(match.getPlayer1().getPlayerImage().getImage());
            viewHolder.player2Image.setImageResource(match.getPlayer2().getPlayerImage().getImage());

            if (match.getPlayer1_score()>match.getPlayer2_score()){
                viewHolder.winner1Image.setVisibility(View.VISIBLE);
                viewHolder.winner2Image.setVisibility(View.GONE);
            }
            else if (match.getPlayer1_score()<match.getPlayer2_score()){
                viewHolder.winner1Image.setVisibility(View.GONE);
                viewHolder.winner2Image.setVisibility(View.VISIBLE);
            }else{
                viewHolder.winner1Image.setVisibility(View.GONE);
                viewHolder.winner2Image.setVisibility(View.GONE);
            }

        }else if (holder instanceof NoItemViewHolder){
            NoItemViewHolder viewHolder=(NoItemViewHolder)holder;

            viewHolder.noItemText.setText("No match found!");
        }else if (holder instanceof LoadingItemViewHolder){
            LoadingItemViewHolder viewHolder= (LoadingItemViewHolder) holder;
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return isAdapterSet?(matches.size()==0?1:matches.size()):1;
    }

    @Override
    public int getItemViewType(int position) {
        return isAdapterSet?(matches.size()==0?NO_MATCH_HOLDER:MATCH_HOLDER): LOADING_HOLDER;
    }

    public void setMatches(List<Match> matches,int lastItem){
        this.matches=matches;
        isAdapterSet=true;
        if (lastItem==0) notifyDataSetChanged();
        else notifyItemRangeInserted(lastItem,matches.size()-lastItem);

    }

    public Match removeMatch(int position){
        Match match=matches.get(position);
        matches.remove(position);
        notifyItemRemoved(position);
        return match;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.player1Image)
        CircleImageView player1Image;

        @BindView(R.id.player2Image)
        CircleImageView player2Image;

        @BindView(R.id.player1Name)
        TextView player1Name;

        @BindView(R.id.player2Name)
        TextView player2Name;

        @BindView(R.id.player1Score)
        TextView player1Score;

        @BindView(R.id.player2Score)
        TextView player2Score;

        @BindView(R.id.foregroundCell)
        ConstraintLayout foregroundCell;

        @BindView(R.id.winner1Image)
        ImageView winner1Image;

        @BindView(R.id.winner2Image)
        ImageView winner2Image;

        MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
