package net.yeonjukko.bodyend.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.GalleryActivity;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.UserRecordModel;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MoonJongRak on 2016. 4. 13..
 */
public class GalleryModeFragment1 extends Fragment {

    private ViewGroup mLayoutImageCompare;
    private UserRecordModel[] data;
    private DayCounter dayCounter;
    private Bitmap image0, image1;

    public GalleryModeFragment1() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.data = (UserRecordModel[]) args.getSerializable("data");
        image0 = BitmapFactory.decodeFile(data[0].getPictureRecord());
        image1 = BitmapFactory.decodeFile(data[1].getPictureRecord());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((GalleryActivity) getActivity()).changeTitleSaveCompare();
        View rootView = inflater.inflate(R.layout.fragment_gallery_mode_1, container, false);
        ((ImageView) rootView.findViewById(R.id.imageViewCompare0)).setImageBitmap(image0);
        ((ImageView) rootView.findViewById(R.id.imageViewCompare1)).setImageBitmap(image1);
        dayCounter = new DayCounter();

        ((TextView) rootView.findViewById(R.id.textViewBeforeDay)).setText(dayCounter.convertDate2String(data[0].getRecordDate()));
        ((TextView) rootView.findViewById(R.id.textViewAfterDay)).setText(dayCounter.convertDate2String(data[1].getRecordDate()));

        mLayoutImageCompare = (ViewGroup) rootView.findViewById(R.id.layoutImageCompare);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((GalleryActivity) getActivity()).changeTitle(2);
        image0.recycle();
        image1.recycle();
    }

    public boolean saveImage() {
        File saveRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BodyEnd");
        if (!saveRoot.exists()) {
            saveRoot.mkdir();
        }
        mLayoutImageCompare.buildDrawingCache();
        Bitmap image = mLayoutImageCompare.getDrawingCache();
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(saveRoot, "BodyEnd_Compare" + System.currentTimeMillis() + ".jpg")));
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
