package net.yeonjukko.bodyend.adapter;
/**
 * Created by yeonjukko on 16. 3. 16..
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.YoutubePlayerActivity;
import net.yeonjukko.bodyend.libs.AndroidImage2Bitmap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class VideoListRecyclerViewAdapter extends RecyclerView.Adapter<VideoListRecyclerViewAdapter.YoutubeListViewHolder> {

    private JSONArray data;
    private Context context;
    private AndroidImage2Bitmap image2Bitmap;

    public VideoListRecyclerViewAdapter(final JSONArray data) {
        this.data = data;

    }

    @Override
    public VideoListRecyclerViewAdapter.YoutubeListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        this.image2Bitmap = new AndroidImage2Bitmap();
        return new YoutubeListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_video_list, parent, false));

    }

    @Override
    public void onBindViewHolder(final VideoListRecyclerViewAdapter.YoutubeListViewHolder holder, final int position) {
        final JSONObject result = (JSONObject) data.get(position);
        holder.tvVideoName.setText(result.get("video_title") + "");
        holder.tvVideoLength.setText(getVideoLength2Text((long) result.get("video_duration")));
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = image2Bitmap.getImageBitmap((String) result.get("video_thumbs"));
                holder.ivThumbnail.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.ivThumbnail.setImageBitmap(bitmap);

                    }
                });

            }
        }).start();
        holder.tvVideoCount.setText(getVideoCount2Text((long) result.get("video_view_count")));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YoutubePlayerActivity.class);
                intent.putExtra("videoTitle",result.get("video_title")+"");
                intent.putExtra("videoId",result.get("video_id")+"");
                context.startActivity(intent);
            }
        });

    }
    public String getVideoCount2Text(long length){
        String tmp = "";
        if(length>=10000){
            tmp = length/10000 +"만";
        }else if(length>=100000000){
            tmp = length/100000000+"억 "+((length/10000)%10000)+"만";
        }
        return tmp;
    }
    public String getVideoLength2Text(long second) {
        String mHour = "";
        String mMinute = "";
        String mSecond = "";

        if (second - 60 * 60 > 1) {
            mHour = (second / (60 * 60)) + "h ";
        }
        if (second % 60 > 0) {
            mMinute = (second / 60) % 60 + "m ";
        }
        if (second > 0) {
            mSecond = second % 60 + "s";
        }


        return mHour + mMinute + mSecond;

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class YoutubeListViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView tvVideoLength;
        public ImageView ivThumbnail;
        public TextView tvVideoName;
        public TextView tvVideoCount;

        public YoutubeListViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout_youtube);
            tvVideoLength = (TextView) v.findViewById(R.id.tv_video_length);
            tvVideoName = (TextView) v.findViewById(R.id.tv_video_name);
            ivThumbnail = (ImageView) v.findViewById(R.id.iv_video_thumnail);
            tvVideoCount = (TextView) v.findViewById(R.id.tv_video_count);
        }
    }


}
