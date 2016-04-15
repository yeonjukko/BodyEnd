package net.yeonjukko.bodyend.activity.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.aakira.expandablelayout.Utils;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.CheckableButton;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DividerItemDecoration;
import net.yeonjukko.bodyend.libs.ExercisesRecyclerViewAdapter;
import net.yeonjukko.bodyend.libs.RecordItemModel;
import net.yeonjukko.bodyend.libs.RecordRecyclerViewAdapter;
import net.yeonjukko.bodyend.model.ExerciseSortInfoModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ExerciseManagerActivity extends AppCompatActivity {
    CheckableButton cbMonday;
    CheckableButton cbTuesday;
    CheckableButton cbWednesday;
    CheckableButton cbThursday;
    CheckableButton cbFriday;
    CheckableButton cbSaturday;
    CheckableButton cbSunday;
    RadioButton rdOxygen;
    RadioButton rdNonoxygen;
    RadioButton rdYoutube;
    EditText etExerciseName;

    public static final int FLAG_MONDAY = 0x40;
    public static final int FLAG_TUESDAY = 0x20;
    public static final int FLAG_WEDNESDAY = 0x10;
    public static final int FLAG_THURSDAY = 0x08;
    public static final int FLAG_FRIDAY = 0x04;
    public static final int FLAG_SATURDAY = 0x02;
    public static final int FLAG_SUNDAY = 0x01;
    public DBmanager dBmanager;
    ExercisesRecyclerViewAdapter adapter;
    View plusView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_manager);
        dBmanager = new DBmanager(getContext());

        ImageButton ibPlusSort = (ImageButton) findViewById(R.id.ib_plus_sort);
        setRecyclerLayout();

        assert ibPlusSort != null;
        ibPlusSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                plusView = inflater.from(getContext()).inflate(R.layout.dialog_ex_sort_plus, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
                setDialogLayout();

                builder.setView(plusView)
                        .setTitle("운동을 추가하세요.")
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            //운동 이름 가져오기

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int exerciseDay = 0;
                                if (cbMonday.isChecked())
                                    exerciseDay += FLAG_MONDAY;
                                if (cbTuesday.isChecked())
                                    exerciseDay += FLAG_TUESDAY;
                                if (cbWednesday.isChecked())
                                    exerciseDay += FLAG_WEDNESDAY;
                                if (cbThursday.isChecked())
                                    exerciseDay += FLAG_THURSDAY;
                                if (cbFriday.isChecked())
                                    exerciseDay += FLAG_FRIDAY;
                                if (cbSaturday.isChecked())
                                    exerciseDay += FLAG_SATURDAY;
                                if (cbSunday.isChecked())
                                    exerciseDay += FLAG_SUNDAY;

                                if (!(cbMonday.isChecked() || cbTuesday.isChecked() || cbWednesday.isChecked() || cbThursday.isChecked() || cbFriday.isChecked() || cbSaturday.isChecked() || cbSunday.isChecked())) {
                                    Toast.makeText(getContext(), "반드시 한 요일 이상 선택하세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                ExerciseSortInfoModel model = new ExerciseSortInfoModel();
                                model.setSortId(UUID.randomUUID().toString());
                                model.setExerciseDay(exerciseDay);
                                model.setExerciseName(etExerciseName.getText().toString());
                                if (rdOxygen.isChecked()) {
                                    model.setExerciseType(1);
                                    model.setSortId(UUID.randomUUID().toString());
                                } else if (rdNonoxygen.isChecked()) {
                                    model.setExerciseType(0);
                                    model.setSortId(UUID.randomUUID().toString());
                                }
//                                else if (rdYoutube.isChecked()) {
//                                    model.setExerciseType(2);
//                                    //model.setSortId(유투브 아이디를 입력);
//                                }
                                model.setExerciseAddDate(getToday());
                                dBmanager.insertExerciseSortInfo(model);
                                setRecyclerLayout();


                            }
                        }).show();
            }
        });
    }

    public void setRecyclerLayout() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_exercise);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList<ExerciseSortInfoModel> data = dBmanager.selectExerciseSortsInfo();
        adapter = new ExercisesRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private void setDialogLayout() {
        etExerciseName = (EditText) plusView.findViewById(R.id.et_exercise_name);
        cbMonday = (CheckableButton) plusView.findViewById(R.id.cb_monday);
        cbTuesday = (CheckableButton) plusView.findViewById(R.id.cb_tuesday);
        cbWednesday = (CheckableButton) plusView.findViewById(R.id.cb_wednesday);
        cbThursday = (CheckableButton) plusView.findViewById(R.id.cb_thursday);
        cbFriday = (CheckableButton) plusView.findViewById(R.id.cb_friday);
        cbSaturday = (CheckableButton) plusView.findViewById(R.id.cb_saturday);
        cbSunday = (CheckableButton) plusView.findViewById(R.id.cb_sunday);
        rdOxygen = (RadioButton) plusView.findViewById(R.id.rd_oxygen);
        rdNonoxygen = (RadioButton) plusView.findViewById(R.id.rd_nonoxygen);
        //rdYoutube = (RadioButton) plusView.findViewById(R.id.rd_youtube);
    }

    public int getToday() {
        //<--오늘 날짜 구하기 ex)20160317
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
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


        String today = year + strMonth + strDate;
        return Integer.parseInt(today);
    }

    public Context getContext() {
        return this;
    }
}
