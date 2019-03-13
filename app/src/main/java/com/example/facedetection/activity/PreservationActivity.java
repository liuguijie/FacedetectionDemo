package com.example.facedetection.activity;

import android.content.Intent;

import com.example.facedetection.R;
import com.example.facedetection.base.BaseActivity;

/**
 * 保存
 */
public class PreservationActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_preservation;
    }

    @Override
    public void onLoad() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("url");

    }
}
