package com.lifekau.android.lifekau;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ReviewFragment extends PagerFragment implements View.OnClickListener{
    private FrameLayout mGoToFoodReviewButton;
    private FrameLayout mGoToLectureReviewButton;

    public static ReviewFragment newInstance(){
        ReviewFragment fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public void findFragmentContainer(ViewGroup viewGroup) {
        mFragmentContainer = viewGroup.findViewById(R.id.fragment_review_container);
    }

    @Override
    public void refresh() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_review, container, false);
        mGoToFoodReviewButton = view.findViewById(R.id.go_to_food_review_button);
        mGoToFoodReviewButton.setOnClickListener(this);
        mGoToLectureReviewButton = view.findViewById(R.id.go_to_lecture_review_button);
        mGoToLectureReviewButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.go_to_food_review_button:
                intent = FoodReviewCornerListActivity.newIntent(getActivity());
                startActivity(intent);
                break;
            case R.id.go_to_lecture_review_button:
                intent = LectureReviewSearchActivity.newIntent(getActivity());
                startActivity(intent);
                break;
        }
    }
}