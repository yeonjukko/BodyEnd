package net.yeonjukko.bodyend.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yalantis.ucrop.UCrop;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.WaterAlarmInfoModel;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class InitTargetActivity extends InitInfoActivity {

    private DatePickerDialog dialog;
    private Calendar calendar;
    private ImageView imageStimulus;
    private Bitmap bitmap;
    public static String mDestination;
    public static Uri mDestinationUri;
    private EditText etStimulusImage;
    private WaterAlarmInfoModel waterAlarmInfoModel;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "BodyEnd";
    private static final int PICK_PHOTO_FOR_AVATAR = 100;
    private DBmanager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_target);

        dbManager = new DBmanager(getContext());
        waterAlarmInfoModel = new WaterAlarmInfoModel();

        final EditText etGoalDate = (EditText) findViewById(R.id.et_goal_date);
        etStimulusImage = (EditText) findViewById(R.id.et_stimulus_picture);
        final EditText etStimulusWord = (EditText) findViewById(R.id.et_stimulus_word);
        imageStimulus = (ImageView) findViewById(R.id.image_stimulus_picture);
        bitmap = null;

        Button btBack = (Button) findViewById(R.id.bt_back);
        Button btFinish = (Button) findViewById(R.id.bt_finish);
        mDestination = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), SAMPLE_CROPPED_IMAGE_NAME + "_" + new Date().getTime()).getAbsolutePath();
        mDestinationUri = Uri.fromFile(new File(mDestination));

        //<--Start of DatePicker
        assert etGoalDate != null;
        etGoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar today = Calendar.getInstance();
                dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        int now = new DayCounter().getToday();
                        int selected = getSelectDay(year, monthOfYear + 1, dayOfMonth);

                        if (now > selected) {
                            etGoalDate.setError("오늘보다 이전의 날짜를 선택하셨습니다!");
                        } else {
                            etGoalDate.setText(selected + "");
                            etGoalDate.setError(null);
                        }
                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));

                dialog.show(getFragmentManager(), "date");
            }
        });
        //End of DatePicker-->

        //<--Start of Stimulus Image
        etStimulusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //End of Stimulus Image-->


        //<--Start of BackButton
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitTargetActivity.this, InitInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //End of BackButton-->

        //<--Start of Finish Button
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etGoalDate.getText().toString().equals("")) {
                    etGoalDate.setError("날짜를 입력하세요");
                    return;
                }
                if (etStimulusWord.getText().toString().equals("")) {
                    etStimulusWord.setError("목표 한마디 입력하세요");
                    return;
                }
                if (bitmap == null) {
                    etStimulusImage.setError("이미지를 선택하세요");
                    return;
                }

                //모델에 저장
                userInfoModel.setUserName(userInfoModel.getUserName());
                userInfoModel.setUserSex(userInfoModel.getUserSex());
                userInfoModel.setUserHeight(userInfoModel.getUserHeight());
                userInfoModel.setUserCurrWeight(userInfoModel.getUserCurrWeight());
                userInfoModel.setUserGoalWeight(userInfoModel.getUserGoalWeight());
                userInfoModel.setGoalDate(Integer.parseInt(etGoalDate.getText().toString()));
                userInfoModel.setStimulusWord(etStimulusWord.getText().toString());
                userInfoModel.setStimulusPicture(mDestination);

                waterAlarmInfoModel.setWaterAlarmStatus(0);
                waterAlarmInfoModel.setWaterAlarmPeriod(30);
                waterAlarmInfoModel.setAlarmTimezoneStart(9);
                waterAlarmInfoModel.setAlarmTimezoneStop(18);
                dbManager.insertWaterAlarmInfoDB(waterAlarmInfoModel);


                //db에 저장
                dbManager.insertUserInfoDB(userInfoModel);
                Log.d("mox", "target" + dbManager.PrintData());
                dbManager.selectUserInfoDB();

                Intent intent = new Intent(getContext(), MaterialActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //End of Finish Button-->
    }

    //<--Start of Pick Image
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    //End of Pick Image


    //<--Start of Crop Image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            mDestinationUri = UCrop.getOutput(data);
            Log.d("mox", mDestinationUri.getPath());

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mDestinationUri);
                imageStimulus.setImageBitmap(bitmap);
                etStimulusImage.setError(null);

            } catch (IOException e) {
                Toast.makeText(getContext(), "이미지 로드 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == PICK_PHOTO_FOR_AVATAR) {
            UCrop.of(data.getData(), mDestinationUri)
                    .withAspectRatio(9, 16)
                    .withMaxResultSize(1080, 1920)
                    .start(InitTargetActivity.this);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d("mox", "croperror" + cropError.toString());

        }
    }

    //End of Crop Image-->
    public int getSelectDay(int year, int month, int date) {
        //<--오늘 날짜 구하기 ex)20160317

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


    private Context getContext() {
        return this;
    }

}
