package com.example.facedetection.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.facedetection.R;
import com.example.facedetection.adapter.FileListAdapter;
import com.example.facedetection.base.BaseActivity;
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
    private List<String> fileList;
    private TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_file_list;
    }

    @Override
    public void onLoad() {
        fileList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_file);
        textView = findViewById(R.id.text_no_data);
        smartRefreshLayout = findViewById(R.id.smart);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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
        if (fileList != null && fileList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        }
    }
}
