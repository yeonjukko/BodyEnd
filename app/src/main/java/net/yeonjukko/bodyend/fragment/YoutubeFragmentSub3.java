package net.yeonjukko.bodyend.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import net.yeonjukko.bodyend.adapter.VideoListRecyclerViewAdapter;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.MyYoutubeInfoModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class YoutubeFragmentSub3 extends Fragment {

    DBmanager dBmanager;
    LinearLayout ifNodataLayout;
    RecyclerView recyclerViewYoutube;
    public static final String DEFAULT_URL = "http://yeonjukko.net:7533/getVideoInfo?id=";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube_sub_3, container, false);
        ImageButton ibBack = (ImageButton) rootView.findViewById(R.id.ib_icon_back);
        ImageButton ibPlus = (ImageButton) rootView.findViewById(R.id.ib_plus_sort);
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
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONObject result = (JSONObject) addMyYoutube(etYoutube.getText().toString());
                                        if (result != null) {
                                            JSONObject data = (JSONObject) result.get("contents");
                                            MyYoutubeInfoModel model = new MyYoutubeInfoModel();
                                            model.setYtId(data.get("video_id").toString());
                                            model.setYtTitle(data.get("video_title").toString());
                                            model.setYtDuration(Integer.parseInt(data.get("video_duration").toString()));
                                            model.setYtViewCount(Integer.parseInt(data.get("video_view_count").toString()));
                                            model.setYtThumbs(data.get("video_thumbs").toString());
                                            dBmanager.insertMyYoutube(model);
                                            Log.d("mox", "완료");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(), "나의 유투브가 추가 되었습니다.", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    }
                                }).start();


                                setLayout();
                            }
                        }).show();
            }
        });



        return rootView;
    }

    public Object addMyYoutube(String id) {
        try {
            URL url = new URL(DEFAULT_URL + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(3000);
            connection.connect();
            return JSONValue.parse(new InputStreamReader(connection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // TODO: 16. 5. 1. 삭제기능 추가 및 널 추가 방지 유투브 가로로 봤을때 체크안됨현상
    }

    public void setLayout() {
        JSONArray youtubeList = dBmanager.selectMyYoutube();
        if (youtubeList.size() == 0) {
            Toast.makeText(getContext(), "추가한 동영상이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            VideoListRecyclerViewAdapter viewAdapter = new VideoListRecyclerViewAdapter(youtubeList);
            recyclerViewYoutube.setAdapter(viewAdapter);


            Log.d("mox", youtubeList.get(0).toString());


        }
    }


}
