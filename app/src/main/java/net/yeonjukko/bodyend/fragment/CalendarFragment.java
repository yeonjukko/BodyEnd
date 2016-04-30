package net.yeonjukko.bodyend.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.WeekdayArrayAdapter;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.CalendarContentsModel;
import net.yeonjukko.bodyend.model.ExerciseAttendanceInfoModel;
import net.yeonjukko.bodyend.activity.MaterialActivity;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by MoonJongRak on 2016. 3. 12..
 */
public class CalendarFragment extends CaldroidFragment {

    private int[] iconImageIds = {android.R.drawable.ic_input_get, android.R.drawable.ic_dialog_email, android.R.drawable.ic_dialog_info, android.R.drawable.ic_dialog_alert};
    private DBmanager dBmanager;

    private HashMap<Integer, CalendarContentsModel> mCalendarContentsModel;
    private UserInfoModel mUserInfoModel;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        dBmanager = new DBmanager(getContext());
        mCalendarContentsModel = dBmanager.selectCalendarContents();
        mUserInfoModel = dBmanager.selectUserInfoDB();
        setCalendarLayout();
        return rootView;
    }

    private void setCalendarLayout() {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getMonthTitleTextView().getLayoutParams();
        params.bottomMargin = 0;
        params.topMargin = 0;
        LinearLayout titleLayout = (LinearLayout) getMonthTitleTextView().getParent();
        titleLayout.setPadding(0, 0, 0, 0);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.Accent));
        titleLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        titleLayout.setGravity(Gravity.CENTER);
        getMonthTitleTextView().setTextColor(Color.WHITE);
        getMonthTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        getLeftArrowButton().setBackgroundResource(R.drawable.icon_back);
        getRightArrowButton().setBackgroundResource(R.drawable.icon_forward);
        getLeftArrowButton().getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        getRightArrowButton().getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());

        ((LinearLayout.LayoutParams) getWeekdayGridView().getLayoutParams()).topMargin = 0;
        getWeekdayGridView().setBackgroundResource(R.color.Divider);
        getWeekdayGridView().setPadding(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -3, getResources().getDisplayMetrics()), 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -2, getResources().getDisplayMetrics()));

    }

    @Override
    public WeekdayArrayAdapter getNewWeekdayAdapter(int themeResource) {
        return new WeekAdapter(
                getActivity(), android.R.layout.simple_list_item_1,
                getDaysOfWeek(), themeResource);
    }

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CalendarAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }


    private class CalendarAdapter extends CaldroidGridAdapter {
        private int today;

        public CalendarAdapter(Context context, int month, int year,
                               Map<String, Object> caldroidData,
                               Map<String, Object> extraData) {
            super(context, month, year, caldroidData, extraData);
            this.today = new DayCounter().getToday();

        }

        private int cellViewHeightSix;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final DateTime dateTime = this.datetimeList.get(position);
            final int parseDate = getDate(dateTime);
            final CalendarContentsModel model = mCalendarContentsModel.get(parseDate);

            View cellView = convertView;

            if (convertView == null) {
                cellView = ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_calendar, parent, false);
            }


            setContents(cellView, model);


            TextView mTextViewDay = (TextView) cellView.findViewById(R.id.textViewDay);


            if (dateTime.getMonth() != month) {
                mTextViewDay.setTextColor(resources
                        .getColor(com.caldroid.R.color.caldroid_darker_gray));
            } else if (dateTime.getWeekDay() == 1) {
                mTextViewDay.setTextColor(Color.RED);
            } else {
                mTextViewDay.setTextColor(Color.BLACK);
            }

            cellView.findViewById(R.id.textViewDDay).setVisibility(View.GONE);

            if (parseDate == today) {
                cellView.findViewById(R.id.layoutCalendarCellMain).setBackgroundResource(R.drawable.background_calendar_today);
            } else if (parseDate == mUserInfoModel.getGoalDate()) {
                cellView.findViewById(R.id.layoutCalendarCellMain).setBackgroundResource(R.color.Primary);
                cellView.findViewById(R.id.textViewDDay).setVisibility(View.VISIBLE);
            } else {
                cellView.findViewById(R.id.layoutCalendarCellMain).setBackgroundResource(R.drawable.background_calendar_nomal);
            }

            mTextViewDay.setText(Integer.toString(dateTime.getDay()));
            setMatchHeight(cellView);

            cellView.findViewById(R.id.layoutCalendarCellMain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model != null) {
                        Intent intent = new Intent(getContext(), MaterialActivity.class);
                        intent.putExtra("showDate", getDate(dateTime));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "해당 날짜에 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return cellView;
        }

        private void setContents(View cellView, CalendarContentsModel mCalendarContentsModel) {
            ViewGroup mLayoutContents = (ViewGroup) cellView.findViewById(R.id.layoutContents);
            mLayoutContents.removeAllViews();
            cellView.findViewById(R.id.imageViewAttendance).setVisibility(View.GONE);

            if (mCalendarContentsModel == null) {
                return;
            }


            if (mCalendarContentsModel.getWaterCount() != 0)
                mLayoutContents.addView(new CalendarContentsView(getContext(), mCalendarContentsModel.getWaterCount() + "", CalendarContentsView.FLAG_WATER));
            if (mCalendarContentsModel.getMyExerciseCount() != 0)
                mLayoutContents.addView(new CalendarContentsView(getContext(), mCalendarContentsModel.getMyExerciseCount() + "", CalendarContentsView.FLAG_MY_EXERCISE));
            if (mCalendarContentsModel.getYoutubeExerciseCount() != 0)
                mLayoutContents.addView(new CalendarContentsView(getContext(), mCalendarContentsModel.getYoutubeExerciseCount() + "", CalendarContentsView.FLAG_YOUTUBE_EXERCISE));
            if (mCalendarContentsModel.isAttendance()) {
                cellView.findViewById(R.id.imageViewAttendance).setVisibility(View.VISIBLE);
            }

        }


        private void setMatchHeight(View cellView) {
            if (cellViewHeightSix == 0 && ((View) getMonthTitleTextView().getParent()).getHeight() != 0 && getWeekdayGridView().getHeight() != 0) {
                float rootViewHeight = rootView.getHeight();
                float titleViewHeight = ((View) getMonthTitleTextView().getParent()).getHeight() + getWeekdayGridView().getHeight();
                float cellViewHeight = rootViewHeight - titleViewHeight;
                cellViewHeightSix = (int) Math.ceil(cellViewHeight / 6);
            }

            if (cellViewHeightSix == 0) {
                cellView.getLayoutParams().height = 500;
            } else {
                cellView.getLayoutParams().height = cellViewHeightSix;
            }
        }

        private int getDate(DateTime time) {
            String year = time.getYear() + "";
            String month;
            if (time.getMonth() >= 10) {
                month = time.getMonth() + "";
            } else {
                month = "0" + time.getMonth();
            }

            String day;
            if (time.getDay() >= 10) {
                day = time.getDay() + "";
            } else {
                day = "0" + time.getDay();
            }

            return Integer.parseInt(year + month + day);

        }


    }

    private class WeekAdapter extends WeekdayArrayAdapter {


        public WeekAdapter(Context context, int textViewResourceId, List<String> objects, int themeResource) {
            super(context, textViewResourceId, objects, themeResource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) super.getView(position, convertView, parent);
            if (v.getText().toString().contains("일")) {
                v.setTextColor(Color.RED);
            } else {
                v.setTextColor(Color.WHITE);

            }
            return v;
        }
    }

    private class CalendarContentsView extends LinearLayout {

        private final static int FLAG_WATER = 0;
        private final static int FLAG_ATTENDANCE = 1;
        private final static int FLAG_YOUTUBE_EXERCISE = 2;
        private final static int FLAG_MY_EXERCISE = 3;

        private Context context;
        private String contents;
        private int flag;

        public CalendarContentsView(Context context, String contents, int flag) {
            super(context);
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);
            this.contents = contents;
            this.context = context;
            this.flag = flag;
            setLayout();
        }

        private void setLayout() {
            View split = new View(context);
            LinearLayout.LayoutParams params = new LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()), ViewGroup.LayoutParams.MATCH_PARENT);
            split.setLayoutParams(params);
            ImageView mImageViewSort = new ImageView(getContext());
            mImageViewSort.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())));

            switch (flag) {
                case FLAG_WATER:
                    split.setBackgroundResource(R.color.WaterSplit);
                    mImageViewSort.setImageResource(R.drawable.icon_bottle_10);
                    break;
                case FLAG_ATTENDANCE:
                    split.setBackgroundResource(R.color.AttendanceSplit);
                    break;
                case FLAG_YOUTUBE_EXERCISE:
                    split.setBackgroundResource(R.color.YoutubeSplit);
                    mImageViewSort.setImageResource(R.drawable.icon_youtube);
                    break;
                case FLAG_MY_EXERCISE:
                    split.setBackgroundResource(R.color.ExerciseSplit);
                    mImageViewSort.setImageResource(R.drawable.icon_exercise);
                    break;
            }
            addView(split);
            addView(mImageViewSort);
            TextView mTextViewContents = new TextView(getContext());
            mTextViewContents.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            mTextViewContents.setText(" " + contents);
            addView(mTextViewContents);
        }

    }
}
