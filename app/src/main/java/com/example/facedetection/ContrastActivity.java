package com.example.facedetection;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.facedetection.base.BaseActivity;
import com.example.facedetection.bean.PictureAddress;
import java.util.ArrayList;
import java.util.List;

public class ContrastActivity extends BaseActivity {


    private ImageView take_icon;
    private RecyclerView recycler;
    private List<String> urlList = new ArrayList<>();
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
        recycler.setLayoutManager(layoutManager);
        ergodicImage();

        // recycler.setAdapter(new PictureListAdapter(this, urlList));
    }

    /**
     * 遍历指定文件夹
     */
    public void ergodicImage() {
        String[] split = url.split("camera2");
        List<PictureAddress> imageList = Util.getAllFiles(split[0] + "camera2", "jpg");
        if (imageList == null || imageList.size() == 0) {
            return;
        }
        for (int i = 0; i < imageList.size(); i++) {

        }
    }

}
