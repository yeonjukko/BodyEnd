package net.yeonjukko.bodyend.libs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.MainActivity;
import net.yeonjukko.bodyend.activity.RecordFragment;
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

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        dBmanager = new DBmanager(getContext());
        mRecordItemModels = dBmanager.selectUserRecordDB();
        return rootView;
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

            if (model != null) {
                if (model.getWaterRecord() >= model.getWaterVolume()) {
                    isWater = true;
                }
                if (model.getPictureRecord() == null || model.getPictureRecord().equals("")) {
                    isImage = true;
                }

            }


            TextView mTextViewDay = (TextView) cellView.findViewById(R.id.textViewDay);


            if (dateTime.getMonth() != month) {
                mTextViewDay.setTextColor(resources
                        .getColor(com.caldroid.R.color.caldroid_darker_gray));
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
                        Intent intent = new Intent(getContext(), MainActivity.class);
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
                cellViewHeightSix = (int) (Math.ceil(cellViewHeight / 6) + 10);

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
}
