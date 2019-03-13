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
import com.example.facedetection.BitmapUtils;
import com.example.facedetection.R;
import com.example.facedetection.Util;
import com.example.facedetection.adapter.PictureListAdapter;
import com.example.facedetection.base.BaseActivity;
import com.example.facedetection.bean.PictureAddress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.megvii.facepp.api.FacePPApi;
import com.megvii.facepp.api.IFacePPCallBack;
import com.megvii.facepp.api.bean.CompareResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.chrono.JapaneseDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
        Util.showProgressDlg("识别对比中","识别",this);
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
                        recycler.setAdapter(new PictureListAdapter(ContrastActivity.this, thanList));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preservation:
                start(FileListActivity.class);
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
    }

    private static final String KEY_IMAGE_FILE = "image_file";
    public static final String KEY_IMAGE_FILE_1 = "image_file1";
    public static final String KEY_IMAGE_FILE_2 = "image_file2";

    public static final String KEY_TEMPLATE_FILE = "template_file";
    public static final String KEY_MERGE_FILE = "merge_file";

    /**
     * 构建请求参数
     */
    private static RequestBody buildRequestBody(final Map<String, String> params, Map<String, byte[]> filePath1, Map<String, byte[]> filePath2) throws Exception {
        // 普通参数
        final MultipartBody.Builder bodyBuilder = addNormalParams(params);

        // 文件参数
        addFileParamsByKey(filePath1, bodyBuilder);
        addFileParamsByKey(filePath2, bodyBuilder);

        return bodyBuilder.build();
    }

    /**
     * 普通参数
     */
    private static MultipartBody.Builder addNormalParams(final Map<String, String> params) {

        final MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // 普通参数
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            bodyBuilder.addFormDataPart(key, value);
        }
        return bodyBuilder;
    }

    private static void addFileParamsByKey(Map<String, byte[]> filePath, MultipartBody.Builder bodyBuilder) throws Exception {
        if (null != filePath) {
            getDataByKey(filePath, KEY_IMAGE_FILE, bodyBuilder);
            getDataByKey(filePath, KEY_IMAGE_FILE_1, bodyBuilder);
            getDataByKey(filePath, KEY_IMAGE_FILE_2, bodyBuilder);
            getDataByKey(filePath, KEY_TEMPLATE_FILE, bodyBuilder);
            getDataByKey(filePath, KEY_MERGE_FILE, bodyBuilder);
        }
    }

    private static void getDataByKey(Map<String, byte[]> file, String key, MultipartBody.Builder bodyBuilder) throws Exception {
        if (!isEmpty(file.get(key))) {
            addFileParams(bodyBuilder, key, file.get(key));
        }
    }

    private static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    private static void addFileParams(MultipartBody.Builder bodyBuilder, String key, byte[] file) throws Exception {
        bodyBuilder.addFormDataPart(key, key, RequestBody.create(MediaType.parse("application/octet-stream"), file));
    }
}
