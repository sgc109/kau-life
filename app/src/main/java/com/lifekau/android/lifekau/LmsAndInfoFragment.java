package com.lifekau.android.lifekau;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class LmsAndInfoFragment extends PagerFragment {

    public static LmsAndInfoFragment newInstance(){
        LmsAndInfoFragment fragment = new LmsAndInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lms_and_info, container, false);
        findFragmentContainer(view);
        return view;
    }

    @Override
    public void findFragmentContainer(View view) {
        mFragmentContainer = view.findViewById(R.id.fragment_lms_and_info_container);
    }

    @Override
    public void refresh() {

    }
}
