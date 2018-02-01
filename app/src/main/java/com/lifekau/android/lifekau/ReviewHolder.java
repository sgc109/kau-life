package com.lifekau.android.lifekau;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sgc109 on 2018-02-01.
 */

public class ReviewHolder extends RecyclerView.ViewHolder{
    private RatingBar mRatingBar;
    private TextView mTimeTextView;
    private TextView mCommentTextView;
    protected Context mContext;
    public ReviewHolder(View itemView) {
        super(itemView);
        mRatingBar = (RatingBar) itemView.findViewById(R.id.list_item_food_review_rating_bar);
        mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_time_text_view);
        mCommentTextView = (TextView) itemView.findViewById(R.id.list_item_comment_text_view);
        mContext = itemView.getContext();
    }

    public void bindReview(Review review) {
        mRatingBar.setRating(review.mRating);
        Resources res = mContext.getResources();
        mTimeTextView.setText(getTimeText(review.mDate));
        mCommentTextView.setText(review.mComment);
    }

    protected String getTimeText(long pastTime){
        long diff = new Date().getTime() - pastTime;
        diff /= 1000;
        long hour = diff / 3600;
        long minute = diff / 60;
        String timeText;
        if(minute == 0) {
            timeText = "방금 전";
        } else if(hour == 0){
            timeText = "" + minute + "분 전";
        } else if(hour <= 3){
            timeText = "" + hour + "시간 전";
        } else {
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            timeText = dateFormat.format(pastTime);
        }
        return timeText;
    }
}
