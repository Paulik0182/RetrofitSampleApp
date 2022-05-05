package com.android.retrofitsampleapp.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;

public class GitUsersViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView = itemView.findViewById(R.id.title_text_view);
    private final TextView subtitleTextView = itemView.findViewById(R.id.subtitle_text_view);

    public GitUsersViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(GitUsersAdapter gitUsersAdapter) {
        titleTextView.setText(gitUsersAdapter.getLogin());
        subtitleTextView.setText(gitUsersAdapter.getNode_id());
    }
}
