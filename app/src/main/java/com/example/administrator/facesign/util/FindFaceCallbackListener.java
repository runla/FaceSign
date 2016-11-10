package com.example.administrator.facesign.util;
import android.graphics.Bitmap;
/**
 * Created by Administrator on 2016/11/9.
 */
public interface FindFaceCallbackListener {
    void listenFaceNum(int num);
    void getBitmap(Bitmap bitmap);
}
