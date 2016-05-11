package net.yeonjukko.bodyend.activity.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yalantis.ucrop.UCrop;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.MaterialActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.UserInfoModel;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class DefaultSettingActivity extends AppCompatActivity {
    DBmanager dBmanager;
    DayCounter counter;
    private static final int PICK_PHOTO_FOR_AVATAR = 100;
    public static Uri mDestinationUri;
    public static String mDestination;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "BodyEnd";
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_setting);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_exit);

        ImageButton ibMenu = (ImageButton) findViewById(R.id.ib_menu);

        LinearLayout dayLayout = (LinearLayout) findViewById(R.id.layout_default_day);
        LinearLayout weightLayout = (LinearLayout) findViewById(R.id.layout_default_weight);
        LinearLayout pictureLayout = (LinearLayout) findViewById(R.id.layout_default_picture);
        LinearLayout wordLayout = (LinearLayout) findViewById(R.id.layout_default_word);
        dBmanager = new DBmanager(getContext());
        counter = new DayCounter();
        mDestination = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), SAMPLE_CROPPED_IMAGE_NAME + "_" + new Date().getTime()).getAbsolutePath();
        mDestinationUri = Uri.fromFile(new File(mDestination));

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menu 클릭
            }
        });
        //목표 날짜 설정
        dayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoModel model = dBmanager.selectUserInfoDB();
                DatePickerDialog mDialog = createDialog(model.getGoalDate());
                mDialog.setMinDate(Calendar.getInstance());
                mDialog.setTitle("변경할 날짜를 선택하세요");
                mDialog.show(getFragmentManager(), "date");

            }
        });

        //목표 문구 변경
        wordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoModel model = dBmanager.selectUserInfoDB();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialog);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_settings, null, false);
                final EditText etWord = (EditText) view.findViewById(R.id.et_addr);
                etWord.setText(model.getStimulusWord());
                builder.setTitle("목표 문구를 설정해주세요.    ")
                        .setView(view)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dBmanager.updateStimulusWord(etWord.getText().toString());
                                Toast.makeText(getContext(), "목표 문구가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();


            }
        });

        //목표 체중 설정
        weightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoModel model = dBmanager.selectUserInfoDB();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialog);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_setting_weight, null, false);
                final EditText etGoalWeight = (EditText) view.findViewById(R.id.et_addr);
                etGoalWeight.setText(model.getUserGoalWeight() + "");
                etGoalWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setTitle("목표 체중을 입력해주세요.")
                        .setView(view)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dBmanager.updateGoalWeight(Float.parseFloat(etGoalWeight.getText().toString()), counter.getToday());
                                Toast.makeText(getContext(), "목표 체중이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        //다이어트 자극사진 설정
        pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });

    }

    //<--Start of Pick Image
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    private Context getContext() {
        return this;
    }

    public DatePickerDialog createDialog(int goalDay) {
    /* calendar code here */


        String date = goalDay + "";
        int goalYear = Integer.parseInt(date.substring(0, 4));
        int goalMonth = Integer.parseInt(date.substring(4, 6)) - 1;
        int goalDate = Integer.parseInt(date.substring(6, 8));

        return DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("mox", "hellp");
                int convertDate = counter.convertDateInt(year,monthOfYear+1,dayOfMonth);
                dBmanager.updateGoalDate(convertDate);
                Toast.makeText(getContext(), "목표 날짜가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }, goalYear, goalMonth, goalDate);

    }

    private static boolean isBrokenSamsungDevice() {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung")
                && isBetweenAndroidVersions(
                Build.VERSION_CODES.LOLLIPOP,
                Build.VERSION_CODES.LOLLIPOP_MR1));
    }

    private static boolean isBetweenAndroidVersions(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            mDestinationUri = UCrop.getOutput(data);
            Log.d("mox", mDestinationUri.getPath());

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mDestinationUri);
                dBmanager.updateStimulusPic(mDestination);
                Toast.makeText(getContext(), "다이어트 자극 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(getContext(), "이미지 로드 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == PICK_PHOTO_FOR_AVATAR) {
            UCrop.of(data.getData(), mDestinationUri)
                    .withAspectRatio(9, 16)
                    .withMaxResultSize(1080, 1920)
                    .start(DefaultSettingActivity.this);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d("mox", "croperror" + cropError.toString());

        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, MaterialActivity.class);
        startActivity(intent);
        super.finish();
    }
}
