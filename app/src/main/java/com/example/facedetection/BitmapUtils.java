package com.example.facedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 图片压缩
 * Created by sdh on 2018/3/16.
 */

public class BitmapUtils {
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩

            baos.reset(); // 重置baos即清空baos

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

            options -= 10;// 每次都减少10

        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        return bitmap;

    }


    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */

    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;

        int w = newOpts.outWidth;

        int h = newOpts.outHeight;

        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为

        float hh = 1080f;// 这里设置高度为800f

        float ww = 720f;// 这里设置宽度为480f

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可

        int be = 1;// be=1表示不缩放

        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放

            be = (int) (newOpts.outWidth / ww);

        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放

            be = (int) (newOpts.outHeight / hh);

        }

        if (be <= 0)

            be = 1;

        newOpts.inSampleSize = be;// 设置缩放比例

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        Bitmap bitmap = ImageRotateUtil.rotateBitmapByDegree(BitmapFactory.decodeFile(srcPath, newOpts), ImageRotateUtil.getBitmapDegree(srcPath));
        return bitmap;// 压缩好比例大小后再进行质量压缩

    }


    public static void deleteDir() {
        File dir = new File("/sdcard/WeiShoot/catch");
        deleteDirWihtFile(dir);
    }

    //删除文件夹和文件夹里面的文件
    private static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static void creatCatchDirCatchDir() {
        File dir = new File("/sdcard/WeiShoot");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
    }

    /**
     * 将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
     *
     * @param filename
     * @param bit
     * @return
     */
    private static String saveMyBitmap(String filename, Bitmap bit) {
        creatCatchDirCatchDir();
        String filePath;
        File dir = new File("/sdcard/WeiShoot/catch");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        filePath = dir + "/" + filename;
        try {
            FileOutputStream fOut = new FileOutputStream(filePath);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return filePath;
    }

    /**
     * 压缩上传路径
     *
     * @param path
     * @return
     */
    public static String compressImageUpload(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);

        Bitmap image = getImage(path);
        return saveMyBitmap(filename, image);
    }
}
