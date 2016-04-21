package net.yeonjukko.bodyend.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.libs.CheckableButton;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

public class InitInfoActivity extends AppCompatActivity {

    public static UserInfoModel userInfoModel;

    int sex = FLAG_SEX_UNCHECKED;  //0: female, 1:male, 2:unChecked
    public final static int FLAG_SEX_FEMALE = 0;
    public final static int FLAG_SEX_MALE = 1;
    public final static int FLAG_SEX_UNCHECKED = 2;
    public static Context mContext;
    private DBmanager dBmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_info);
        dBmanager = new DBmanager(this);

        if (userInfoModel == null) {
            userInfoModel = new UserInfoModel();
        }
        if (dBmanager.selectUserInfoDB() != null) {
            Intent intent = new Intent(this, MaterialActivity.class);
            startActivity(intent);
            finish();
        }
        final EditText etName = (EditText) findViewById(R.id.et_name);
        final EditText etHeight = (EditText) findViewById(R.id.et_height);
        final EditText etCurrWeight = (EditText) findViewById(R.id.et_curr_weight);
        final EditText etGoalWeight = (EditText) findViewById(R.id.et_target_weight);

        Button btNext = (Button) findViewById(R.id.bt_next);
        TextView tvCm = (TextView) findViewById(R.id.tv_cm);
        TextView tvKg1 = (TextView) findViewById(R.id.tv_kg1);
        TextView tvKg2 = (TextView) findViewById(R.id.tv_kg2);
        final EditText etSex = (EditText) findViewById(R.id.et_sex);

        //<-- Start of keyboard focus
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        tvCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHeight.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        tvKg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrWeight.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        tvKg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etGoalWeight.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });

        // End of keyboard focus-->


        //<-- Start of Checkable Button toggle
        final CheckableButton btFemale = (CheckableButton) findViewById(R.id.bt_female);
        final CheckableButton btMale = (CheckableButton) findViewById(R.id.bt_male);
        btFemale.setChecked(true);

        btFemale.setOnCheckedChangeWidgetListener(new CheckableButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CheckableButton buttonView, boolean isChecked) {
                if (btMale.isChecked()) {
                    btMale.setChecked(!isChecked);
                }
                if (btMale.isChecked() == btFemale.isChecked()) {
                    sex = FLAG_SEX_UNCHECKED;
                } else {
                    sex = FLAG_SEX_FEMALE;
                }

            }
        });

        btMale.setOnCheckedChangeWidgetListener(new CheckableButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CheckableButton buttonView, boolean isChecked) {
                if (btFemale.isChecked()) {
                    btFemale.setChecked(!isChecked);
                }

                if (btMale.isChecked() == btFemale.isChecked()) {
                    sex = FLAG_SEX_UNCHECKED;
                } else {
                    sex = FLAG_SEX_MALE;
                }


            }
        });

        // End of Checkable Button toggle-->


        //<-- Start of Next Button Click
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().equals("")) {
                    etName.setError("이름을 입력하세요");
                    return;
                }
                if (sex == FLAG_SEX_UNCHECKED) {
                    etSex.setError("성별을 정확히 입력하세요");
                    return;
                }
                if (etHeight.getText().toString().equals("") || etHeight.getText().toString().startsWith(".")) {
                    etHeight.setError("키를 입력하세요");
                    return;
                }
                if (etCurrWeight.getText().toString().equals("") || etCurrWeight.getText().toString().startsWith(".")) {
                    etCurrWeight.setError("현재 몸무게를 입력하세요");
                    return;
                }
                if (etGoalWeight.getText().toString().equals("") || etGoalWeight.getText().toString().startsWith(".")) {
                    etGoalWeight.setError("목표 몸무게를 입력하세요");
                    return;
                }

                userInfoModel.setUserName(etName.getText().toString());
                userInfoModel.setUserSex(sex);
                userInfoModel.setUserHeight(Float.parseFloat(etHeight.getText().toString()));
                userInfoModel.setUserCurrWeight(Float.parseFloat(etCurrWeight.getText().toString()));
                userInfoModel.setUserGoalWeight(Float.parseFloat(etGoalWeight.getText().toString()));

                Intent intent = new Intent(InitInfoActivity.this, InitTargetActivity.class);
                startActivity(intent);
                finish();

            }

        });
        //End of Next Button Click-->

        // if coming back to InitTarget Variable Setting

        if (userInfoModel.getUserName() == null)
            etName.setText("");
        else
            etName.setText(userInfoModel.getUserName());

        sex = userInfoModel.getUserSex();

        if (sex == FLAG_SEX_FEMALE) {
            btFemale.setChecked(true);
        } else if (sex == FLAG_SEX_MALE) {
            btMale.setChecked(true);
        }

        if (userInfoModel.getUserHeight() == 0)
            etHeight.setText("");
        else {
            etHeight.setText(userInfoModel.getUserHeight() + "");
        }

        if (userInfoModel.getUserCurrWeight() == 0)
            etCurrWeight.setText("");
        else
            etCurrWeight.setText(userInfoModel.getUserCurrWeight() + "");

        if (userInfoModel.getUserGoalWeight() == 0)
            etGoalWeight.setText("");
        else
            etGoalWeight.setText(userInfoModel.getUserGoalWeight() + "");

//다른 액티비티에서 호출할때
        mContext = this;


    }


}
