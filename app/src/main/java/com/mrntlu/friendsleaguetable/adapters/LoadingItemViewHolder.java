package com.mrntlu.friendsleaguetable.adapters;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.friendsleaguetable.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    public LoadingItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
