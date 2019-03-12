package com.example.facedetection.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;
import com.example.facedetection.Util;
import com.example.facedetection.adapter.PictureListAdapter;
import com.example.facedetection.base.BaseActivity;
import com.example.facedetection.bean.PictureAddress;
import com.example.facedetection.face.FaceApi;
import com.megvii.facepp.api.IFacePPCallBack;
import com.megvii.facepp.api.bean.CompareResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_contrast;
    }

    @Override
    public void onLoad() {
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

        preservation.setOnClickListener(this);
        newly_build.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(layoutManager);
        ergodicImage();
    }

    /**
     * 遍历对比
     */
    public void ergodicImage() {
        String[] split = url.split("camera2");
        List<PictureAddress> imageList = Util.getAllFiles(split[0] + "camera2", ".jpg");
        if (imageList == null || imageList.size() == 0) {
            return;
        }
        byte[] data1 = Util.compressIma(url);
        //遍历识别
        for (int i = imageList.size() - 1; i >= 0; i--) {
            String path = imageList.get(i).getPath();
            byte[] data2 = Util.compressIma(path);
            faceData(data1, data2, path);
        }
        recycler.setAdapter(new PictureListAdapter(this, thanList));

    }

    public void faceData(byte[] data1, byte[] data2, final String path) {
        FaceApi faceApi = new FaceApi();
        HashMap<String, String> params = new HashMap<>();
        params.put("api_key", "IzPz7W9NNprFzJvOlA8g-BHFjfhJZPNC");
        params.put("api_secret", "iguAgm6pLRAOUqCmenagPcO3qCy5fI_I");
        faceApi.compare(params, data1, data2, new IFacePPCallBack<CompareResponse>() {
            @Override
            public void onSuccess(CompareResponse compareResponse) {
                try {
                    JSONObject js = new JSONObject(compareResponse.toString());
                    String confidence = js.getString("confidence");//获取相似值
                    if (Double.valueOf(confidence) > 80) {
                        thanList.add(path);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preservation:
                start(PreservationActivity.class);
                break;
            case R.id.newly_build:
                start(NewlyBuildActivity.class);
                break;
        }

    }

    public void start(Class c) {
        Intent intent = new Intent(this, c);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
