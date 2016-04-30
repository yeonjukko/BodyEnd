package net.yeonjukko.bodyend.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.GalleryActivity;
import net.yeonjukko.bodyend.fragment.GalleryModeFragment0;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by MoonJongRak on 2016. 4. 13..
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ImageViewHolder> {
    private Queue<Integer> mSelectedQueue;

    private Context mContext;
    private DBmanager mDBmanager;
    private ArrayList<UserRecordModel> mUserRecordModels;

    private BitmapFactory.Options bitmapOptions;

    private GalleryModeFragment0 mGalleryModeFragment0;
    private boolean isSelectMode;

    public GalleryRecyclerViewAdapter(Context mContext, GalleryModeFragment0 mGalleryModeFragment0) {
        this.mGalleryModeFragment0 = mGalleryModeFragment0;
        this.mContext = mContext;
        this.mSelectedQueue = new LinkedList<>();
        mDBmanager = new DBmanager(mContext);
        mUserRecordModels = mDBmanager.selectUserRecordImage();

        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {

        if (isSelectMode) {
            holder.mLayoutSelected.setVisibility(View.GONE);
            for (int selected : mSelectedQueue) {
                if (selected == holder.getAdapterPosition()) {
                    holder.mLayoutSelected.setVisibility(View.VISIBLE);
                    break;
                }
            }
        } else {
            holder.mLayoutSelected.setVisibility(View.GONE);
        }

        new ImageLoadThread(holder.mImageViewBody, mUserRecordModels.get(holder.getAdapterPosition()).getPictureRecord()).start();
        holder.mImageViewBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRecordModel data = mUserRecordModels.get(holder.getAdapterPosition());
                if (isSelectMode) {
                    if (holder.mLayoutSelected.getVisibility() == View.VISIBLE) {
                        for (int i = 0; i < mSelectedQueue.size(); i++) {
                            if (((LinkedList<Integer>) mSelectedQueue).get(i) == holder.getAdapterPosition()) {
                                ((LinkedList<Integer>) mSelectedQueue).remove(i);
                                break;
                            }
                        }
                    } else {
                        if (mSelectedQueue.size() == 2) {
                            notifyItemChanged(mSelectedQueue.poll());
                        }
                        mSelectedQueue.add(holder.getAdapterPosition());
                    }
                    notifyItemChanged(holder.getAdapterPosition());
                    changeTitle();
                } else {
                    mGalleryModeFragment0.showBigImage(data.getPictureRecord());
                    GalleryActivity activity = (GalleryActivity) mContext;
                    activity.changeTitleSaveNormal(data.getPictureRecord());
                }
            }
        });
        if (!isSelectMode) {
            holder.mImageViewBody.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isSelectMode = true;
                    mSelectedQueue.add(holder.getAdapterPosition());
                    changeTitle();
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

    private void changeTitle() {
        GalleryActivity activity = (GalleryActivity) mContext;
        activity.changeTitle(mSelectedQueue.size());
    }


    @Override
    public int getItemCount() {
        return mUserRecordModels != null ? mUserRecordModels.size() : 0;
    }

    public void setModeSelect(boolean isSelectMode) {
        this.isSelectMode = isSelectMode;
        this.mSelectedQueue.clear();
        notifyDataSetChanged();
        changeTitle();
    }

    public UserRecordModel[] getSelectedPicture() {

        if (mSelectedQueue.size() != 2) {
            return null;
        }

        UserRecordModel[] result = new UserRecordModel[2];
        int index = 0;
        for (int selected : mSelectedQueue) {
            result[index] = mUserRecordModels.get(selected);
            index++;
        }

        Arrays.sort(result, new Comparator<UserRecordModel>() {
            @Override
            public int compare(UserRecordModel lhs, UserRecordModel rhs) {
                if (lhs.getRecordDate() > rhs.getRecordDate()) {
                    return 1;
                } else {
                    return 0;
                }

            }
        });

        return result;
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewBody;
        private ViewGroup mLayoutSelected;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageViewBody = (ImageView) itemView.findViewById(R.id.imageViewBody);
            mLayoutSelected = (ViewGroup) itemView.findViewById(R.id.layoutSelected);
        }
    }

    private class ImageLoadThread extends Thread {

        private ImageView mImageView;
        private String path;

        private ImageLoadThread(ImageView mImageView, String path) {
            this.mImageView = mImageView;
            this.path = path;
        }

        @Override
        public synchronized void start() {
            if (mImageView.getTag() != null) {
                ((ImageLoadThread) mImageView.getTag()).interrupt();
            }
            mImageView.setTag(this);
            super.start();
        }

        @Override
        public void run() {
            boolean isSaving = false;
            File cacheFile = getCacheFile();
            try {
                final Bitmap image;
                if (cacheFile.exists()) {
                    image = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                } else {
                    image = BitmapFactory.decodeFile(path, bitmapOptions);
                    try {
                        isSaving = true;
                        FileOutputStream outputStream = new FileOutputStream(cacheFile);
                        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        isSaving = false;
                    } catch (IOException e) {
                        deleteCahceFile(cacheFile);
                    }
                }

                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(image);
                    }
                });
            } catch (Exception ignored) {
                if (isSaving) {
                    deleteCahceFile(cacheFile);
                }

                ignored.printStackTrace();
            }
        }

        private File getCacheFile() {
            return new File(mContext.getExternalCacheDir(), Long.toString(path.hashCode()));
        }

        private void deleteCahceFile(File cacheFile) {
            if (cacheFile.exists()) {
                cacheFile.delete();
            }
        }
    }
}
