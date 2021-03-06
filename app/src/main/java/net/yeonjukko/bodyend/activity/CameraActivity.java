package net.yeonjukko.bodyend.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by MoonJongRak on 2016. 3. 18..
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FLAG_FILE_PATH = "result_file_path";
    private int mCameraFace = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera mCamera;
    private SharedPreferences mShPf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        findViewById(R.id.buttonTakePicture).setOnClickListener(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_camera_alert, null, false);
        final CheckBox vCheckbox = (CheckBox) view.findViewById(R.id.cb_preference);


        //다시보기 sharedpreference 가져오기
        mShPf = getSharedPreferences("setting",MODE_PRIVATE);


        //mShPf 다시보기 설정 default: true true-> 띄워줌 false->안띄워줌

        if(mShPf.getBoolean("review",true)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialog);
            builder.setView(view)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(vCheckbox.isChecked()){
                                mShPf.edit().putBoolean("review",false)
                                        .apply();

                            }
                            dialog.dismiss();
                        }
                    }).show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        mCamera = getCamera();
        if (mCamera == null) {
            Toast.makeText(getContext(), "카메라를 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        setCameraView();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ViewGroup) findViewById(R.id.layoutCamera)).removeAllViews();
    }

    private Camera getCamera() {
        try {
            return Camera.open();
        } catch (Exception e) {
            return null;
        }
    }


    private void setCameraView() {
        ViewGroup mLayoutCamera = (ViewGroup) findViewById(R.id.layoutCamera);
        if (mLayoutCamera != null) {
            mLayoutCamera.removeAllViews();
            mLayoutCamera.addView(new BodyEndCameraSurfaceView(getContext(), mCamera));
        }
    }


    private Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTakePicture:
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        File save = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BodyEnd_" + System.currentTimeMillis());
                        try {
                            Bitmap bitmapOrg = BitmapFactory.decodeByteArray(data, 0, data.length);

                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(save));
                            Matrix matrix = new Matrix();

                            if (mCameraFace == Camera.CameraInfo.CAMERA_FACING_BACK) {
                                matrix.postRotate(90);
                            } else {
                                matrix.postRotate(270);
                            }
                            Bitmap rotate = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
                            rotate.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.close();

                            Intent intent = new Intent();
                            intent.putExtra(FLAG_FILE_PATH, save.getAbsolutePath());
                            setResult(RESULT_OK, intent);
                            Toast.makeText(getContext(), save.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            setResult(RESULT_CANCELED);
                        }

                        finish();

                    }
                });
                break;


        }

    }


    private class BodyEndCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener, Camera.AutoFocusCallback {

        private Camera mCamera;
        private SurfaceHolder mSurfaceHolder;

        public BodyEndCameraSurfaceView(Context context, Camera mCamera) {
            super(context);

            this.mCamera = mCamera;
            Camera.Parameters parameters = mCamera.getParameters();

            this.mSurfaceHolder = getHolder();
            this.mSurfaceHolder.addCallback(this);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
                this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                Camera.Parameters mParameters = this.mCamera.getParameters();
                mParameters.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
                mParameters.setRotation(90);

                List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
                Camera.Size bestSize = sizeList.get(0);
                for (int i = 1; i < sizeList.size(); i++) {
                    if ((sizeList.get(i).width * sizeList.get(i).height) >
                            (bestSize.width * bestSize.height)) {
                        bestSize = sizeList.get(i);
                    }
                }
                mParameters.setPreviewSize(bestSize.width, bestSize.height);
                holder.setFixedSize(bestSize.width, bestSize.height);
                this.mCamera.setParameters(mParameters);
                this.mCamera.setPreviewDisplay(mSurfaceHolder);
                this.mCamera.startPreview();
                setOnClickListener(this);
            } catch (IOException e) {
                //에러시 처리할것.
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (this.mSurfaceHolder.getSurface() == null) {
                return;
            }

            try {
                this.mCamera.stopPreview();
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = getBestPreviewSize(width, height);
                parameters.setPreviewSize(size.width, size.height);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                //에러시 처리
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (this.mCamera != null) {
                this.mCamera.stopPreview();
                this.mCamera.release();
            }
        }


        private Camera.Size getBestPreviewSize(int width, int height) {
            Camera.Size result = null;
            Camera.Parameters p = mCamera.getParameters();
            for (Camera.Size size : p.getSupportedPreviewSizes()) {
                if (size.width <= width && size.height <= height) {
                    if (result == null) {
                        result = size;
                    } else {
                        int resultArea = result.width * result.height;
                        int newArea = size.width * size.height;

                        if (newArea > resultArea) {
                            result = size;
                        }
                    }
                }
            }
            return result;
        }


        @Override
        public void onClick(View v) {
            if (v == this) {
                this.mCamera.autoFocus(this);
            }
        }

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //일단 정지
        }
    }
}
