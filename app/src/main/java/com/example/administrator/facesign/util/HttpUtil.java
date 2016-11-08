package com.example.administrator.facesign.util;

/**
 * 創建
 * 创建人：陈坚润
 * 时间：2016-10-29
 * 功能：连接网络服务器，获取网上数据
 * getHttpToByteArray是用來返回一個byte數組的，一般我是想用來獲取網上的圖片的
 * getHttpToString是用來獲取網上數據最後以一個String類型的數據返回
 */

import android.util.Log;

import com.example.administrator.facesign.activity.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	/**
	 * 功能：连接网络服务器，获取网上数据,我是用來獲取網上圖片的
	 *
	 *
	 */
	public static void getHttpToByteArray(final String urlPath,final HttpCallbackListener listener){

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection=null;
				try {
					URL url = new URL(urlPath);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					Log.d(BaseActivity.TAG,urlPath);
					if (connection.getResponseCode() == 200) {
						Log.d(BaseActivity.TAG,"error---------1");

						InputStream inputStream = connection.getInputStream();
						byte[] data = readStream(inputStream).toByteArray();
						inputStream.close();
						listener.onFinishTypeArray(data);
					}

					Log.d(BaseActivity.TAG,"error---------");



				} catch (IOException e) {
					listener.onError(e);
				}
				finally {
					if (connection != null) {


					}
				}
			}
		}).start();


	}

	/**
	 * 创建人：陈坚润
	 * 时间：2016-10-29
	 * 功能：连接网络服务器，获取网上数据
	 * 参数：URL地址
	 * 返回：string
	 *
	 */
	public static void getHttpToString(final String urlPath,final HttpCallbackListener listener){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(urlPath);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setRequestProperty("Content-type", "text/html");
					connection.setRequestProperty("Accept-Charset", "utf-8");
					connection.setRequestProperty("contentType", "utf-8");
					if (connection.getResponseCode() == 200) {
						InputStream inputStream = connection.getInputStream();
						String str1 = new String(readStream(inputStream).toByteArray(),"UTF-8");

						listener.onFinishStr(str1);
					}


				} catch (IOException e) {
					listener.onError(e);
				}
				finally {

				}
			}
		}).start();
	}

	private static ByteArrayOutputStream readStream(InputStream inputStream){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = inputStream.read(buffer))!= -1){
				out.write(buffer,0,len);
				Log.d(BaseActivity.TAG,"+++++++++"+len);
			}
			out.close();
			inputStream.close();
			return out;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
