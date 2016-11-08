package com.example.administrator.facesign.util;
//
public interface HttpCallbackListener {
	 void onFinishStr(String response);
	 void onFinishTypeArray(byte[] response);
	 void onError(Exception e);
}
