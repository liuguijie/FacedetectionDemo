package com.example.facedetection;

import android.view.View;
import android.widget.ImageView;

import com.example.facedetection.base.BaseActivity;

public class ShotActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_shot;
    }

    @Override
    public void onLoad() {
        ImageView right_icon = findViewById(R.id.clude_icon);
        right_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clude_icon:

                break;
        }
    }
}
