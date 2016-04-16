package net.yeonjukko.bodyend.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;

import java.util.ArrayList;


public class YoutubeFragmentSub3 extends Fragment {

    DBmanager dBmanager;
    LinearLayout ifNodataLayout;
    RecyclerView recyclerViewYoutube;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube_sub_3, container, false);
        ImageButton ibBack = (ImageButton) rootView.findViewById(R.id.ib_icon_back);
        ImageButton ibPlus = (ImageButton) rootView.findViewById(R.id.ib_plus_sort);
        ifNodataLayout = (LinearLayout) rootView.findViewById(R.id.if_no_data);
        recyclerViewYoutube = (RecyclerView) rootView.findViewById(R.id.recyclerViewYoutube);
        recyclerViewYoutube.setLayoutManager(new LinearLayoutManager(getContext()));
        dBmanager = new DBmanager(getContext());
        setLayout();

        //뒤로 가기 버튼
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //추가 하기 버튼
        ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialog);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_add_youtube, null, false);
                final EditText etYoutube = (EditText) view.findViewById(R.id.et_addr);
                builder.setTitle("추가할 유투브 아이디를 입력해주세요.")
                        .setMessage("유투브 아이디는 공유하기에서 찾을 수 있습니다.")
                        .setView(view)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dBmanager.insertMyYoutube(etYoutube.getText().toString());
                                setLayout();
                                Toast.makeText(getContext(), "나의 유투브가 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });


        return rootView;
    }

    public void setLayout() {
        ArrayList<String> youtubeList = dBmanager.selectMyYoutube();
        if (youtubeList.size() == 0) {
        } else {
            ifNodataLayout.setVisibility(View.GONE);
            Log.d("mox",youtubeList.get(0).toString());


        }
    }

}
