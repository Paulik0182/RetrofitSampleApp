package com.android.retrofitsampleapp.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.GitUserEntity;

import java.util.List;

public class GitUsersAdapter extends RecyclerView.Adapter<GitUsersViewHolder> {

    private List<GitUserEntity> data;

    public void setData(List<GitUserEntity> users) {
        data = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GitUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GitUsersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_git_project, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GitUsersViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private GitUserEntity getItem(int pos) {
        return data.get(pos);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}