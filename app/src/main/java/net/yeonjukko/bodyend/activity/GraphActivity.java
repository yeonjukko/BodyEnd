package net.yeonjukko.bodyend.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by MoonJongRak on 2016. 4. 30..
 */
public class GraphActivity extends AppCompatActivity {
    private BarChart mBarChart;
    private DBmanager mDBmanager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mDBmanager = new DBmanager(getContext());

        float startWeight = 0, goalWeight = mDBmanager.selectUserInfoDB().getUserGoalWeight(), maxWeight = 0, minWeight = goalWeight;

        ArrayList<UserRecordModel> data = mDBmanager.selectGraphData();

        ArrayList<String> dataDate = new ArrayList<>();
        ArrayList<BarEntry> dataEntries = new ArrayList<>();

        mBarChart = (BarChart) findViewById(R.id.barChart);

        if (data.size() > 0) {
            startWeight = data.get(0).getWeightRecord();
        }

        for (int i = 0; i < data.size(); i++) {
            UserRecordModel model = data.get(i);
            dataEntries.add(new BarEntry(model.getWeightRecord(), i));
            dataDate.add(model.getRecordDate() + "");

            if (maxWeight < model.getWeightRecord()) {
                maxWeight = model.getWeightRecord();
            }
            if (minWeight > model.getWeightRecord()) {
                minWeight = model.getWeightRecord();
            }

        }

        BarData t = new BarData(dataDate, new BarDataSet(dataEntries, "몸무게"));
        t.setValueFormatter(new MyValueFormatter());
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(2);

        LimitLine goalLine = new LimitLine(goalWeight, "목표체중");
        goalLine.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        LimitLine startLine = new LimitLine(startWeight, "시작체중");
        startLine.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(10, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(minWeight - 2); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaxValue(maxWeight + 2);
        leftAxis.addLimitLine(goalLine);
        leftAxis.addLimitLine(startLine);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setLabelCount(0, false);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(minWeight - 2); // this replaces setStartAtZero(true)
        rightAxis.setAxisMaxValue(maxWeight + 2);


        mBarChart.setDescription(getString(R.string.app_name));
        mBarChart.setData(t);
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
