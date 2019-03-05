package com.example.facedetection;

import android.content.Intent;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.facedetection.base.BaseActivity;


//付智焱第一次提交测试
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
        int halfWidth = Util.getScreenWidth(this) / 2;
        lp.width = halfWidth;
        lp.height = halfWidth;
        default_icon.setLayoutParams(lp);
        default_icon.setImageResource(R.mipmap.ic_launcher);
    }

    /**
     * 开始按钮监听
     *
     * @param v
     */
    public void startOnclick(View v) {
        Intent intent = new Intent(this, ShotActivity.class);
        startActivity(intent);
    }
}
