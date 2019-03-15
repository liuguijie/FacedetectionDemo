package com.example.facedetection.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;
import com.example.facedetection.util.Util;
import com.example.facedetection.adapter.PictureListAdapter;
import com.example.facedetection.base.BaseActivity;
import com.example.facedetection.bean.PictureAddress;
import com.megvii.facepp.api.FacePPApi;
import com.megvii.facepp.api.IFacePPCallBack;
import com.megvii.facepp.api.bean.CompareResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 上个页面拍照点击继续跳转此页面会卡住几秒才会跳转，按理因该是页面先出来再执行网络请求，现在没显示页面就执行了
 * 写在onResume里也不好使
 */
public class ContrastActivity extends BaseActivity implements View.OnClickListener {


    private ImageView take_icon;
    private RecyclerView recycler;
    private List<String> thanList = new ArrayList<>();
    private Button preservation;
    private Button newly_build;
    private String url;
    private int flag = 0;
    private List<byte[]> imageUrl2List = new ArrayList<>();
    private List<String> pathList = new ArrayList<>();
    private byte[] data1;
    private PictureListAdapter adapter;
    private String clickUrl = null;
    private Context ctx;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contrast;
    }

    @Override
    public void onLoad() {
        ctx = ContrastActivity.this;
        findViewById(R.id.clude_icon).setVisibility(View.GONE);
        TextView title = findViewById(R.id.clude_title);
        preservation = findViewById(R.id.preservation);
        newly_build = findViewById(R.id.newly_build);
        take_icon = findViewById(R.id.take_image);
        recycler = findViewById(R.id.recycler);
        title.setText(R.string.tv_portfollo);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if (url == null) {
            return;
        }
        Glide.with(this).load(url).into(take_icon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(layoutManager);
        preservation.setOnClickListener(this);
        newly_build.setOnClickListener(this);
        ergodicImage();
    }

    /**
     * 遍历对比
     */
    public void ergodicImage() {
        String[] split = url.split("camera2");
        List<PictureAddress> imageList = Util.getAllFiles(split[0] + "camera3", ".jpg");
        if (imageList == null || imageList.size() == 0) {
            return;
        }
        data1 = Util.compressIma(url);
        //遍历识别
        for (int i = 0; i < imageList.size(); i++) {
            String str = imageList.get(i).getPath();
            byte[] data2 = Util.File2byte(str);
            imageUrl2List.add(data2);
            pathList.add(str);

        }
        Util.showProgressDlg("识别对比中", "识别", this);
        compare(flag);

    }

    //对比
    public void compare(final int i) {
        byte[] da2 = imageUrl2List.get(i);
        FacePPApi facePPApi = new FacePPApi("IzPz7W9NNprFzJvOlA8g-BHFjfhJZPNC", "iguAgm6pLRAOUqCmenagPcO3qCy5fI_I");
        facePPApi.compare(new HashMap<String, String>(), data1, da2, new IFacePPCallBack<CompareResponse>() {
            @Override
            public void onSuccess(CompareResponse compareResponse) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(compareResponse.toString());
                    String confidence = jsonObject.getString("confidence");
                    if (Double.valueOf(confidence) >= 80) {
                        thanList.add(pathList.get(i));
                    }
                    flag++;
                    if (flag == imageUrl2List.size()) {
                        Util.stopProgressDlg();
                        adapter = new PictureListAdapter(ContrastActivity.this, thanList);
                        recycler.setAdapter(adapter);
                        adapter.setOnClickListener(new PictureListAdapter.ItemOnClickInface() {
                            @Override
                            public void onClick(int position) {
                                clickUrl = thanList.get(position);
                            }
                        });
                    } else {
                        compare(flag);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String s) {
                Log.e("ERROR", s);
            }
        });
    }

    /**
     * 复制图片
     *
     * @param path
     * @param fileName
     * @return
     */
    public void copy(String path, File fileName) {
        //创建文件
        File newsFile = new File(fileName.getParentFile().getAbsoluteFile(), System.currentTimeMillis() + ".jpg");
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
                Intent intent = new Intent(ctx, FileListActivity.class);
                intent.putExtra("path", newsFile.getParentFile().getAbsolutePath());
                startActivity(intent);
                finish();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ContrastActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preservation:
                if (clickUrl == null) {
                    Toast.makeText(ctx, R.string.tv_itm_tips, Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File parentFile = new File(clickUrl);
                        copy(url, parentFile);
                    }
                }).start();
                break;
            case R.id.newly_build:
                start(NewlyBuildActivity.class);
                break;
        }

    }

    public void start(Class c) {
        Intent intent = new Intent(this, c);
        intent.putExtra("url", url);
        startActivity(intent);
        fileList();
    }
}
