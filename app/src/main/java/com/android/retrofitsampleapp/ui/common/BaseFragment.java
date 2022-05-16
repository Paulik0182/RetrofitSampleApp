package com.android.retrofitsampleapp.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.retrofitsampleapp.App;

/**
 * это базавая активити, для общей логике нескольких активити.
 * пакет common общий пакет для классов с общей логикой для активити.
 * <p>
 * protected доступность только наследникам конкретно только этому классу
 */

//это общий класс от которой все остальные должны наследоватся
public class BaseFragment extends Fragment {

    protected App app;

//    private BaseFragment() {
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = getApp();
    }

    //вариант_2 app
    protected App getApp() {
        return (App) getActivity().getApplication();
    }
}
