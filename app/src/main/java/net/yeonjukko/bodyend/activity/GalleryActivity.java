package net.yeonjukko.bodyend.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.model.UserRecordModel;

/**
 * Created by MoonJongRak on 2016. 4. 13..
 */
public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG_STYLE_COMPLETE = "complete";
    private final String TAG_STYLE_CHANGE = "change";

    private GalleryModeFragment0 mGalleryModeFragment0;
    private GalleryModeFragment1 mGalleryModeFragment1;

    private TextView mTextViewTitle;
    private ImageButton mImageButtonStyleChange;

    private String defaultTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mImageButtonStyleChange = (ImageButton) findViewById(R.id.imageButtonChangeStyle);
        mImageButtonStyleChange.setOnClickListener(this);
        findViewById(R.id.imageButtonBack).setOnClickListener(this);
        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        defaultTitle = mTextViewTitle.getText().toString();
        this.mGalleryModeFragment0 = new GalleryModeFragment0();
        getSupportFragmentManager().beginTransaction().replace(R.id.layoutGalleryMain, mGalleryModeFragment0).commit();
    }

    @Override
    public void onBackPressed() {
        if (mGalleryModeFragment0.isShowBigImage()) {
            mGalleryModeFragment0.hideBigImage();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonChangeStyle:
                if (mGalleryModeFragment0.isShowBigImage()) {
                    mGalleryModeFragment0.hideBigImage();
                }

                if (mImageButtonStyleChange.getTag() == null || mImageButtonStyleChange.getTag().equals(TAG_STYLE_CHANGE)) {
                    if (mGalleryModeFragment0.isSelectMode()) {
                        mGalleryModeFragment0.setSelectMode(false);
                        changeTitleDefault();
                    } else {
                        mGalleryModeFragment0.setSelectMode(true);
                    }
                } else if (mImageButtonStyleChange.getTag().equals(TAG_STYLE_COMPLETE)) {
                    UserRecordModel[] selected = mGalleryModeFragment0.getSelectedPicture();
                    if (selected == null) {
                        throw new IndexOutOfBoundsException("2개 이상 선택 안됐음.");
                    }
                    mGalleryModeFragment1 = new GalleryModeFragment1(selected);
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.layoutGalleryMain, mGalleryModeFragment1).commit();
                }

                break;
            case R.id.imageButtonBack:
                onBackPressed();
                break;
        }
    }

    public void changeTitle(int size) {

        mTextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mTextViewTitle.setTypeface(Typeface.DEFAULT);

        switch (size) {
            case 0:
                mTextViewTitle.setText("비교할 이미지 2개를 선택해주세요.");
                break;
            case 1:
                mTextViewTitle.setText("1개 더 선택해 주세요.");
                break;
            case 2:
                mTextViewTitle.setText("완료 버튼을 눌러주세요.");
                break;
        }
    }

    private void changeTitleDefault() {
        mTextViewTitle.setText(defaultTitle);
        mTextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        mTextViewTitle.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void imageButtonStyleChange(int size) {

        //선택 사이즈가 2개이면 알맞게 선택한것으로 간주.
        if (size == 2) {
            mImageButtonStyleChange.setImageResource(R.drawable.calendar_prev_arrow);
            mImageButtonStyleChange.setTag(TAG_STYLE_COMPLETE);
        } else {
            mImageButtonStyleChange.setImageResource(R.drawable.icon_youtube);
            mImageButtonStyleChange.setTag(TAG_STYLE_CHANGE);
        }
    }

    private Context getContext() {
        return this;
    }

}
