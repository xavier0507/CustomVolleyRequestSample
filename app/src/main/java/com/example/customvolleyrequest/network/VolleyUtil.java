package com.example.customvolleyrequest.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Xavier on 2015/11/26.
 */
public class VolleyUtil {
	private final static VolleyUtil instance = new VolleyUtil();
	private static Context mContext;
	private static RequestQueue mQueue;

	private VolleyUtil() {
	}

	public static VolleyUtil getInstance(Context context) {
		mContext = context;
		mQueue = Volley.newRequestQueue(mContext);
		return instance;
	}

	public static void addRequest(Request request) {
		mQueue.add(request);
	}

	public static void cancelRequest() {
		mQueue.cancelAll(mContext);
	}
}
