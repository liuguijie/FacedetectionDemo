package com.example.facedetection.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.facedetection.R;
import com.example.facedetection.util.Util;
import com.example.facedetection.base.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 加载逻辑
     */
    @Override
    public void onLoad() {
        //图片宽高为屏幕宽度的一半
        ImageView default_icon = findViewById(R.id.main_default_icon);
        ViewGroup.LayoutParams lp = default_icon.getLayoutParams();
        int halfWidth = Util.getScreenWidth(this) /2;
        lp.width = halfWidth;
        lp.height = halfWidth;
        default_icon.setLayoutParams(lp);
    }

    /**
     * 开始按钮监听
     *
     * @param v
     */
    public void startOnclick(View v) {
        Intent intent = new Intent(this, MyCameraActivity.class);
        startActivity(intent);
    }
}
