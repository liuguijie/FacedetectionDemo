package com.example.facedetection.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;
import com.example.facedetection.bean.PictureAddress;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Created by Administrator on 2019/3/13.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<PictureAddress> fileList;
    private LayoutInflater inflater;
    private Context mContext;
    private ItemOnClick itemOnClick;

    public FileListAdapter(List<PictureAddress> fileList, Context mContext) {
        this.fileList = fileList;
        inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    public void setItemOnClickListener(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    @NonNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        if (itemOnClick != null) {
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileList != null ? fileList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PhotoView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_txt);
            imageView = itemView.findViewById(R.id.be_similar_icon);
        }
    }

    public interface ItemOnClick {
        void onClickListener();
    }
}
