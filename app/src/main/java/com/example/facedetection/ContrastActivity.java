package com.example.facedetection;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.facedetection.base.BaseActivity;

public class ContrastActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_contrast;
    }
    @Override
    public void onLoad() {
        findViewById(R.id.clude_icon).setVisibility(View.GONE);
        TextView title = findViewById(R.id.clude_title);
        title.setText(R.string.tv_portfollo);

        Intent intent = getIntent();

    }

}
