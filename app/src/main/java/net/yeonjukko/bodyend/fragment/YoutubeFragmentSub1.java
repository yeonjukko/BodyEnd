package net.yeonjukko.bodyend.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class YoutubeFragmentSub1 extends Fragment {
    View rootView;
    public JSONObject data = null;
    public static final String DEFAULT_URL = "http://yeonjukko.net:7533/";
    public static final String FLAG_GETVIDEO = "getVideo";
    public static final String FLAG_GETCATEGORY = "getCategory";


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_youtube_sub_1, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listViewCategory);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject resultCategory = (JSONObject) getServer(FLAG_GETCATEGORY);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultCategory == null) {
                            Toast.makeText(getContext(), "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else {

                            if ((long) resultCategory.get("code") == 0) {
                                final JSONArray categoryList = (JSONArray) resultCategory.get("contents");
                                String mCategory[] = new String[categoryList.size()];
                                for (int i = 0; i < categoryList.size(); i++) {
                                    JSONObject tmp = (JSONObject) categoryList.get(i);
                                    if (i == 8) {
                                        mCategory[i] = tmp.get("video_category_title")+"";

                                    } else {
                                        mCategory[i] = tmp.get("video_category_title") + " (" + tmp.get("video_count") + ")";
                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mCategory);
                                listView.setAdapter(adapter);
                                listView.setBackgroundColor(getResources().getColor(R.color.Icons));
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        FragmentManager fm = getFragmentManager();
                                        if (position == 8) {
                                            YoutubeFragmentSub3 fragment = new YoutubeFragmentSub3();
                                            fm.beginTransaction()
                                                    .replace(R.id.fragment3View, fragment)
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            YoutubeFragmentSub2 fragment = new YoutubeFragmentSub2((long) ((JSONObject) categoryList.get(position)).get("video_category_id"), (String) ((JSONObject) categoryList.get(position)).get("video_category_title"));
                                            fm.beginTransaction()
                                                    .replace(R.id.fragment3View, fragment)
                                                    .addToBackStack(null)
                                                    .commit();
                                        }
                                    }
                                });


//                                VideoListRecyclerViewAdapter viewAdapter = new VideoListRecyclerViewAdapter((JSONArray) result.get("contents"));
//                                recyclerViewYoutube.setAdapter(viewAdapter);

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


    private static Object getServer(String flag) {
        try {
            URL url = new URL(DEFAULT_URL + flag);
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
