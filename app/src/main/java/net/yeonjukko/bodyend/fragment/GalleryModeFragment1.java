package net.yeonjukko.bodyend.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.UserRecordModel;

import org.w3c.dom.Text;

/**
 * Created by MoonJongRak on 2016. 4. 13..
 */
public class GalleryModeFragment1 extends Fragment {

    UserRecordModel[] data;
    DayCounter dayCounter;
    Bitmap image0, image1;

    public GalleryModeFragment1(UserRecordModel[] data) {
        super();
        this.data = data;
        image0 = BitmapFactory.decodeFile(data[0].getPictureRecord());
        image1 = BitmapFactory.decodeFile(data[1].getPictureRecord());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gallery_mode_1, container, false);
        ((ImageView) rootView.findViewById(R.id.imageViewCompare0)).setImageBitmap(image0);
        ((ImageView) rootView.findViewById(R.id.imageViewCompare1)).setImageBitmap(image1);

        dayCounter = new DayCounter();

        ((TextView) rootView.findViewById(R.id.textViewBeforeDay)).setText(dayCounter.convertDate2String(data[0].getRecordDate()));
        ((TextView) rootView.findViewById(R.id.textViewAfterDay)).setText(dayCounter.convertDate2String(data[1].getRecordDate()));

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        image0.recycle();
        image1.recycle();
    }
}
