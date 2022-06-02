package com.android.retrofitsampleapp.ui.projects;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.UsedConst;
import com.android.retrofitsampleapp.domain.GitProjectEntity;
import com.android.retrofitsampleapp.domain.GitUserEntity;
import com.android.retrofitsampleapp.ui.git_common.BaseGitListFragmenty;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsFragment extends BaseGitListFragmenty<GitProjectEntity> {

    private static final String GIT_PROJECT_ENTITY_KEY = "GIT_PROJECT_ENTITY_KEY";
    private static final String LOGIN_EXTRA_KEY = "LOGIN_EXTRA_KEY";

    private final GitProjectAdapter adapter = new GitProjectAdapter();

    private GitProjectEntity gitProjectEntity;
    private ImageView avatarImageView;

    public ProjectsFragment() {
    }

    public static ProjectsFragment newInstance(GitProjectEntity gitProjectEntity) {
        Bundle args = new Bundle();
        args.putParcelable(GIT_PROJECT_ENTITY_KEY, gitProjectEntity);

        ProjectsFragment fragment = new ProjectsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        getActivity().setTitle(getLogin());//подставили имя в заголовок (не понял как. пояснение.)????

        setContractViews(progressBar, recyclerView);
        gitProjectEntity = getArguments().getParcelable(GIT_PROJECT_ENTITY_KEY);

        loadUser(getLogin());
        loadData();
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        avatarImageView = view.findViewById(R.id.avatar_image_view);
    }

    private void loadUser(String login) {
        getGitHubApi().getUser(login).enqueue(new Callback<GitUserEntity>() {
            @Override
            public void onResponse(@NonNull Call<GitUserEntity> call,
                                   @NonNull Response<GitUserEntity> response) {
                final String avatarUrl = response.body().getAvatarUrl();
                Toast.makeText(getContext(), avatarUrl, Toast.LENGTH_SHORT).show();
                setAvatar(avatarUrl);
            }

            @Override
            public void onFailure(@NonNull Call<GitUserEntity> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAvatar(String avatarUrl) {
        Glide
                .with(this)
                .load(avatarUrl)
                .placeholder(UsedConst.imageConst.DEFAULT_IMAGE_CONST)
                .into(avatarImageView);
    }

    @Override
    protected Call<List<GitProjectEntity>> getRetrofitCall() {
        return getGitHubApi().getProject(getLogin());
    }

    @Override
    protected void onSuccess(List<GitProjectEntity> data) {
        adapter.setData(data);
    }

    @Override
    protected void onError(Throwable t) {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private String getLogin() {
        assert getArguments() != null;
        return getArguments().getParcelable(LOGIN_EXTRA_KEY);//получаем логин
    }


    private void showProjectScreen(GitProjectEntity gitProjectEntity) {
        getController().showProjectScreen(gitProjectEntity);
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
        // Он нам позволит свольть приложение раньше чем откроится фрагмент.
        // Приложение свалится если мы забудим в классе RootActivity
        // отнаследоватся от интерфейса (implements NoteListFragment.Controller).
    }

    //сам контроллер. указываем метод через который вызываем фрагмент (фрагмент с деталями  замиси)
    //обязательно нужно в активити имплементировать (наследоватся от) интерфейс
    public interface Controller {
        void showProjectScreen(GitProjectEntity gitProjectEntity);
    }
}
