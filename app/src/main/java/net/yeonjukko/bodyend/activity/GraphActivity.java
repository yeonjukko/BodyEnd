package net.yeonjukko.bodyend.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.ExerciseAttendanceInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

/**
 * Created by MoonJongRak on 2016. 4. 30..
 */
public class GraphActivity extends AppCompatActivity {
    private BarChart mBarChart;
    private DBmanager mDBmanager;
    private DayCounter dayCounter;
    private float startWeight, goalWeight, maxWeight, minWeight, currWeight;
    private ArrayList<UserRecordModel> data;
    private ArrayList<ExerciseAttendanceInfoModel> attendData;
    private ArrayList<String> dataDate;
    private ArrayList<String> layoutDate;
    private ArrayList<Integer> attendDate;
    private ArrayList<Integer> layoutBottle;

    private ArrayList<BarEntry> dataEntries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mDBmanager = new DBmanager(getContext());
        dayCounter = new DayCounter();


        startWeight = 0;
        goalWeight = mDBmanager.selectUserInfoDB().getUserGoalWeight();
        maxWeight = 0;
        minWeight = goalWeight;
        currWeight = 0;

        data = mDBmanager.selectGraphData();
        dataDate = new ArrayList<>();
        layoutDate = new ArrayList<>();
        dataEntries = new ArrayList<>();
        layoutBottle = new ArrayList<>();
        attendDate = new ArrayList<>();

        mBarChart = (BarChart) findViewById(R.id.barChart);

        if (data.size() > 0) {
            startWeight = data.get(0).getWeightRecord();
            currWeight = data.get(data.size() - 1).getWeightRecord();
        }

        for (int i = 0; i < data.size(); i++) {
            UserRecordModel model = data.get(i);
            dataEntries.add(new BarEntry(model.getWeightRecord(), i));
            dataDate.add(dayCounter.convertDate2String2(model.getRecordDate()));
            attendDate.add(model.getRecordDate());
            layoutDate.add(dayCounter.convertDate2String(model.getRecordDate()));
            layoutBottle.add(model.getWaterRecord());


            if (maxWeight < model.getWeightRecord()) {
                maxWeight = model.getWeightRecord();
            }
            if (minWeight > model.getWeightRecord()) {
                minWeight = model.getWeightRecord();
            }
        }


        setLayout();

        BarData t = new BarData(dataDate, new BarDataSet(dataEntries, "몸무게"));
        t.setValueFormatter(new MyValueFormatter());


        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(2);

        LimitLine goalLine = new LimitLine(goalWeight, "목표체중");
        goalLine.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        goalLine.setLineColor(getResources().getColor(R.color.material_blue_A700));

        LimitLine startLine = new LimitLine(startWeight, "시작체중");
        startLine.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(10, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(minWeight - 2); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaxValue(maxWeight + 2);
        leftAxis.addLimitLine(goalLine);
        leftAxis.addLimitLine(startLine);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setLabelCount(0, false);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(minWeight - 2); // this replaces setStartAtZero(true)
        rightAxis.setAxisMaxValue(maxWeight + 2);
        rightAxis.setDrawGridLines(false);


        mBarChart.setScaleYEnabled(false);
        mBarChart.setDescription(getString(R.string.app_name));
        mBarChart.setData(t);


    }

    private void setLayout() {
        //날짜 레이아웃
        TextView tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        TextView tvLastDate = (TextView) findViewById(R.id.tv_last_date);

        tvStartDate.setText(layoutDate.get(0));
        tvLastDate.setText(layoutDate.get(data.size() - 1));

        //목표 진행률 레이아웃
        TextView tvPercent = (TextView) findViewById(R.id.tv_percent);

        int rate;
        if (currWeight <= goalWeight) {
            rate = (int) (1 + (goalWeight - currWeight) / (startWeight - goalWeight)) * 100;
        } else {
            rate = (int) (1 - (startWeight - currWeight) / (startWeight - goalWeight)) * 100;
        }
        tvPercent.setText(rate + "%");
        tvPercent.setTextColor(getResources().getColor(R.color.material_red_A700));

        //변화된 무게 레이아웃
        TextView tvChanged = (TextView) findViewById(R.id.tv_changed);
        float changed = startWeight - currWeight;
        if (changed > 0) {
            tvChanged.setText("-" + String.format("%.1f",changed) + "kg");
            tvChanged.setTextColor(getResources().getColor(R.color.caldroid_holo_blue_dark));
        } else if (changed == 0) {
            tvChanged.setText("-");
        } else if (changed < 0) {
            tvChanged.setText("+" + String.format("%.1f",changed) + "kg");
            tvChanged.setTextColor(getResources().getColor(R.color.material_red_A700));
        }

        //평균 물 섭취량
        TextView tvBottles = (TextView) findViewById(R.id.tv_bottles);
        int sum = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += layoutBottle.get(i);
        }
        tvBottles.setText(sum / data.size() + "");

        //출석체크
        TextView tvAttend = (TextView) findViewById(R.id.tv_attend);
        TextView tvTotal = (TextView) findViewById(R.id.tv_total);

        int attend = 0;
        for (int i = 0; i < attendDate.size(); i++) {
            if (mDBmanager.selectExerciseAttendance(attendDate.get(i)) != null) {
                attend++;
            }
        }
        tvAttend.setText(attend+"");
        tvTotal.setText(data.size()+"");

        //ib_exit
        ImageButton ibExit = (ImageButton)findViewById(R.id.ib_exit);
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private Context getContext() {
        return this;
    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value); // e.g. append a dollar-sign
        }
    }
}
