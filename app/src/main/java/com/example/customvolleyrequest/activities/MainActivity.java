package com.example.customvolleyrequest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.customvolleyrequest.R;
import com.example.customvolleyrequest.beans.MyDataResult;
import com.example.customvolleyrequest.network.CustomCommonVolleyRequestBuilder;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mResultText;
    private ProgressBar mLoadingProgress;

    private CustomCommonVolleyRequestBuilder<String> mGetStringCustomCommonVolleyRequest;
    private CustomCommonVolleyRequestBuilder<MyDataResult> mGetMyDataResultCustomCommonVolleyRequestBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mResultText = (TextView) this.findViewById(R.id.text_result);
        this.mLoadingProgress = (ProgressBar) this.findViewById(R.id.progress_loading);

        this.invokeGetStringCustomCommonVolleyRequest();
        this.invokeGetMyDataResultCustomCommonVolleyRequestBuilder();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.mGetStringCustomCommonVolleyRequest != null) {
            this.mGetStringCustomCommonVolleyRequest.cancelAll();
        }

        if (this.mGetMyDataResultCustomCommonVolleyRequestBuilder != null) {
            this.mGetMyDataResultCustomCommonVolleyRequestBuilder.cancelAll();
        }
    }

    private void invokeGetStringCustomCommonVolleyRequest() {
        String testUrl = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire";
        Map<String, String> params = new HashMap<>();
        params.put("rid", "bf073841-c734-49bf-a97f-3757a6013812");
        params.put("limit", "50");

        this.mGetStringCustomCommonVolleyRequest = new CustomCommonVolleyRequestBuilder<String>(
                this,
                Request.Method.GET,
                CustomCommonVolleyRequestBuilder.encodingQueryURLString(testUrl, params),
                String.class) {
            @Override
            public void preExecute() {
                super.preExecute();
                mLoadingProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onApiSuccess(String response) {
                Log.d(MainActivity.class.getName(), response);
                mResultText.setText(response);
            }

            @Override
            public void postSuccessExecute() {
                super.postSuccessExecute();
                mLoadingProgress.setVisibility(View.GONE);
            }

            @Override
            public void onApiError(VolleyError error) {
                super.onApiError(error);
                Log.e(MainActivity.class.getName(), error.getMessage());
            }

            @Override
            public void postErrorExecute() {
                super.postErrorExecute();
                mLoadingProgress.setVisibility(View.GONE);
            }
        };
        this.mGetStringCustomCommonVolleyRequest.addQueue();
    }

    private void invokeGetMyDataResultCustomCommonVolleyRequestBuilder() {
        String testUrl = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire";
        Map<String, String> params = new HashMap<>();
        params.put("rid", "bf073841-c734-49bf-a97f-3757a6013812");
        params.put("limit", "10");

        this.mGetMyDataResultCustomCommonVolleyRequestBuilder = new CustomCommonVolleyRequestBuilder<MyDataResult>(
                this,
                Request.Method.GET,
                CustomCommonVolleyRequestBuilder.encodingQueryURLString(testUrl, params),
                MyDataResult.class) {
            @Override
            public void onApiSuccess(MyDataResult response) {
                Log.d(MainActivity.class.getName(), "Size: " + response.getResult().getResults().size());
            }
        };
        this.mGetMyDataResultCustomCommonVolleyRequestBuilder.addQueue();
    }
}
