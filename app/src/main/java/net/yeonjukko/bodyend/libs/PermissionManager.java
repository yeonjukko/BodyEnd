package net.yeonjukko.bodyend.libs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by yeonjukko on 16. 5. 30..
 */
public class PermissionManager {

    private Fragment fragment;
    private String permissionName;

    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int REQUEST_CAMERA_PERMISSION = 202;
    public static final int REQUEST_LOCATION_PERMISSION = 203;

    public PermissionManager(Fragment fragment, String permissionName) {
        this.fragment = fragment;
        this.permissionName = permissionName;
    }

    public boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public boolean checkPermission() {

        boolean result = false;
        if (!isOverMarshmallow()) {
            result = true;
            return result;
        }

        switch (permissionName) {
            case CAMERA_PERMISSION:
                if (ContextCompat.checkSelfPermission(fragment.getContext(), CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                    // 이 권한을 필요한 이유를 설명해야하는가?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), CAMERA_PERMISSION)) {
                        // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                        // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                        fragment.requestPermissions(new String[]{CAMERA_PERMISSION},
                                REQUEST_CAMERA_PERMISSION);


                    } else {
                        fragment.requestPermissions(new String[]{CAMERA_PERMISSION},
                                REQUEST_CAMERA_PERMISSION);
                    }
                    result = false;
                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
                } else {
                    result = true;
                }

            case LOCATION_PERMISSION:
                if (ContextCompat.checkSelfPermission(fragment.getContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {

                    // 이 권한을 필요한 이유를 설명해야하는가?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), LOCATION_PERMISSION)) {
                        fragment.requestPermissions(new String[]{LOCATION_PERMISSION},
                                REQUEST_LOCATION_PERMISSION);
                    } else {
                        fragment.requestPermissions(new String[]{LOCATION_PERMISSION},
                                REQUEST_LOCATION_PERMISSION);
                        // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
                    }

                    result = false;
                } else {
                    result = true;
                }
        }
        return result;
    }

}
