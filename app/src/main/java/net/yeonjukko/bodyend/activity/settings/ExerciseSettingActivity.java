package net.yeonjukko.bodyend.activity.settings;

import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.ExerciseSpotInfoModel;

import java.util.ArrayList;

public class ExerciseSettingActivity extends AppCompatActivity {

    private static final int REQUEST_SPOT_NAME = 106;
    private static final int REQUEST_SORT_NAME = 107;

    DBmanager dBmanager;
    LinearLayout spotLayout;
    LinearLayout spotLayout2;
    LinearLayout sortLayout;
    LinearLayout sortLayout2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_setting);
        spotLayout = (LinearLayout) findViewById(R.id.layout_attendance_spot);
        spotLayout2 = (LinearLayout) findViewById(R.id.layout_attendance_spot_2);

        sortLayout = (LinearLayout) findViewById(R.id.layout_exercise);
        sortLayout2 = (LinearLayout) findViewById(R.id.layout_exercise_2);


        dBmanager = new DBmanager(getContext());

        setSpotList();
        spotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AttendanceMapAcitivity.class);
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (statusOfGPS) {
                    startActivityForResult(intent, REQUEST_SPOT_NAME);
                } else {
                    Toast.makeText(getContext(), "GPS를 켜주세요.", Toast.LENGTH_SHORT).show();
                    
                }
            }
        });

        sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ExerciseManagerActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setSpotList() {
        spotLayout2.removeAllViews();
        final ArrayList<ExerciseSpotInfoModel> model = dBmanager.selectExerciseSpotsInfo();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = 65;
        View view = findViewById(R.id.myView);
        ViewGroup.LayoutParams params2 = view.getLayoutParams();

        TextView tvSpots;
        for (int i = 0; i < model.size(); i++) {
            final int j = i;
            tvSpots = new TextView(this);
            tvSpots.setLayoutParams(params);
            tvSpots.setHeight(160);
            tvSpots.setGravity(Gravity.CENTER_VERTICAL);
            tvSpots.setText((i + 1) + ". " + model.get(i).getSpotName());
            spotLayout2.addView(tvSpots);

            View view2 = new View(this);
            view2.setLayoutParams(params2);
            view2.setBackgroundColor(getResources().getColor(R.color.Divider));
            spotLayout2.addView(view2);
            tvSpots.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Snackbar mySnackbar = Snackbar.make(v, "삭제하시겠습니까?", Snackbar.LENGTH_SHORT)
                            .setAction("삭제", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dBmanager.deleteSpot(model.get(j).getSpotId());
                                    setSpotList();
                                    Log.d("mox", dBmanager.selectExerciseSpotsInfo().size() + ":size");
                                }
                            });
                    View tmpView = mySnackbar.getView();
                    TextView tv = (TextView)tmpView.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(getResources().getColor(R.color.caldroid_white));
                    mySnackbar.show();
                    return false;
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SPOT_NAME) {
                double longitude = data.getDoubleExtra("longitude", 0);
                double latitude = data.getDoubleExtra("latitude", 0);
                String spotName = data.getStringExtra("location");
                ExerciseSpotInfoModel spotInfoModel = new ExerciseSpotInfoModel();
                spotInfoModel.setSpotX(longitude);
                spotInfoModel.setSpotY(latitude);
                spotInfoModel.setSpotName(spotName);
                spotInfoModel.setAttendanceDay(0);
                dBmanager.insertExerciseSpotInfo(spotInfoModel);
                setSpotList();
            }
        }
    }

    public Context getContext() {
        return this;
    }
}
