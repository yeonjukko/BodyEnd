package net.yeonjukko.bodyend.libs;
/**
 * Created by yeonjukko on 16. 3. 16..
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.yeonjukko.bodyend.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class VideoListRecyclerViewAdapter extends RecyclerView.Adapter<VideoListRecyclerViewAdapter.YoutubeListViewHolder> {

    private JSONArray data;
    private Context context;

    public VideoListRecyclerViewAdapter(final JSONArray data) {
        this.data = data;

    }

    @Override
    public VideoListRecyclerViewAdapter.YoutubeListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new YoutubeListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_video_list, parent, false));

    }

    @Override
    public void onBindViewHolder(final VideoListRecyclerViewAdapter.YoutubeListViewHolder holder, final int position) {
        JSONObject result = (JSONObject)data.get(position);
        Log.d("mox", result.get("video_id").toString());
        result.get("video_category_title");


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class YoutubeListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvVideoLength;
        public ImageView ivThumbnail;
        public TextView tvVideoName;

        public YoutubeListViewHolder(View v) {
            super(v);
            tvVideoLength = (TextView) v.findViewById(R.id.tv_video_length);
            tvVideoName = (TextView) v.findViewById(R.id.tv_video_name);
            ivThumbnail = (ImageView) v.findViewById(R.id.iv_video_thumnail);

        }
    }


}
