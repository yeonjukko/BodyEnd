package net.yeonjukko.bodyend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.aakira.expandablelayout.Utils;

import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DividerItemDecoration;
import net.yeonjukko.bodyend.libs.RecordItemModel;
import net.yeonjukko.bodyend.libs.RecordRecyclerViewAdapter;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RecordActivity extends AppCompatActivity {
    public DBmanager dBmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        dBmanager = new DBmanager(this);

        //record 테이블 초기화 및 날짜 설정 메소드
        insertUserRecord();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




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

        RecordRecyclerViewAdapter adapter = new RecordRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    public void insertUserRecord() {
        UserRecordModel model = new UserRecordModel();

        model.setRecordDate(getToday());
        model.setPictureRecord("오늘의 몸매를 등록하세요.");
        model.setWeightRecord(dBmanager.selectUserInfoDB().getUserCurrWeight());
        model.setWaterRecord(0);
        model.setWaterVolume(dBmanager.selectUserInfoDB().getUserCurrWeight() * 33 / 300);
        model.setMealBreakfast("");
        model.setMealLunch("");
        model.setMealDinner("");
        model.setMealRefreshments("");
        dBmanager.insertUserRecordDB(model);
    }
    public int getToday(){
        //<--오늘 날짜 구하기 ex)20160317
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int date = calendar.get(Calendar.DATE);
        String strMonth = null;
        String strDate = null;

        if (month < 10)
            strMonth = "0" + month;
        else if (month >= 10)
            strMonth = month + "";

        if (date < 10)
            strDate = "0" + month;
        else if (date >= 10)
            strDate = date + "";


        String today = year + "" + strMonth + "" + strDate;
        return Integer.parseInt(today);
    }
}