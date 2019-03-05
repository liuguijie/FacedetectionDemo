package com.example.facedetection;

import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.example.facedetection.base.BaseActivity;

public class ShotActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView surfaceView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shot;
    }

    @Override
    public void onLoad() {
        ImageView camera = findViewById(R.id.camera);
        ImageView right_icon = findViewById(R.id.clude_icon);
        surfaceView = findViewById(R.id.surfaceView);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        holder.addCallback(this);
        camera.setOnClickListener(this);
        right_icon.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clude_icon://反转摄像头

                break;
            case R.id.camera://拍照

                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
