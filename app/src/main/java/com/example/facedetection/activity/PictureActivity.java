package com.example.facedetection.activity;
import android.content.Intent;
import com.bumptech.glide.Glide;
import com.example.facedetection.R;
import com.example.facedetection.base.BaseActivity;
import com.github.chrisbanes.photoview.PhotoView;

public class PictureActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_pictrue;
    }

    @Override
    public void onLoad() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        PhotoView photoView = findViewById(R.id.photoImage);
        Glide.with(this).load(path).into(photoView);
    }
}
