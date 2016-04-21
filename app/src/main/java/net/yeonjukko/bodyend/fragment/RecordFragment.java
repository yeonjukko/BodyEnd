package net.yeonjukko.bodyend.fragment;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.aakira.expandablelayout.Utils;
import com.github.amlcurran.showcaseview.MaterialShowcaseDrawer;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.CalendarActivity;
import net.yeonjukko.bodyend.activity.CameraActivity;
import net.yeonjukko.bodyend.activity.GalleryActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.libs.DividerItemDecoration;
import net.yeonjukko.bodyend.libs.RecordItemModel;
import net.yeonjukko.bodyend.libs.RecordRecyclerViewAdapter;
import net.yeonjukko.bodyend.model.ExerciseSpotInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RecordFragment extends Fragment {
    public DBmanager dBmanager;
    public int showDate;
    public int tmpDate;
    View rootView;
    public RecordRecyclerViewAdapter adapter;
    DayCounter dayCounter;
    List<RecordItemModel> data;
    RecyclerView recyclerView;
    MaterialRippleLayout ivLeft;
    MaterialRippleLayout ivRight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_record, container, false);
        dBmanager = new DBmanager(getContext());
        dayCounter = new DayCounter();

        ivLeft = (MaterialRippleLayout) rootView.findViewById(R.id.image_left);
        ivRight = (MaterialRippleLayout) rootView.findViewById(R.id.image_right);

        //record 테이블 초기화 및 날짜 설정 메소드

        //정각에만 실행
        insertUserRecord();
        showDate = dayCounter.getToday();
        tmpDate = showDate;
        //캘린더 불러오기
        ImageView btCalendar = (ImageView) rootView.findViewById(R.id.ic_calendar);
        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });


        //날짜바꾸기 버튼
        ivLeft.setOnClickListener(changeDate);
        ivRight.setOnClickListener(changeDate);


        //비포 애프터 사진 모음
        ImageView btGallery = (ImageView) rootView.findViewById(R.id.ic_photos);


        //CalendarActivity에서 넘어올 때
        Intent intent = getActivity().getIntent();
        if (intent.getIntExtra("showDate", dayCounter.getToday()) != dayCounter.getToday()) {
            showDate = intent.getIntExtra("showDate", dayCounter.getToday());
        }
        setLayout(showDate);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        data = new ArrayList<>();
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


        rootView.findViewById(R.id.ic_photos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GalleryActivity.class));
            }
        });

        return rootView;
    }


    View.OnClickListener changeDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            try {
                //오늘 날짜
                long dbDate = format.parse(tmpDate + "").getTime();
                UserRecordModel model = null;

                if (v.getParent() == ivLeft) {
                    dbDate -= 60 * 60 * 24 * 1000;
                    int tmp = dayCounter.convertDate(dbDate);
                    model = dBmanager.selectDescUserRecordDB(true, tmp);

                } else if (v.getParent() == ivRight) {
                    dbDate += 60 * 60 * 24 * 1000;
                    int tmp = dayCounter.convertDate(dbDate);
                    model = dBmanager.selectDescUserRecordDB(false, tmp);
                }

                if (model != null) {
                    setLayout(model.getRecordDate());
                    tmpDate = model.getRecordDate();
                    adapter = new RecordRecyclerViewAdapter(data, tmpDate, RecordFragment.this);
                    recyclerView.setAdapter(adapter);

                } else {
                    if (tmpDate != showDate) {
                        Toast.makeText(getContext(), "이전의 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    private void setLayout(int date) {
        if (date == dayCounter.getToday())
            ivRight.setVisibility(View.INVISIBLE);
        else
            ivRight.setVisibility(View.VISIBLE);
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
        model.setRecordDate(dayCounter.getToday());
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.d("mox", resultCode + "");

            if (requestCode == RecordRecyclerViewAdapter.REQUEST_SPOT_NAME) {
                Log.d("mox", "spot" + data.getDoubleExtra("longitude", 0));
                double longitude = data.getDoubleExtra("longitude", 0);
                double latitude = data.getDoubleExtra("latitude", 0);
                String spotName = data.getStringExtra("location");
                ExerciseSpotInfoModel spotInfoModel = new ExerciseSpotInfoModel();
                spotInfoModel.setSpotX(longitude);
                spotInfoModel.setSpotY(latitude);
                spotInfoModel.setSpotName(spotName);
                spotInfoModel.setAttendanceDay(0);
                dBmanager.insertExerciseSpotInfo(spotInfoModel);


            } else if (requestCode == RecordRecyclerViewAdapter.REQUEST_CAMERA_CODE) {

                String imagePath = data.getStringExtra(CameraActivity.FLAG_FILE_PATH);
                dBmanager.updatePictureRecord(imagePath, showDate);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

}