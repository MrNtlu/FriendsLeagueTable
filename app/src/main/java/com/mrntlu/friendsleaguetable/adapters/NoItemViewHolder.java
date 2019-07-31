package com.mrntlu.friendsleaguetable.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.friendsleaguetable.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cellNoItem)
    public TextView noItemText;

    public NoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
