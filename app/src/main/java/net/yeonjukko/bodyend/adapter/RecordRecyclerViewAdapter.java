package net.yeonjukko.bodyend.adapter;

/**
 * Created by yeonjukko on 16. 3. 16..
 */


import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.CameraActivity;
import net.yeonjukko.bodyend.activity.GalleryActivity;
import net.yeonjukko.bodyend.activity.MaterialActivity;
import net.yeonjukko.bodyend.fragment.RecordFragment;
import net.yeonjukko.bodyend.activity.settings.AttendanceMapAcitivity;
import net.yeonjukko.bodyend.activity.settings.ExerciseSettingActivity;
import net.yeonjukko.bodyend.activity.settings.WaterSettingActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.libs.Int2DayCalculator;
import net.yeonjukko.bodyend.libs.PermissionManager;
import net.yeonjukko.bodyend.libs.RecordFragmentItemModel;
import net.yeonjukko.bodyend.model.ExerciseAttendanceInfoModel;
import net.yeonjukko.bodyend.model.ExerciseJoinSortInfoModel;
import net.yeonjukko.bodyend.model.ExerciseSpotInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;
import net.yeonjukko.bodyend.model.YoutubeRecordModel;

import java.util.ArrayList;
import java.util.List;

import static net.yeonjukko.bodyend.R.drawable.layout_round_all;
import static net.yeonjukko.bodyend.R.drawable.layout_round_bottom;


