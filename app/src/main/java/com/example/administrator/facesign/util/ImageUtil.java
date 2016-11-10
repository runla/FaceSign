package com.example.administrator.facesign.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.administrator.facesign.activity.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/8.
 */
public class ImageUtil {

    public static boolean saveBitmap(Context context,Bitmap mybitmap,String studentid) {
        Log.d(BaseActivity.TAG,"saveBitmap");
        boolean result = false;
        String urlPath = getPersonalImagePath(studentid);
        File file = new File(urlPath);
  //      if (!file.exists()) {
            Log.d(BaseActivity.TAG,"file is no exist");
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    mybitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    //update gallery
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    result = true;
                } else {
//                    Toast.makeText(MainActivity.this, "不能读取到SD卡", Toast.LENGTH_SHORT).show();
                    result = false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
      //  }
        return false;
    }

    /**
     * 个人图片保存路径
     * @param studentid
     * @return
     */
    public static String getPersonalImagePath(String studentid){
        String path = Environment.getExternalStorageDirectory() + "/aImage/";
        path += studentid+".jpg";
        return path;
    }
    /**
     * 获取拍照后图片保存的路径
     * @return
     */
    public static String getSaveImagePath(){
        String path = Environment.getExternalStorageDirectory() + "/aImage/";
        String fileName = String.valueOf(System.currentTimeMillis())+".jpg";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        path += fileName;
        return  path;
    }

    /**
     * 刷新手机图库
     * @param context
     * @param file
     */
    public static void refreshGallery(Context context,File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 压缩图片
     * @param Path
     * @return
     */
    public static byte[] imageProcessing(String Path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap img = BitmapFactory.decodeFile(Path, options);
        options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max((double) options.outWidth / 1024f,(double) options.outHeight / 1024f)));

        options.inJustDecodeBounds = false;
        img = BitmapFactory.decodeFile(Path, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        float scale = Math.min(1,Math.min(600f / img.getWidth(), 600f / img.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(),img.getHeight(), matrix, false);
        imgSmall.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] array = stream.toByteArray();
        return array;
    }
}
