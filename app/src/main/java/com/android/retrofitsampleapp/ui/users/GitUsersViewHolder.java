package com.android.retrofitsampleapp.ui.users;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.GitUserEntity;

public class GitUsersViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView = itemView.findViewById(R.id.title_text_view);
    private final TextView subtitleTextView = itemView.findViewById(R.id.subtitle_text_view);

    private GitUserEntity user;//звсели user

    public GitUsersViewHolder(@NonNull View itemView, GitUsersAdapter.OnItemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            listener.onItemClick(user);//передали user в слушатель
        });
    }

    public void bind(GitUserEntity gitUserEntity) {
        user = gitUserEntity;//сохранили user в текущей карточке
        titleTextView.setText(gitUserEntity.getLogin());
        subtitleTextView.setText(gitUserEntity.getNodeId());
    }
}
