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
    private OnItemClickListener listener;//завели слушатель

    //сохнанили слушатель. после интерфейса идем сюда
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GitUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GitUsersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_git_user, parent, false), listener);// listener - передаем далее во viewHolder
    }

    public void setData(List<GitUserEntity> users) {
        data = users;
        notifyDataSetChanged();
    }

    //интерфейс со слушателем
    public interface OnItemClickListener {
        void onItemClick(GitUserEntity user);
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