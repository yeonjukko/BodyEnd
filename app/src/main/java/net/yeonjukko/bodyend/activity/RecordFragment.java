package net.yeonjukko.bodyend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.Utils;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DividerItemDecoration;
import net.yeonjukko.bodyend.libs.RecordItemModel;
import net.yeonjukko.bodyend.libs.RecordRecyclerViewAdapter;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RecordFragment extends Fragment {
    public DBmanager dBmanager;
    public int showDate;
    View rootView;
    RecordRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_record, container, false);
        dBmanager = new DBmanager(getContext());

        //record 테이블 초기화 및 날짜 설정 메소드

        //정각에만 실행
        insertUserRecord();
        showDate = getToday();

        //캘린더 불러오기
        ImageView btCalendar = (ImageView) rootView.findViewById(R.id.ic_calendar);
        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CalendarActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //CalendarActivity에서 넘어올 때
        Intent intent = getActivity().getIntent();
        if (intent.getIntExtra("showDate", getToday()) != getToday()) {
            showDate = intent.getIntExtra("showDate", getToday());
        }
        setLayout(showDate);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final List<RecordItemModel> data = new ArrayList<>();
        data.add(new RecordItemModel(
                getString(R.string.record_water),
                R.color.Primary,
                R.color.Icons,
                Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        data.add(new RecordItemModel(
                getString(R.string.record_exercise),
                R.color.Primary,
                R.color.Icons,
                Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        data.add(new RecordItemModel(
                getString(R.string.record_weight),
                R.color.Primary,
                R.color.Icons,
                Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        data.add(new RecordItemModel(
                getString(R.string.record_meal),
                R.color.Primary,
                R.color.Icons,
                Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        data.add(new RecordItemModel(
                getString(R.string.record_picture),
                R.color.Primary,
                R.color.Icons,
                Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));

        adapter = new RecordRecyclerViewAdapter(data, showDate, this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private void setLayout(int date) {
        TextView tvCurrDate = (TextView) rootView.findViewById(R.id.tv_curr_date);
        TextView tvCurrDay = (TextView) rootView.findViewById(R.id.tv_curr_day);
        String mDate = date + "";
        String year = mDate.substring(0, 4);
        String month = mDate.substring(4, 6);
        String date2 = mDate.substring(6, 8);
        tvCurrDate.setText(year + "/" + month + "/" + date2);
        tvCurrDate.setTextColor(getResources().getColor(android.R.color.white));
        tvCurrDay.setTextColor(getResources().getColor(android.R.color.white));
        Calendar date3 = Calendar.getInstance();
        date3.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date2));

        switch (date3.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                tvCurrDay.setText("월요일");
                break;
            case Calendar.TUESDAY:
                tvCurrDay.setText("화요일");
                break;
            case Calendar.WEDNESDAY:
                tvCurrDay.setText("수요일");
                break;
            case Calendar.THURSDAY:
                tvCurrDay.setText("목요일");
                break;
            case Calendar.FRIDAY:
                tvCurrDay.setText("금요일");
                break;
            case Calendar.SATURDAY:
                tvCurrDay.setText("토요일");
                break;
            case Calendar.SUNDAY:
                tvCurrDay.setText("일요일");
                break;
        }


    }

    public void insertUserRecord() {
        UserRecordModel model = new UserRecordModel();
        model.setRecordDate(getToday());
        model.setPictureRecord("");
        model.setWeightRecord(dBmanager.selectUserInfoDB().getUserCurrWeight());
        model.setWaterRecord(0);
        model.setWaterVolume((int) (dBmanager.selectUserInfoDB().getUserCurrWeight() * 33 / 300));
        model.setMealBreakfast("");
        model.setMealLunch("");
        model.setMealDinner("");
        model.setMealRefreshments("");
        dBmanager.insertUserRecordDB(model);
    }

    public int getToday() {
        //<--오늘 날짜 구하기 ex)20160317
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        String strMonth = null;
        String strDate = null;


        if (month < 10)
            strMonth = "0" + month;
        else if (month >= 10)
            strMonth = month + "";

        if (date < 10)
            strDate = "0" + date;
        else if (date >= 10)
            strDate = date + "";


        String today = year + strMonth + strDate;
        return Integer.parseInt(today);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RecordRecyclerViewAdapter.REQUEST_CAMERA_CODE) {
            String imagePath = data.getStringExtra(CameraActivity.FLAG_FILE_PATH);
            dBmanager.updatePictureRecord(imagePath, showDate);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            ((RecordRecyclerViewAdapter.ViewHolderPicture)RecordRecyclerViewAdapter.holderTest).imageTodayPic.setImageBitmap(bitmap);
//            ((RecordRecyclerViewAdapter.ViewHolderPicture)RecordRecyclerViewAdapter.holderTest).imageTodayPic.setVisibility(View.VISIBLE);
//           Log.d("mox2",((RecordRecyclerViewAdapter.ViewHolderPicture)RecordRecyclerViewAdapter.holderTest).imageTodayPic.getVisibility()+"visible");
//                   ((RecordRecyclerViewAdapter.ViewHolderPicture) RecordRecyclerViewAdapter.holderTest).expandableLayout.invalidate();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}