package com.example.facedetection.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;
import com.example.facedetection.base.BaseActivity;

import java.io.File;

/**
 * 新建
 */
public class NewlyBuildActivity extends BaseActivity {

    private EditText ed_name;
    private String path;

    @Override
    public int getLayoutId() {
        return R.layout.activity_newly;
    }

    @Override
    public void onLoad() {
        ImageView icon = findViewById(R.id.newly_icon);
        ed_name = findViewById(R.id.ed_name);
        Intent intent = getIntent();
        path = intent.getStringExtra("url");
        if (null == path) {
            return;
        }
        Glide.with(this).load(path).into(icon);
    }

    //保存
    public void saveOnClick(View v) {
        String name = ed_name.getText().toString().trim();
        if ("".equals(name)) {
            Toast.makeText(this, R.string.ed_name_tips, Toast.LENGTH_SHORT).show();
            return;
        }
        String[] sp = path.split("camera2/");
        String newsName = sp[0] + "camera2/" + name + ".jpg";
        File oldFile = new File(path);
        boolean b = oldFile.renameTo(new File(newsName));
        if (b) {
            Toast.makeText(this, "文件夹已存在", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FileListActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
