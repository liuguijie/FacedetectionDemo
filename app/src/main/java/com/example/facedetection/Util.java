package com.example.facedetection;

import android.app.Activity;
import android.graphics.Point;

public class Util {
    //获取屏幕的宽度
    public static int getScreenWidth(Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }
}
