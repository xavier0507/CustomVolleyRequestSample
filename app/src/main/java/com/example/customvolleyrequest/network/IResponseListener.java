package com.example.customvolleyrequest.network;

import com.android.volley.VolleyError;

/**
 * Created by Xavier on 2015/11/26.
 */
public interface IResponseListener<T> {
	void preExecute();

	void onApiSuccess(T response);

	void postSuccessExecute();

	void onApiError(VolleyError error);

	void postErrorExecute();
}
