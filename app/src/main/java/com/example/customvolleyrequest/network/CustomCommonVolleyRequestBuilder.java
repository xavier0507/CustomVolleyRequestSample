package com.example.customvolleyrequest.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xavier on 2015/11/26.
 */
public abstract class CustomCommonVolleyRequestBuilder<T> extends Request<T> implements IResponseListener<T> {
	protected final static int DEFAULT_TIME_OUT_INTERVAL = 5;
	protected final static int DEFAULT_RETRY_TIMES = 0;

	protected int timeOutMS;
	protected int retryTimes;

	protected Context context;
	protected Class<T> clazz;
	protected Gson gson = new Gson();
	protected Map<String, String> headers;
	protected VolleyUtil volleyUtil;

	public CustomCommonVolleyRequestBuilder(Context context, int method, String url, Class<T> clazz) {
		super(method, url, null);
		this.context = context;
		this.clazz = clazz;
		this.withTimeOutSec(DEFAULT_TIME_OUT_INTERVAL);
		this.withRetryTimes(DEFAULT_RETRY_TIMES);
		this.volleyUtil = VolleyUtil.getInstance(this.context);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return this.headers != null ? this.headers : super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
		this.onApiSuccess(response);
		this.postSuccessExecute();
	}

	@Override
	public void deliverError(VolleyError error) {
		this.onApiError(error);
		this.postErrorExecute();
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			if (String.class.isAssignableFrom(this.clazz)) {
				return Response.success((T) json, HttpHeaderParser.parseCacheHeaders(response));
			}

			return Response.success(this.gson.fromJson(json, this.clazz), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	public void preExecute() {
	}

	@Override
	public void postSuccessExecute() {
	}

	@Override
	public void onApiError(VolleyError error) {
	}

	@Override
	public void postErrorExecute() {
	}

	public static String encodingQueryURLString(String url, Map<String, String> params) {
		String queryString = "";

		if (params != null) {
			List<NameValuePair> getParams = new ArrayList<>();

			if (params.containsKey("")) {
				queryString = "&" + params.get("");
			} else {
				for (String key : params.keySet()) {
					getParams.add(new BasicNameValuePair(key, params.get(key)));
				}

				queryString = "&" + URLEncodedUtils.format(getParams, "UTF-8");
			}
		}

		return url + queryString;
	}

	public CustomCommonVolleyRequestBuilder withTimeOutSec(int timeOutSec) {
		this.timeOutMS = timeOutSec * 1000;
		return this;
	}

	public CustomCommonVolleyRequestBuilder withRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
		return this;
	}

	public CustomCommonVolleyRequestBuilder withAuth() {
		this.headers = new HashMap<>();
		// Put keys and values of your header
		// this.headers.put("YOUR_KEY1", "YOUR_VALUE1");
		// this.headers.put("YOUR_KEY2", "YOUR_VALUE2");
		return this;
	}

	public void addQueue() {
		this.preExecute();
		this.setRetryPolicy(new DefaultRetryPolicy(this.timeOutMS, this.retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		this.volleyUtil.addRequest(this);
	}

	public void cancelRequest() {
		this.cancel();
	}

	public void cancelAll() {
		this.volleyUtil.cancelRequest();
	}
}
