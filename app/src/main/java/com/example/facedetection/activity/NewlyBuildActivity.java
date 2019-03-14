package com.example.facedetection.activity;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;
import com.example.facedetection.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 新建
 */
public class NewlyBuildActivity extends BaseActivity {

    private EditText ed_name;
    private String path;
    private String camera3Path = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_newly;
    }

    @Override
    public void onLoad() {
        findViewById(R.id.clude_icon).setVisibility(View.GONE);
        TextView title = findViewById(R.id.clude_title);
        ImageView icon = findViewById(R.id.newly_icon);
        ed_name = findViewById(R.id.ed_name);
        title.setText(R.string.news);
        Intent intent = getIntent();
        path = intent.getStringExtra("url");
        if (null == path) {
            return;
        }
        Glide.with(this).load(path).into(icon);

    }

    //保存
    public void saveOnClick(View v) {
        final String name = ed_name.getText().toString().trim();
        if ("".equals(name)) {
            Toast.makeText(this, R.string.ed_name_tips, Toast.LENGTH_SHORT).show();
            return;
        }
        final File file = new File(Environment.getExternalStorageDirectory() + "/camera2");
        new Thread() {
            @Override
            public void run() {
                super.run();
                File file1 = new File(file, name);
                boolean b = copyAndDelete(file1);
                if (b) {
                    Intent intent = new Intent(NewlyBuildActivity.this, FileListActivity.class);
                    intent.putExtra("path", file1.getAbsolutePath());
                    startActivity(intent);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewlyBuildActivity.this, "文件夹已存在", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();

    }

    /**
     * 复制
     * @param name
     * @return
     */
    public boolean copyAndDelete(File name) {
        boolean copy = false;
        int end;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        File file = new File(path);
        try {
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(name);
            byte[] by = new byte[2048];
            while ((end = inputStream.read(by)) != -1) {
                outputStream.write(by, 0, end);
                copy = true;
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            //删除原来图片
            if (copy && file.delete())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
