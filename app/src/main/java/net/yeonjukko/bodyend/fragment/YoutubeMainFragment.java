package net.yeonjukko.bodyend.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.yeonjukko.bodyend.R;

public class YoutubeMainFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_youtube, null, false);

        FragmentManager fm = getFragmentManager();
        ((ViewGroup) rootView.findViewById(R.id.fragment3View)).removeAllViews();
        YoutubeFragmentSub1 fragment = new YoutubeFragmentSub1();
        fm.beginTransaction()
                .replace(R.id.fragment3View, fragment)
                .commit();

        return rootView;
    }



}
