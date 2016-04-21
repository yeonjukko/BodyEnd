package net.yeonjukko.bodyend.libs;

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
import net.yeonjukko.bodyend.activity.MaterialActivity;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by MoonJongRak on 2016. 3. 12..
 */
public class BodyEndCalendarFragment extends CaldroidFragment {

    private int[] imageViewIds = {R.id.imageViewIcon0, R.id.imageViewIcon1, R.id.imageViewIcon2, R.id.imageViewIcon3};
    private int[] iconImageIds = {android.R.drawable.ic_input_get, android.R.drawable.ic_dialog_email, android.R.drawable.ic_dialog_info, android.R.drawable.ic_dialog_alert};
    private DBmanager dBmanager;

    private HashMap<Integer, UserRecordModel> mRecordItemModels;
    private UserInfoModel mUserInfoModel;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        dBmanager = new DBmanager(getContext());
        mRecordItemModels = dBmanager.selectUserRecordDB();
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

        public CalendarAdapter(Context context, int month, int year,
                               Map<String, Object> caldroidData,
                               Map<String, Object> extraData) {
            super(context, month, year, caldroidData, extraData);

        }

        private int cellViewHeightSix;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final DateTime dateTime = this.datetimeList.get(position);
            final UserRecordModel model = mRecordItemModels.get(getDate(dateTime));

            boolean isWeight = false, isWater = false, isImage = false, isAttendance = false;


            View cellView = convertView;

            if (convertView == null) {
                cellView = ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_calendar, parent, false);
            }

//            if (model != null) {
//                if (model.getWaterRecord() >= model.getWaterVolume()) {
//                    isWater = true;
//                }
//                if (model.getPictureRecord() == null || model.getPictureRecord().equals("")) {
//                    isImage = true;
//                }
//
//            }


            TextView mTextViewDay = (TextView) cellView.findViewById(R.id.textViewDay);


            if (dateTime.getMonth() != month) {
                mTextViewDay.setTextColor(resources
                        .getColor(com.caldroid.R.color.caldroid_darker_gray));
            } else if (dateTime.getWeekDay() == 1) {
                mTextViewDay.setTextColor(Color.RED);
            } else {
                mTextViewDay.setTextColor(Color.BLACK);
            }

            mTextViewDay.setText(Integer.toString(dateTime.getDay()));
            List<Integer> mListImageIds = getIconList(isWeight, isWater, isImage, isAttendance);
            for (int i = 0; i < mListImageIds.size(); i++) {
                ((ImageView) cellView.findViewById(imageViewIds[i])).setImageResource(mListImageIds.get(i));
            }
            for (int i = imageViewIds.length; i > mListImageIds.size(); i--) {
                ((ImageView) cellView.findViewById(imageViewIds[i - 1])).setImageDrawable(null);
            }
            setMatchHeight(cellView);

            cellView.setOnClickListener(new View.OnClickListener() {
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


        private List<Integer> getIconList(boolean isWeight, boolean isWater, boolean isImage, boolean isAttendance) {
            ArrayList<Integer> mListImageIds = new ArrayList<>();

            if (isWeight) {
                mListImageIds.add(iconImageIds[0]);
            }
            if (isWater) {
                mListImageIds.add(iconImageIds[1]);
            }
            if (isImage) {
                mListImageIds.add(iconImageIds[2]);
            }
            if (isAttendance) {
                mListImageIds.add(iconImageIds[3]);
            }

            return mListImageIds;

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
}
