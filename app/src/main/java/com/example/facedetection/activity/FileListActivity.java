package com.example.facedetection.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.facedetection.R;
import com.example.facedetection.util.Util;
import com.example.facedetection.adapter.FileListAdapter;
import com.example.facedetection.base.BaseActivity;
import com.example.facedetection.bean.PictureAddress;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/13.
 */

public class FileListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private FileListAdapter adapter;
    private List<PictureAddress> fileList;
    private TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_file_list;
    }

    @Override
    public void onLoad() {
        fileList = new ArrayList<>();
        findViewById(R.id.clude_icon).setVisibility(View.GONE);
        TextView title = findViewById(R.id.clude_title);
        recyclerView = findViewById(R.id.recycler_file);
        smartRefreshLayout = findViewById(R.id.smart);
        textView = findViewById(R.id.text_no_data);
        title.setText(R.string.tv_portfollo);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        fileList = Util.getAllFiles(path, ".jpg");

        if (fileList != null && fileList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            return;
        }

        adapter = new FileListAdapter(fileList, this);
        recyclerView.setAdapter(adapter);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
            }
        });
        adapter.setItemOnClickListener(new FileListAdapter.ItemOnClick() {
            @Override
            public void onClickListener() {

            }
        });
    }
}
