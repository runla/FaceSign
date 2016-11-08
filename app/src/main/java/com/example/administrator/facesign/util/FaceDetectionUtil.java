package com.example.administrator.facesign.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/5.
 * 检测错人脸的位置，然后画出来
 */
public class FaceDetectionUtil {

    /**
     *
     * @param filePath
     * @return
     */
    public static Bitmap findFace(String filePath){
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
        byte[] array = ImageUtil.imageProcessing(filePath);//对图片进行压缩
        //获取图片资源
        Bitmap bitmap = BitmapFactory.decodeByteArray(array,0,array.length,bitmapOption).copy(Bitmap.Config.RGB_565, true);


        //假设最多有5张脸
        int numOfFaces = 5;
        FaceDetector mFaceDetector = new FaceDetector(bitmap.getWidth(),bitmap.getHeight(),numOfFaces);
        FaceDetector.Face[] mFace = new FaceDetector.Face[numOfFaces];

        //实际上有几张脸
        numOfFaces = mFaceDetector.findFaces(bitmap, mFace);

        Paint mPaint = new Paint();
        //画笔颜色
        mPaint.setColor(Color.GREEN);
        //画笔的样式是矩形框，不是矩形块
        mPaint.setStyle(Paint.Style.STROKE);
        //线宽
        mPaint.setStrokeWidth(2.0f);
        float eyesDistance = 0f;

        Canvas canvas = new Canvas(bitmap);
        for (int i = 0; i < numOfFaces; i++) {
            PointF eyeMidPoint = new PointF();
            //两眼的中点距离
            mFace[i].getMidPoint(eyeMidPoint);
            //两眼之间的距离
            eyesDistance = mFace[i].eyesDistance();

            //画矩形框
            canvas.drawRect((int)(eyeMidPoint.x-eyesDistance),
                    (int)(eyeMidPoint.y-eyesDistance),
                    (int)(eyeMidPoint.x+eyesDistance),
                    (int)(eyeMidPoint.y+eyesDistance),
                    mPaint);
        }
        return bitmap;
    }

    /**
     * 使用face++接口进行人脸识别
     * @param firstPath
     * @param secondPath
     * @return
     */
    public static Double facePlusPlusRecognitionCompare(String firstPath, String secondPath){

        //初始化
        HttpRequests httpRequests = new HttpRequests("14494aa3e3b1587c99d260a779c300b0","Rdgg9mNFpBDuXN3A9cZ_BItSjst0raLd",true, true);

        try {
            //获取第一张图片的信息
            byte[] array1 = ImageUtil.imageProcessing(firstPath);
            JSONObject result1 = httpRequests.detectionDetect(new PostParameters().setImg(array1));
            String face1 = result1.getJSONArray("face").getJSONObject(0).getString("face_id");

            //获取第二张图片的信息
            byte[] array2 = ImageUtil.imageProcessing(secondPath);
            JSONObject result2 = httpRequests.detectionDetect(new PostParameters().setImg(array2));
            String face2 = result2.getJSONArray("face").getJSONObject(0).getString("face_id");

            //对比两张人脸的相似程度
            JSONObject Compare = httpRequests.recognitionCompare(new PostParameters().setFaceId1(face1).setFaceId2(face2));
            final Double smilar = Double.valueOf(Compare.getString("similarity"));

            return smilar;

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FaceppParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1.1;
    }
}
