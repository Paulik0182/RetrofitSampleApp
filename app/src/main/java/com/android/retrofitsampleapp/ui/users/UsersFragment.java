package com.android.retrofitsampleapp.ui.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.GitUserEntity;
import com.android.retrofitsampleapp.ui.git_common.BaseGitListFragmenty;
import com.android.retrofitsampleapp.ui.projects.ProjectsFragment;

import java.util.List;

import retrofit2.Call;

public class UsersFragment extends BaseGitListFragmenty<GitUserEntity> {

    private final GitUsersAdapter adapter = new GitUsersAdapter();

    private final GitUsersAdapter.OnItemClickListener listener = new GitUsersAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(GitUserEntity user) {
            showUserScreen(user);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setContractViews(progressBar, recyclerView);

//        adapter.setOnItemClickListener(this::openUserScreen);// ::это ссылка на один метод,
//        // а :: означает, что этот метод использовать как лямду чтобы передать его в адаптер
//        // и приобразовать его в OnItemClickListener (это синтаксический сахр)

        loadData();
    }


    @Override
    protected Call<List<GitUserEntity>> getRetrofitCall() {
        return getGitHubApi().getUsers();
    }

    @Override
    protected void onSuccess(List<GitUserEntity> data) {
        adapter.setData(data);
    }

    @Override
    protected void onError(Throwable t) {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void openUserScreen(GitUserEntity user) {
        Intent intent = ProjectsFragment.getLaunchIntent(this, user.getLogin());
        startActivity(intent);
        Toast.makeText(getContext(), "Нажали " + user.getLogin(), Toast.LENGTH_SHORT).show();
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
    }


    //метод для открытия отдельной заметки
    private void showUserScreen(GitUserEntity gitUserEntity) {
        getController().showUserScreen(gitUserEntity);
        //для того чтобы фрагмент знал что-то об активети,
        // лучше всего сделать связь (взаимодействие) через интерфейс,
        // в этом случае фрагмент напрямую не будет обращатся к активити,
        // все взаимодействие будет через интерфейс
    }

    //взаимодействие активити и фрагмента через контроллер
    private Controller getController() {
        return (Controller) getActivity();
    }

    //это метод сработывает в момент присоединения фрагмента к активити
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getController();

        //этот метод с вызовом метода getController() своего рода костыль.
        // Он нам позволит свальть приложение раньше чем откроится фрагмент.
        // Приложение свалится если мы забудим в классе RootActivity
        // отнаследоватся от интерфейса (implements UsersFragment.Controller).
    }

    //сам контроллер. указываем метод через который вызываем фрагмент (фрагмент с деталями  замиси)
    //обязательно нужно в активити имплементировать (наследоватся от) интерфейс
    interface Controller {
        void showUserScreen(GitUserEntity gitUserEntity);
    }
}