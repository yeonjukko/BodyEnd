package net.yeonjukko.bodyend.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.adapter.GalleryRecyclerViewAdapter;
import net.yeonjukko.bodyend.model.UserRecordModel;

/**
 * Created by MoonJongRak on 2016. 4. 13..
 */
public class GalleryModeFragment0 extends Fragment {
    private View rootView;
    private GalleryRecyclerViewAdapter mGalleryRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_gallery_mode_0, container, false);
            RecyclerView mRecyclerViewGallery = (RecyclerView) rootView.findViewById(R.id.recyclerViewGallery);
            assert mRecyclerViewGallery != null;
            mRecyclerViewGallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mGalleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(getContext(), this);
            mRecyclerViewGallery.setAdapter(mGalleryRecyclerViewAdapter);
        }

        return rootView;
    }

    public void showBigImage(String path) {
        ImageView mImageViewBigImage = (ImageView) rootView.findViewById(R.id.imageViewBigImage);
        Bitmap image = BitmapFactory.decodeFile(path);
        if (image != null && mImageViewBigImage != null) {
            mImageViewBigImage.setImageBitmap(image);
            rootView.findViewById(R.id.recyclerViewGallery).setVisibility(View.GONE);
            mImageViewBigImage.setVisibility(View.VISIBLE);
        }
    }

    public void hideBigImage() {
        ImageView mImageViewBigImage = (ImageView) rootView.findViewById(R.id.imageViewBigImage);

        if (mImageViewBigImage.getVisibility() == View.VISIBLE) {
            mImageViewBigImage.setVisibility(View.GONE);
            rootView.findViewById(R.id.recyclerViewGallery).setVisibility(View.VISIBLE);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageViewBigImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();

        }
    }

    public boolean isShowBigImage() {
        return rootView.findViewById(R.id.imageViewBigImage).getVisibility() == View.VISIBLE;
    }

    public void setSelectMode(boolean isSelectMode) {
        mGalleryRecyclerViewAdapter.setModeSelect(isSelectMode);
    }

    public boolean isSelectMode() {
        return mGalleryRecyclerViewAdapter.isSelectMode();
    }

    public UserRecordModel[] getSelectedPicture() {
        return mGalleryRecyclerViewAdapter.getSelectedPicture();
    }

}
