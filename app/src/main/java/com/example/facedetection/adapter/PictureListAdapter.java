package com.example.facedetection.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import com.bumptech.glide.Glide;
import com.example.facedetection.R;

import java.util.List;

public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.MyViewHolder> {
    private List<String> mIcons;
    private LayoutInflater inflater;
    private Context mContext;
    private ItemOnClickInface mOnClickInface;

    public PictureListAdapter(Context context, List<String> icons) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mIcons = icons;
    }

    public void setOnClickListener(ItemOnClickInface onClickInface) {
        this.mOnClickInface = onClickInface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_view, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        Glide.with(mContext).load(mIcons.get(i)).into(viewHolder.beSimilar);
        viewHolder.beSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickInface.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIcons != null ? mIcons.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView beSimilar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            beSimilar = itemView.findViewById(R.id.be_similar_icon);
        }
    }

    public interface ItemOnClickInface {
        void onClick(int position);
    }
}
