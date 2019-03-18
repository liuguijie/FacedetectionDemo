package com.example.facedetection.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.facedetection.util.Util;

import java.io.ByteArrayOutputStream;
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
    private File newsFile;

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
        new Thread() {
            @Override
            public void run() {
                super.run();
                //创建路径
                File file = new File(Environment.getExternalStorageDirectory() + "/camera2/" + name);
                if (!file.exists()) {
                    file.mkdirs();
                    copy(path, file);
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
     * 复制图片
     *
     * @param path
     * @param fileName
     * @return
     */
    public void copy(String path, File fileName) {
        String time = System.currentTimeMillis() + ".jpg";
        //创建文件
        newsFile = new File(fileName, time);
        File oldFile = new File(path);
        int length;
        boolean iscopy = false;
        byte[] by = new byte[2048];
        try {
            FileInputStream is = new FileInputStream(oldFile);
            FileOutputStream os = new FileOutputStream(newsFile);
            while ((length = is.read(by)) != -1) {
                os.write(by, 0, length);
                os.flush();
                iscopy = true;
            }
            is.close();
            os.close();
            //删除原文件
            boolean delete = oldFile.delete();
            if (delete && iscopy) {
                Intent intent = new Intent(NewlyBuildActivity.this, FileListActivity.class);
                intent.putExtra("path", newsFile.getParentFile().getAbsolutePath());
                startActivity(intent);
                finish();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
