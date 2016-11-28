package com.example.administrator.facesign.vollery;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2016/11/16.
 */
public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache{

    //获取图片缓存的最大内存，在这里我们设置为手机APP允许运行最大内存的八分之一
    public static int getDefaultLruCacheSize(){
        final int maxMemorySize = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemorySize / 8;
        return  cacheSize;
    }

    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {

    }
}
