package com.example.facedetection;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facedetection.adapter.PictureListAdapter;
import com.example.facedetection.base.BaseActivity;
import com.example.facedetection.bean.PictureAddress;
import com.example.facedetection.face.FacePPApi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.megvii.facepp.api.IFacePPCallBack;
import com.megvii.facepp.api.bean.CompareResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 上个页面拍照点击继续跳转此页面会卡住几秒才会跳转，按理因该是页面先出来再执行网络请求，现在没显示页面就执行了
 * 写在onResume里也不好使
 */
public class ContrastActivity extends BaseActivity {


    private ImageView take_icon;
    private RecyclerView recycler;
    private List<String> thanList = new ArrayList<>();
    private Button preservation;
    private Button newly_build;
    private String url;
    private int flag = 0, listSize;
    HashMap<String, String> hashMap = new HashMap<>();
    private List<String> imageUrl2List = new ArrayList<>();

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(layoutManager);
        ergodicImage();
    }

    String data1;

    /**
     * 遍历对比
     */
    public void ergodicImage() {
        flag = 0;
        String[] split = url.split("camera2");
        List<PictureAddress> imageList = Util.getAllFiles(split[0] + "camera2", "jpg");
        data1 = BitmapUtils.compressImageUpload(url);
        compare(flag);
        if (imageList == null || imageList.size() == 0) {
            return;
        }

        listSize = imageList.size();
        //遍历识别
        for (int i = 0; i < imageList.size(); i++) {
            String path = BitmapUtils.compressImageUpload(imageList.get(i).getPath());
            imageUrl2List.add(path);
//            faceData(data1, path, path);
            compare(flag);
        }
        recycler.setAdapter(new PictureListAdapter(this, thanList));

    }

    //对比
    public void compare(int i) {
        OkGo.<String>post("https://api-cn.faceplusplus.com/facepp/v3/compare")
                .tag(this)
                .isMultipart(true)
                .params("apiKey", "IzPz7W9NNprFzJvOlA8g-BHFjfhJZPNC")
                .params("apiSecret", "iguAgm6pLRAOUqCmenagPcO3qCy5fI_I")
                .params("image_url1", data1)
                .params("image_url2", "/sdcard/WeiShoot/catch/1552366386103.JPG")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            flag++;
                            compare(flag);
                        }
                    }
                });
    }

    public void faceData(String data1, String data2, final String path) {
        FacePPApi facePPApi = new FacePPApi("IzPz7W9NNprFzJvOlA8g-BHFjfhJZPNC", "iguAgm6pLRAOUqCmenagPcO3qCy5fI_I");
        facePPApi.compare(hashMap, new IFacePPCallBack<CompareResponse>() {
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
                try {
                    JSONObject js = new JSONObject(s);
                    String error = js.getString("error_message");
                    Log.e("ERROR", error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //第二种提交方式
//        HashMap<String, byte[]> map1 = new HashMap<>();
//        map1.put("image_file1", data1);
//        HashMap<String, byte[]> map2 = new HashMap<>();
//        map1.put("image_file2", data2);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("api_key", "IzPz7W9NNprFzJvOlA8g-BHFjfhJZPNC");
//        params.put("api_secret", "iguAgm6pLRAOUqCmenagPcO3qCy5fI_I");
//        HttpUtils.post("https://api-cn.faceplusplus.com/facepp/v3/compare", params, map1, map2, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.getMessage();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    ResponseBody body = response.body();
//                    if (body != null) {
//                        String result=body.string();
//                        JSONObject js = new JSONObject(result);
//                        String confidence = js.getString("confidence");//获取相似值
//                        if (Double.valueOf(confidence) > 80) {
//                            thanList.add(path);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

}
