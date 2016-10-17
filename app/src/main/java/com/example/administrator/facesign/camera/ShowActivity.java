package com.example.administrator.facesign.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.facesign.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.*;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {

    private static final int Request_Camera=1;
    private Bitmap bitmap;
    private ImageView img_show;
    private RelativeLayout layout_load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        img_show = (ImageView) findViewById(R.id.img_show);
        layout_load = (RelativeLayout) findViewById(R.id.layout_load);
        //设置不可见
        layout_load.setVisibility(View.INVISIBLE);
        startCamera();

    }

    void startCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Request_Camera);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Camera && resultCode == Activity.RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap)bundle.get("data");
            img_show.setImageBitmap(bitmap);
            //saveBitmap(bitmap);
        }

        if (requestCode == Request_Camera && resultCode == Activity.RESULT_CANCELED && null != data) {
            finish();
        }
    }


    public void onClick_Cannel(View view){
        //finish();
        startCamera();
    }

    //开始识别
    public void onClick_upload(View view){
        //设置不可见
        layout_load.setVisibility(View.VISIBLE);
        if (bitmap!=null){
            saveBitmap(bitmap);
        }
        else{
            Toast.makeText(ShowActivity.this, "图片出错", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean saveBitmap(Bitmap mybitmap) {
        boolean result = false;

        String path = Environment.getExternalStorageDirectory() + "/Images/";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(path + fileName);
        if (!file.exists()) {
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
                    this.sendBroadcast(intent);
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
        }
        return false;
    }
    }
