package com.example.facedetection.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.facedetection.R;

public class CommomDialog extends Dialog implements View.OnClickListener {
    private OnClickListener mClickListener;
    private Context mContext;
    private String mContent;

    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId, OnClickListener clickListener) {
        super(context, themeResId);
        this.mContext = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(false);
        init();
    }

    private void init() {
        Button cancel = findViewById(R.id.dialog_cancel);
        Button again = findViewById(R.id.dialog_continue);
        cancel.setOnClickListener(this);
        again.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                if (mClickListener != null) {
                    mClickListener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.dialog_continue:
                if (mClickListener != null) {
                    mClickListener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, boolean again);
    }
}
