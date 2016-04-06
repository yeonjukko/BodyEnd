package net.yeonjukko.bodyend.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.VideoListRecyclerViewAdapter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class YoutubeFragmentSub2 extends Fragment {
    View rootView;
    public JSONObject data = null;
    public static final String DEFAULT_URL = "http://yeonjukko.net:7533/getVideo?category=";
    public int id;

    public YoutubeFragmentSub2(int selectId) {
        super();
        id = selectId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_youtube_sub_2, container, false);

        final RecyclerView recyclerViewYoutube = (RecyclerView) rootView.findViewById(R.id.recyclerViewYoutube);
        recyclerViewYoutube.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject result = (JSONObject) getServer();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result == null) {
                            Toast.makeText(getContext(), "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("mox", result.toJSONString());
                            if ((long) result.get("code") == 0) {
                                VideoListRecyclerViewAdapter viewAdapter = new VideoListRecyclerViewAdapter((JSONArray) result.get("contents"));
                                recyclerViewYoutube.setAdapter(viewAdapter);

                            } else {
                                Toast.makeText(getContext(), "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

            }
        }).start();

        return rootView;

    }


    private Object getServer() {
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
    }


}
