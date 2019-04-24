package com.bmusic.bmusicworld.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends BaseFragment {


    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.offlineplayer, container, false);
    }

    @Override
    protected String getTitle() {
        return "MenuFragment";
    }
}
