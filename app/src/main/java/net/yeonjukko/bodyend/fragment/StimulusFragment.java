package net.yeonjukko.bodyend.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.settings.DefaultSettingActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;

public class StimulusFragment extends Fragment {
    Bitmap bitmap;
    DBmanager dBmanager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_stimulus, container, false);
        dBmanager = new DBmanager(rootView.getContext());

        TextView tvDday = (TextView) rootView.findViewById(R.id.tv_d_day);
        TextView tvStimulusWord = (TextView) rootView.findViewById(R.id.tv_stimulus);

        //레이아웃 데이터 설정
        DayCounter counter = new DayCounter();
        int day = counter.dayCounter(dBmanager.selectUserInfoDB().getGoalDate());
        String stimulusWord = dBmanager.selectUserInfoDB().getStimulusWord();

        tvDday.setText(day + "");
        tvStimulusWord.setText(stimulusWord);


        //<--레이아웃 배경화면 설정
        final ImageView layout = (ImageView) rootView.findViewById(R.id.layout_bgr);

        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(dBmanager.selectUserInfoDB().getStimulusPicture(), options);
                final Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawable.setAlpha(190);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.setBackground(drawable);

                    }
                });

            }
        }).start();

        // TODO: 16. 5. 17. 프레그먼트 표시넣기

        // TODO: 16. 5. 11. 베타에만 넣고 빼기
        final SharedPreferences spref = getContext().getSharedPreferences("mNotice1", Context.MODE_PRIVATE);
        //공지사항을 안읽었을 경우우
        if (!spref.getBoolean("mNotice1", false)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialog);
            builder.setTitle("바디엔드 공지사항")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("[v1.03 업데이트 사항]\n - 공지사항탭추가\n - 유투브 앱에서 바로 유투브 추가 기능 \n\n 0922님❤ 강하나 하체 영상 교체했습니당!\n 1472님❤ 유투브앱 공유-bodyend 선택 기능 추가했어용\n\n여러분~ 호옥시 에러가 나더라도 당황하지 않코 침착하게 보고서 누르기를 눌러주세용~\n ")
                    .setCancelable(false)
                    .setPositiveButton("네! 보고서 제출하겠습니다", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = spref.edit();
                            editor.putBoolean("mNotice1", true);
                            editor.apply();
                            dialog.dismiss();

                        }
                    })
                    .show();
        }

        return rootView;
    }


}
