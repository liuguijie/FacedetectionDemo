package com.example.facedetection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        ImageView default_icon = findViewById(R.id.main_default_icon);
        //宽度为屏幕的一半，高度自适应
        int width = Util.getScreenWidth(this);
        ViewGroup.LayoutParams lp = default_icon.getLayoutParams();
        lp.width = width / 2;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        default_icon.setLayoutParams(lp);
        default_icon.setMaxWidth(width / 2);
        default_icon.setMaxHeight(width * 2);
    }

    /**
     * 开始按钮监听
     *
     * @param v
     */
    public void startOnclick(View v) {

    }
}