public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final List<RecordFragmentItemModel> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private int VIEW_TYPE_WATER = 100;
    private int VIEW_TYPE_EXERCISE = 101;
    private int VIEW_TYPE_WEIGHT = 102;
    private int VIEW_TYPE_MEAL = 103;
    private int VIEW_TYPE_PICTURE = 104;
    private int VIEW_TYPE_ERROR = 105;
    public static final int REQUEST_CAMERA_CODE = 200;
    public static final int REQUEST_SPOT_NAME = 201;


    private CheckBox mCheckbox;
    private int showDate;
    public static RecyclerView.ViewHolder holderTest;
    private ProgressDialog mProgressDialog;

    private LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
    private ExerciseSpotInfoModel checkModel;
    private boolean isLocUpdated = false;

    DBmanager dBmanager;
    LayoutInflater inflater;
    RecordFragment recordFragemnt;
    DayCounter dayCounter;


    //layout 순서 정하기
    public RecordRecyclerViewAdapter(final List<RecordFragmentItemModel> data, int showDate, RecordFragment recordFragment) {
        this.data = data;
        this.recordFragemnt = recordFragment;
        //RecordActivity에서 넘어온 표시할 날짜
        this.showDate = showDate;

        expandState.append(0, true);
        expandState.append(1, false);
        expandState.append(2, true);
        expandState.append(3, true);
        expandState.append(4, true);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        dBmanager = new DBmanager(context);
        inflater = ((MaterialActivity) context).getLayoutInflater();
        dayCounter = new DayCounter();

        if (viewType == VIEW_TYPE_WATER) {
            return new ViewHolderWater(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_water, parent, false));
        } else if (viewType == VIEW_TYPE_EXERCISE) {
            return new ViewHolderExercise(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_exercise, parent, false));
        } else if (viewType == VIEW_TYPE_WEIGHT) {
            return new ViewHolderWeight(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_weight, parent, false));
        } else if (viewType == VIEW_TYPE_MEAL) {
            return new ViewHolderMeal(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_meal, parent, false));
        } else if (viewType == VIEW_TYPE_PICTURE) {
            return new ViewHolderPicture(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_picture, parent, false));
        }

        throw new IndexOutOfBoundsException("viewHolderTypeError: 내부 에러가 발생하였습니다. ");

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_WATER;
        } else if (position == 1) {
            return VIEW_TYPE_MEAL;
        } else if (position == 2) {
            return VIEW_TYPE_WEIGHT;

        } else if (position == 3) {
            return VIEW_TYPE_EXERCISE;

        } else if (position == 4) {
            return VIEW_TYPE_PICTURE;
        }
        return VIEW_TYPE_ERROR;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final RecordFragmentItemModel item = data.get(position);
        final Resources resource = context.getResources();
        holderTest = holder;

        UserRecordModel userRecordModel = dBmanager.selectUserRecordDB(showDate);
        if (position == 0) {

            // holderWater 설정
            final ViewHolderWater holderWater = (ViewHolderWater) holder;

            //오늘보다 이전의 데이터를 가져왔을 때 버튼 enable처리
            if (showDate < dayCounter.getToday()) {
                holderWater.btWaterMinus.setEnabled(false);
                holderWater.btWaterPlus.setEnabled(false);
            }

            holderWater.textView.setText(item.description);
            holderWater.textView.setTextSize(19);
            holderWater.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderWater.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderWater.itemView.setBackground(resource.getDrawable(layout_round_all));
            holderWater.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
            holderWater.expandableLayout.setInterpolator(item.interpolator);
            holderWater.expandableLayout.setExpanded(expandState.get(position));

            holderWater.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderWater.buttonLayout, 0f, 180f).start();
                    holderWater.itemView.setBackground(resource.getDrawable(layout_round_all));
                    holderWater.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderWater.buttonLayout, 180f, 0f).start();
                    holderWater.itemView.setBackground(resource.getDrawable(layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderWater.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderWater.expandableLayout);
                }
            });

            holderWater.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);

            //<--물 ui 설정
            final float waterVolume = userRecordModel.getWaterVolume();
            int water_cup_count = (int) waterVolume;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = 80;
            params.width = 80;
            ImageView fullWater;

            //<--물 기록 UI 설정 부분
            holderWater.gridWaterLayout.removeAllViews();
            if (waterVolume >= 20) water_cup_count = 20;
            for (int i = 0; i < water_cup_count; i++) {
                fullWater = new ImageView(context);
                fullWater.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                fullWater.setLayoutParams(params);
                holderWater.gridWaterLayout.addView(fullWater);
            }

            //<--마신 잔 수 나타내기
            int waterRecord = dBmanager.selectUserRecordDB(showDate).getWaterRecord();
            if (waterRecord > water_cup_count) {
                for (int i = 0; i < water_cup_count; i++) {
                    holderWater.gridWaterLayout.getChildAt(i).setBackgroundResource(R.drawable.icon_bottle_checked);
                    holderWater.gridWaterLayout.getChildAt(i).setTag("checked");
                }
            } else {
                for (int i = 0; i < waterRecord; i++) {
                    holderWater.gridWaterLayout.getChildAt(i).setBackgroundResource(R.drawable.icon_bottle_checked);
                    holderWater.gridWaterLayout.getChildAt(i).setTag("checked");
                }
            }

            holderWater.btWaterPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int waterRecord = dBmanager.selectUserRecordDB(showDate).getWaterRecord();
                    if (waterRecord >= waterVolume) {
                        Toast.makeText(context, "이미 목표를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    holderWater.gridWaterLayout.getChildAt(waterRecord).setBackgroundResource(R.drawable.icon_bottle_checked);
                    holderWater.gridWaterLayout.getChildAt(waterRecord).setTag("checked");
                    waterRecord++;
                    dBmanager.updateWaterRecord(waterRecord, showDate);
                    holderWater.gridWaterLayout.invalidate();
                }
            });

            holderWater.btWaterMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int waterRecord = dBmanager.selectUserRecordDB(showDate).getWaterRecord();
                    if (waterRecord == 0) {
                        Toast.makeText(context, "물 좀 드새오!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    holderWater.gridWaterLayout.getChildAt(waterRecord - 1).setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    holderWater.gridWaterLayout.getChildAt(waterRecord - 1).setTag(null);
                    waterRecord--;
                    dBmanager.updateWaterRecord(waterRecord, showDate);
                    holderWater.gridWaterLayout.invalidate();
                }
            });

            holderWater.btWaterSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WaterSettingActivity.class);
                    context.startActivity(intent);
                }
            });


        } else if (position == 3) {
            final ViewHolderExercise holderExercise = (ViewHolderExercise) holder;
            //체크박스리스너 설정 해제
            holderExercise.cbAttendance.setOnCheckedChangeListener(null);
            if (showDate < dayCounter.getToday()) {
                holderExercise.cbAttendance.setEnabled(false);
                //오늘 이전의 날짜에 출석체크를 이미 했을 때
                if (dBmanager.selectExerciseAttendance(showDate) != null) {
                    holderExercise.cbAttendance.setChecked(false);
                    holderExercise.cbAttendance.setTextColor(resource.getColor(R.color.Divider));
                    holderExercise.cbAttendance.setPaintFlags(holderExercise.cbAttendance.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holderExercise.tvSpotName.setTextColor(resource.getColor(R.color.Divider));
                    holderExercise.tvSpotName.setText(dBmanager.selectExerciseSpotInfo(dBmanager.selectExerciseAttendance(showDate).getSpotId()).getSpotName() + "");
                } else {
                    holderExercise.cbAttendance.setTextColor(resource.getColor(android.R.color.holo_red_light));
                }
            } else {
                //오늘 출석체크를 이미 했을 때
                if (dBmanager.selectExerciseAttendance(showDate) != null) {
                    holderExercise.cbAttendance.setHighlightColor(resource.getColor(R.color.caldroid_light_red));
                    holderExercise.cbAttendance.setTextColor(resource.getColor(R.color.Divider));
                    holderExercise.cbAttendance.setPaintFlags(holderExercise.cbAttendance.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holderExercise.tvSpotName.setText("in " + dBmanager.selectExerciseSpotInfo(dBmanager.selectExerciseAttendance(showDate).getSpotId()).getSpotName() + "");
                    holderExercise.tvSpotName.setTextColor(resource.getColor(R.color.Divider));
                    Log.d("mox", dBmanager.selectExerciseSpotInfo(dBmanager.selectExerciseAttendance(showDate).getSpotId()).getSpotName() + "");
                    holderExercise.cbAttendance.setChecked(true);
                    holderExercise.cbAttendance.setEnabled(false);
                } else {
                    holderExercise.cbAttendance.setChecked(false);
                }
            }

            holderExercise.textView.setText(item.description);
            holderExercise.textView.setTextSize(19);
            holderExercise.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderExercise.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderExercise.itemView.setBackground(resource.getDrawable(layout_round_all));
            holderExercise.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
            holderExercise.expandableLayout.setInterpolator(item.interpolator);

            holderExercise.expandableLayout.setExpanded(true);
            //holderExercise.expandableLayout.setExpanded(expandState.get(position));
            holderExercise.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderExercise.buttonLayout, 0f, 180f).start();
                    holderExercise.itemView.setBackground(resource.getDrawable(layout_round_all));
                    holderExercise.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderExercise.buttonLayout, 180f, 0f).start();
                    holderExercise.itemView.setBackground(resource.getDrawable(layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderExercise.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderExercise.expandableLayout);
                }
            });
            holderExercise.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            holderExercise.tvSpotName.setTextColor(resource.getColor(R.color.Secondary_text));

            holderExercise.btExerciseSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExerciseSettingActivity.class);
                    context.startActivity(intent);
                }
            });

            mCheckbox = holderExercise.cbAttendance;
            holderExercise.cbAttendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (dBmanager.selectExerciseAttendance(showDate) != null) {
                            Toast.makeText(context, "이미 출석체크가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        if (new PermissionManager(recordFragemnt, PermissionManager.LOCATION_PERMISSION).checkPermission()) {
                            permissionCheckAfter();

                        } else {
                            notifyDataSetChanged();
                            return;
                        }

                        // TODO: 16. 6. 2. 출석체크 퍼미션

                    }
                }
            });


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140);
            params.gravity = Gravity.CENTER_VERTICAL;

            //오늘날짜
            if (dayCounter.getToday() == showDate) {
                final ArrayList<ExerciseJoinSortInfoModel> model = dBmanager.selectRecordExerciseSortsInfo(true, showDate, new Int2DayCalculator().getDay(showDate));
                final ArrayList<YoutubeRecordModel> ytModel = dBmanager.selectYoutubeInfo(showDate);
                //오늘의 운동기록 체크
                for (int i = 0; i < model.size(); i++) {
                    final int j = i;
                    final CheckBox mFlam = new CheckBox(context);
                    mFlam.setText(model.get(i).getExerciseName());
                    mFlam.setTextColor(resource.getColor(R.color.Primary_text));

                    if (model.get(i).getRecordDate() != 0) {
                        //해야할 운동을 했을 때
                        mFlam.setChecked(true);
                        mFlam.setTextColor(resource.getColor(R.color.Divider));
                        mFlam.setPaintFlags(mFlam.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    } else {
                        //해야할 운동을 안 했을 때
                        mFlam.setChecked(false);
                    }
                    mFlam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            dBmanager.updateCheckStatus(model.get(j).getSortId(), showDate, isChecked);
                            if (isChecked) {
                                mFlam.setTextColor(resource.getColor(R.color.Divider));
                                mFlam.setPaintFlags(mFlam.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            } else {
                                mFlam.setTextColor(resource.getColor(R.color.Primary_text));
                                mFlam.setPaintFlags(0);
                            }
                        }
                    });
                    holderExercise.exsortLayout.addView(mFlam, params);
                }
                //유투브 운동 기록
                for (int i = 0; i < ytModel.size(); i++) {
                    CheckBox mFlam = new CheckBox(context);
                    mFlam.setText(ytModel.get(i).getYoutubeTitle());
                    mFlam.setTextColor(resource.getColor(R.color.Divider));
                    mFlam.setPaintFlags(mFlam.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    mFlam.setChecked(true);
                    mFlam.setEnabled(false);
                    holderExercise.exsortLayout.addView(mFlam, params);
                }
                //오늘 이전의 날짜
            } else {
                final ArrayList<ExerciseJoinSortInfoModel> model = dBmanager.selectRecordExerciseSortsInfo(false, showDate, new Int2DayCalculator().getDay(showDate));
                final ArrayList<YoutubeRecordModel> ytModel = dBmanager.selectYoutubeInfo(showDate);
                //오늘의 운동기록 체크
                for (int i = 0; i < model.size(); i++) {
                    CheckBox mFlam = new CheckBox(context);
                    mFlam.setText(model.get(i).getExerciseName());
                    if (model.get(i).getRecordDate() != 0) {
                        //해야할 운동을 했을 때
                        mFlam.setChecked(true);
                        mFlam.setTextColor(resource.getColor(R.color.Divider));
                        mFlam.setPaintFlags(holderExercise.cbAttendance.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    } else {
                        //해야할 운동을 안 했을 때
                        mFlam.setTextColor(resource.getColor(android.R.color.holo_red_light));
                        mFlam.setChecked(false);
                    }
                    mFlam.setEnabled(false);
                    holderExercise.exsortLayout.addView(mFlam, params);
                }
                //유투브 운동 기록

                for (int i = 0; i < ytModel.size(); i++) {
                    CheckBox mFlam = new CheckBox(context);
                    mFlam.setText(ytModel.get(i).getYoutubeTitle());
                    mFlam.setTextColor(resource.getColor(R.color.Divider));
                    mFlam.setChecked(true);
                    mFlam.setEnabled(false);
                    mFlam.setPaintFlags(mFlam.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holderExercise.exsortLayout.addView(mFlam, params);
                }
            }


        } else if (position == 2) {
            final ViewHolderWeight holderWeight = (ViewHolderWeight) holder;

            //오늘보다 이전의 데이터를 가져왔을 때 버튼 enable처리
            if (showDate < dayCounter.getToday()) {
                holderWeight.btWeightPlus.setEnabled(false);
                holderWeight.btWeightMinus.setEnabled(false);
            }
            holderWeight.textView.setText(item.description);
            holderWeight.textView.setTextSize(19);
            holderWeight.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderWeight.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderWeight.itemView.setBackground(resource.getDrawable(layout_round_all));
            holderWeight.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
            holderWeight.expandableLayout.setInterpolator(item.interpolator);
            holderWeight.expandableLayout.setExpanded(expandState.get(position));
            holderWeight.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderWeight.buttonLayout, 0f, 180f).start();
                    holderWeight.itemView.setBackground(resource.getDrawable(layout_round_all));
                    holderWeight.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderWeight.buttonLayout, 180f, 0f).start();
                    holderWeight.itemView.setBackground(resource.getDrawable(layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderWeight.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderWeight.expandableLayout);
                }
            });
            holderWeight.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            holderWeight.tvWeight.setText((dBmanager.selectUserInfoDB().getUserCurrWeight()) + "kg");
            holderWeight.tvWeight.setTextColor(resource.getColor(R.color.Primary_text));
            holderWeight.btWeightPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    float weight = dBmanager.selectUserInfoDB().getUserCurrWeight() + 0.1f;
                    String strNumber = String.format("%.1f", weight);
                    dBmanager.updateCurrWeight(Float.parseFloat(strNumber), showDate);
                    holderWeight.tvWeight.setText(strNumber + "kg");
                    holderWeight.expandableLayout.invalidate();
                }
            });
            holderWeight.btWeightMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float weight = (float) (dBmanager.selectUserInfoDB().getUserCurrWeight() - 0.1);
                    String strNumber = String.format("%.1f", weight);
                    dBmanager.updateCurrWeight(Float.parseFloat(strNumber), showDate);
                    holderWeight.tvWeight.setText(strNumber + "kg");
                    holderWeight.expandableLayout.invalidate();
                }
            });


        } else if (position == 1) {
            final ViewHolderMeal holderMeal = (ViewHolderMeal) holder;

            //오늘보다 이전의 데이터를 가져왔을 때 버튼 enable처리
            if (showDate < dayCounter.getToday()) {
                holderMeal.etBreakfast.setEnabled(false);
                holderMeal.etLunch.setEnabled(false);
                holderMeal.etDinner.setEnabled(false);
                holderMeal.etRefreshment.setEnabled(false);

            }

            holderMeal.textView.setText(item.description);
            holderMeal.textView.setTextSize(19);
            holderMeal.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderMeal.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderMeal.itemView.setBackground(resource.getDrawable(layout_round_all));
            holderMeal.expandableLayout.setBackground(resource.getDrawable(layout_round_bottom));
            holderMeal.expandableLayout.setInterpolator(item.interpolator);

            //holderMeal.expandableLayout.setExpanded(expandState.get(position));
            holderMeal.expandableLayout.setExpanded(false);
            holderMeal.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderMeal.buttonLayout, 0f, 180f).start();
                    holderMeal.itemView.setBackground(resource.getDrawable(layout_round_all));
                    holderMeal.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderMeal.buttonLayout, 180f, 0f).start();
                    holderMeal.itemView.setBackground(resource.getDrawable(layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderMeal.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderMeal.expandableLayout);
                }
            });

            holderMeal.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            holderMeal.tvBreakfast.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.tvLunch.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.tvDinner.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.tvRefreshment.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etBreakfast.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etLunch.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etDinner.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etRefreshment.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etBreakfast.setText(userRecordModel.getMealBreakfast());
            holderMeal.etLunch.setText(userRecordModel.getMealLunch());
            holderMeal.etDinner.setText(userRecordModel.getMealDinner());
            holderMeal.etRefreshment.setText(userRecordModel.getMealRefreshments());
            holderMeal.etBreakfast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holderMeal.etBreakfast.requestFocus();
                    Handler mHandler = new Handler();

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            mgr.showSoftInput(holderMeal.etBreakfast, InputMethodManager.SHOW_FORCED);
                        }
                    }, 500);

                }
            });
            holderMeal.etRefreshment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holderMeal.etRefreshment.requestFocus();
                    Handler mHandler = new Handler();

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            mgr.showSoftInput(holderMeal.etRefreshment, InputMethodManager.SHOW_FORCED);
                        }
                    }, 500);

                }
            });

            holderMeal.etBreakfast.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    dBmanager.updateBreakfast(s.toString(), showDate);
                }
            });
            holderMeal.etLunch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    dBmanager.updateLunch(s.toString(), showDate);
                }
            });
            holderMeal.etDinner.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    dBmanager.updateDinner(s.toString(), showDate);
                }
            });
            holderMeal.etRefreshment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    dBmanager.updateRefreshment(s.toString(), recordFragemnt.showDate);
                }
            });


        } else if (position == 4) {
            final ViewHolderPicture holderPicture = (ViewHolderPicture) holder;
            //오늘보다 이전의 데이터를 가져왔을 때 버튼 enable처리
            if (showDate < dayCounter.getToday()) {
                holderPicture.btCamera.setEnabled(false);

            }
            holderPicture.textView.setText(item.description);
            holderPicture.textView.setTextSize(19);
            holderPicture.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderPicture.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderPicture.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
            holderPicture.expandableLayout.setInterpolator(item.interpolator);
            holderPicture.expandableLayout.setExpanded(expandState.get(position));
            holderPicture.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderPicture.buttonLayout, 0f, 180f).start();
                    holderPicture.itemView.setBackground(resource.getDrawable(layout_round_all));
                    holderPicture.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderPicture.buttonLayout, 180f, 0f).start();
                    holderPicture.itemView.setBackground(resource.getDrawable(layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderPicture.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderPicture.expandableLayout);
                }
            });
            holderPicture.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            holderPicture.btGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, GalleryActivity.class);
                    recordFragemnt.startActivity(intent);
                }
            });
            holderPicture.btCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CameraActivity.class);
                    recordFragemnt.startActivityForResult(intent, REQUEST_CAMERA_CODE);

                }
            });

            if (!userRecordModel.getPictureRecord().equals("")) {

                Bitmap bitmap = BitmapFactory.decodeFile(userRecordModel.getPictureRecord());
                holderPicture.imageTodayPic.setImageBitmap(bitmap);
                holderPicture.imageTodayPic.setVisibility(View.VISIBLE);
                holderPicture.expandableLayout.setExpanded(expandState.get(position));
                holderPicture.expandableLayout.getLayoutParams().height = bitmap.getHeight() - holderPicture.imageTodayPic.getPaddingBottom() * 4;

            }


        }

    }

    public void permissionCheckAfter() {
        final AlertDialog.Builder builderAttend = new AlertDialog.Builder(context, R.style.MyDialog);
        ArrayList<ExerciseSpotInfoModel> models = dBmanager.selectExerciseSpotsInfo();
        int size = models.size();
        String items[] = new String[size];
        for (int i = 0; i < dBmanager.selectExerciseSpotsInfo().size(); i++) {
            items[i] = models.get(i).getSpotName();
        }

        builderAttend.setTitle("출석체크 할 장소를 선택해주세요. ")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (statusOfGPS) {
                            mProgressDialog = ProgressDialog.show(context, "",
                                    "잠시만 기다려 주세요.", true);
                            //선택한 장소의 정보가져오기
                            checkModel = dBmanager.selectExerciseSpotsInfo().get(which);
                            buildGoogleApiClient();
                            createLocationRequest();
                            dialog.dismiss();

                        } else {
                            Toast.makeText(context, "GPS을 켜고 출석체크하세요.", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();
                    }
                })
                .setNeutralButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, AttendanceMapAcitivity.class);
                        recordFragemnt.startActivityForResult(intent, REQUEST_SPOT_NAME);
                        notifyDataSetChanged();

                    }
                })
                .show();
    }


    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolderWater extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public GridLayout gridWaterLayout;
        public ImageView btWaterSetting;
        public Button btWaterPlus;
        public Button btWaterMinus;


        public ViewHolderWater(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_water);
            gridWaterLayout = (GridLayout) v.findViewById(R.id.layout_water_grid);
            btWaterPlus = (Button) v.findViewById(R.id.bt_water_plus);
            btWaterMinus = (Button) v.findViewById(R.id.bt_water_minus);
            btWaterSetting = (ImageView) v.findViewById(R.id.bt_water_setting);

        }
    }

    public static class ViewHolderExercise extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public LinearLayout exsortLayout;
        public ExpandableRelativeLayout expandableLayout;
        public TextView tvSpotName;
        public CheckBox cbAttendance;
        public ImageButton btExerciseSetting;

        public ViewHolderExercise(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button1);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_exercise);
            exsortLayout = (LinearLayout) v.findViewById(R.id.layout_exercise_sorts);
            tvSpotName = (TextView) v.findViewById(R.id.tv_spot_name);
            cbAttendance = (CheckBox) v.findViewById(R.id.cb_attendance);
            btExerciseSetting = (ImageButton) v.findViewById(R.id.bt_exercise_setting);

        }
    }

    public static class ViewHolderWeight extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public TextView tvWeight;
        public Button btWeightPlus;
        public Button btWeightMinus;

        public ViewHolderWeight(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button2);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_weight);
            tvWeight = (TextView) v.findViewById(R.id.tv_weight);
            btWeightPlus = (Button) v.findViewById(R.id.bt_weight_plus);
            btWeightMinus = (Button) v.findViewById(R.id.bt_weight_minus);

        }
    }

    public static class ViewHolderMeal extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public TextView tvBreakfast;
        public TextView tvLunch;
        public TextView tvDinner;
        public TextView tvRefreshment;
        public EditText etBreakfast;
        public EditText etLunch;
        public EditText etDinner;
        public EditText etRefreshment;

        public ViewHolderMeal(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button3);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_meal);
            tvBreakfast = (TextView) v.findViewById(R.id.tv_breakfast);
            tvLunch = (TextView) v.findViewById(R.id.tv_lunch);
            tvDinner = (TextView) v.findViewById(R.id.tv_dinner);
            tvRefreshment = (TextView) v.findViewById(R.id.tv_refreshment);
            etBreakfast = (EditText) v.findViewById(R.id.et_breakfast);
            etLunch = (EditText) v.findViewById(R.id.et_lunch);
            etDinner = (EditText) v.findViewById(R.id.et_dinner);
            etRefreshment = (EditText) v.findViewById(R.id.et_refreshment);
        }
    }

    public static class ViewHolderPicture extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public ImageView imageTodayPic;
        public ImageButton btGallery;
        public ImageButton btCamera;

        public ViewHolderPicture(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button4);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_picture);
            imageTodayPic = (ImageView) v.findViewById(R.id.image_today_picture);
            btCamera = (ImageButton) v.findViewById(R.id.bt_camera);
            btGallery = (ImageButton) v.findViewById(R.id.bt_picture_setting);
        }


    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }


    //출석체크 관련 메소드
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("service", "Attend onConnected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("conn", "connsusp");

        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        //onConnect 다음 실행
        isLocUpdated = true;
        stopLocationUpdates();
        double dbX = checkModel.getSpotX();
        double dbY = checkModel.getSpotY();

        Location lo = new Location("dbLocation");
        lo.setLongitude(dbX);
        lo.setLatitude(dbY);
        float distance = location.distanceTo(lo);

        if (distance <= 50) {
            ExerciseAttendanceInfoModel model = new ExerciseAttendanceInfoModel();
            model.setRecordDate(showDate);
            model.setSpotId(checkModel.getSpotId());
            dBmanager.insertExerciseSpotAttendance(model);
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "거리가 멀어서 출석체크할 수 없습니다.", Toast.LENGTH_SHORT).show();
            //출석체크 업데이트
            mCheckbox.setChecked(false);

        }
        mProgressDialog.dismiss();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //출석체크 업데이트
        Log.d("conn", "connfail");

        mCheckbox.setChecked(false);
        Toast.makeText(context, "출석체크를 할 수 없습니다.", Toast.LENGTH_SHORT).show();

    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    if (!isLocUpdated) {
                        stopLocationUpdates();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                mCheckbox.setChecked(false);
                                Toast.makeText(context, "출석체크를 할 수 없습니다.", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Log.d("service", "Attend startLocationUpdates");
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();

        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        Log.d("service", "Attend createLocationRequest");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }


}
