package com.example.customvolleyrequest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.customvolleyrequest.R;
import com.example.customvolleyrequest.network.CustomCommonVolleyRequestBuilder;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
	private TextView mResultText;

	private CustomCommonVolleyRequestBuilder<String> mGetStringCustomCommonVolleyRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.mResultText = (TextView) this.findViewById(R.id.text_result);
		this.invokeGetStringCustomCommonVolleyRequest();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (this.mGetStringCustomCommonVolleyRequest != null) {
			this.mGetStringCustomCommonVolleyRequest.cancelAll();
		}
	}

	private void invokeGetStringCustomCommonVolleyRequest() {
		String testUrl = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire";
		Map<String, String> params = new HashMap<>();
		params.put("rid", "bf073841-c734-49bf-a97f-3757a6013812");
		params.put("limit", "10");

		this.mGetStringCustomCommonVolleyRequest = new CustomCommonVolleyRequestBuilder<String>(
				this,
				Request.Method.GET,
				CustomCommonVolleyRequestBuilder.encodingQueryURLString(testUrl, params),
				String.class) {
			@Override
			public void onApiSuccess(String response) {
				Log.d(MainActivity.class.getName(), response);
				mResultText.setText(response);
			}

			@Override
			public void onApiError(VolleyError error) {
				super.onApiError(error);
				Log.e(MainActivity.class.getName(), error.getMessage());
			}
		};
		this.mGetStringCustomCommonVolleyRequest.addQueue();
	}
}
