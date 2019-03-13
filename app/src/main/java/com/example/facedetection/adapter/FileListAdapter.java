package com.example.facedetection.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;

import java.util.List;

/**
 * Created by Administrator on 2019/3/13.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<String> fileList;
    private LayoutInflater inflater;
    private Context mContext;

    public FileListAdapter(List<String> fileList,Context mContext) {
        this.fileList = fileList;
        this.inflater = inflater;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textView.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(fileList.get(i)).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return fileList != null ? fileList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_txt);
            imageView = itemView.findViewById(R.id.be_similar_icon);
        }
    }
}
