package com.lifekau.android.lifekau.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lifekau.android.lifekau.R;
import com.lifekau.android.lifekau.manager.LibraryManager;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.lang.ref.WeakReference;


public class LibraryListActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final int TYPE_READING_ROOM = 0;
    public static final int TYPE_STUDY_ROOM = 1;

    private static final String EXTRA_ROOM_TYPE = "extra_room_type";
    private static final int TOTAL_READING_ROOM_NUM = 5;
    private static final int TOTAL_STUDY_ROOM_NUM = 6;

    private LibraryManager mLibraryManager = LibraryManager.getInstance();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private ProgressBar mProgressBar;
    private int mRoomType;
    private int mSelectedArray;

    public static Intent newIntent(Context context, int roomType) {
        Intent intent = new Intent(context, LibraryListActivity.class);
        intent.putExtra(EXTRA_ROOM_TYPE, roomType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_library_list);
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        mRoomType = intent.getIntExtra(EXTRA_ROOM_TYPE, TYPE_READING_ROOM);
        mSelectedArray =
                mRoomType == TYPE_READING_ROOM ? R.array.library_reading_room_list : R.array.library_study_room_list;
        mRecyclerAdapter = new RecyclerView.Adapter<LibraryListViewHolder>() {
            @Override
            public LibraryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_library, parent, false);
                return new LibraryListViewHolder(view);
            }

            @Override
            public void onBindViewHolder(LibraryListViewHolder holder, int position) {
                holder.bind(position);
            }

            @Override
            public int getItemCount() {
                return getResources().getStringArray(mSelectedArray).length;
            }
        };
        mRecyclerView = (RecyclerView) findViewById(R.id.library_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mProgressBar = findViewById(R.id.library_list_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        LibraryManagerAsyncTask libraryManagerAsyncTask = new LibraryManagerAsyncTask(getApplication(), this);
        libraryManagerAsyncTask.execute();
    }

    public class LibraryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mDetailTextView;
        private View mItemView;

        public LibraryListViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mNameTextView = itemView.findViewById(R.id.list_item_library_item_name);
            mDetailTextView = itemView.findViewById(R.id.list_item_library_item_detail);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mItemView.setBackgroundColor(Color.WHITE);
            if (mRoomType == TYPE_READING_ROOM) {
                mNameTextView.setText(mLibraryManager.getReadingRoomName(position));
                int cntAvailableSeat = mLibraryManager.getReadingRoomAvailableSeat(position);
                int cntTotalSeat = mLibraryManager.getReadingRoomTotalSeat(position);
                String statusString;
                String format = getString(R.string.library_reading_room_detail_format);
                statusString = String.format(format, cntTotalSeat - cntAvailableSeat, cntAvailableSeat);
//                mItemView.setBackgroundColor(Color.WHITE);
                if (cntAvailableSeat == 0) {
                    mItemView.setBackgroundColor(changeAlpha(Color.RED, 0.15f));
                    mDetailTextView.setTextColor(getResources().getColor(R.color.room_not_available_color));
                }
//                else if(cntAvailableSeat < cntTotalSeat / 2){
//                    mDetailTextView.setTextColor(getResources().getColor(R.color.orange));
//                }
                else {
//                    mItemView.setBackgroundColor(changeAlpha(Color.GREEN, 0.15f));
                    mDetailTextView.setTextColor(getResources().getColor(R.color.room_available_color));
                }
                mDetailTextView.setText(statusString);
            } else if (mRoomType == TYPE_STUDY_ROOM) {
                mNameTextView.setText(getResources().getStringArray(R.array.library_study_room_list)[position]);
                boolean isAvailableNow = mLibraryManager.isStudyRoomAvailableNow(position);
                Log.e("ERROR", "position" + position + " " + "이용 가능 여부 " + isAvailableNow);
                String format = getString(R.string.library_study_room_detail_format);
                if (!isAvailableNow) {
                    mDetailTextView.setText(String.format(format, getString(R.string.not_available_now)));
                    mItemView.setBackgroundColor(changeAlpha(Color.RED, 0.15f));
                    mDetailTextView.setTextColor(getResources().getColor(R.color.room_not_available_color));
                } else {
                    mDetailTextView.setText(String.format(format, getString(R.string.available_now)));
                    mItemView.setBackgroundColor(Color.WHITE);
                    mDetailTextView.setTextColor(getResources().getColor(R.color.room_available_color));
                }
            }
        }

        private int changeAlpha(int color, float percent) {
            return ColorUtils.setAlphaComponent(color, (int) (255 * percent));
        }

        @Override
        public void onClick(View view) {
            if (mRoomType == TYPE_READING_ROOM) {
                Intent intent = ReadingRoomDetailActivity.newIntent(view.getContext());
                intent.putExtra(EXTRA_ROOM_TYPE, getAdapterPosition() + 1);
                startActivity(intent);
            } else mLibraryManager.showStudyRoomStatus(view.getContext(), getAdapterPosition());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private static class LibraryManagerAsyncTask extends AsyncTask<Void, Void, Integer> {

        private WeakReference<LibraryListActivity> activityReference;
        private WeakReference<Application> applicationWeakReference;

        // only retain a weak reference to the activity
        LibraryManagerAsyncTask(Application application, LibraryListActivity libraryListActivity) {
            applicationWeakReference = new WeakReference<>(application);
            activityReference = new WeakReference<>(libraryListActivity);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            LibraryListActivity libraryListActivity = activityReference.get();
            Resources resources = applicationWeakReference.get().getResources();
            if (libraryListActivity == null || libraryListActivity.isFinishing())
                return resources.getInteger(R.integer.unexpected_error);
            LibraryManager lm = libraryListActivity.mLibraryManager;
            int result;
            int count = 0;
            for (int i = 0; i < TOTAL_STUDY_ROOM_NUM; i++) {
                while (!isCancelled() && (result = lm.pullStudyRoomDetailStatus(applicationWeakReference.get(), i)) != resources.getInteger(R.integer.no_error)) {
                    if (result == resources.getInteger(R.integer.network_error)) {
                        count++;
                    } else return result;
                    if (count == resources.getInteger(R.integer.maximum_retry_num))
                        return resources.getInteger(R.integer.network_error);
                }
            }
            while (!isCancelled() && (result = lm.pullReadingRoomStatus(applicationWeakReference.get())) != resources.getInteger(R.integer.no_error)) {
                if (result == resources.getInteger(R.integer.network_error)) {
                    count++;
                } else return result;
                if (count == resources.getInteger(R.integer.maximum_retry_num))
                    return resources.getInteger(R.integer.network_error);
            }
            return resources.getInteger(R.integer.no_error);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            LibraryListActivity libraryListActivity = activityReference.get();
            Resources resources = applicationWeakReference.get().getResources();
            if (libraryListActivity == null || libraryListActivity.isFinishing()) return;
            if (result == resources.getInteger(R.integer.no_error)) {
                libraryListActivity.mRecyclerAdapter.notifyDataSetChanged();
            } else if (result == resources.getInteger(R.integer.network_error)) {
                libraryListActivity.showErrorMessage();
            }
            ProgressBar progressBar = libraryListActivity.findViewById(R.id.library_list_progress_bar);
            RecyclerView recyclerView = libraryListActivity.findViewById(R.id.library_list_recycler_view);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void showErrorMessage() {
        Toast toast = Toast.makeText(this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
