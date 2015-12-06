# 客製化Volley網路請求 (Custom Volley Reqeuest)
## 使用方式 (How to use):    
    1. 產生一個"CustomCommonVolleyRequestBuilder"物件 (Create a "CustomCommonVolleyRequestBuilder object)
    2. 實作onApiSuccess (Implement "onApiSuccess")
    3. 將該物件加入佇列 (Add the request into the volley queue) 
    
## 建構函式參數 (Constructer):
    1. Context
    2. Request method
    3. URL
    4. Class Type

## 其他用法 (Optional items)
    1. preExecute(): 用於送出請求前的前置行為 (Actions before sending a request)
    2. postSuccessExecute(): 用於請求成功，並回傳成功後的行為 (Actions after sending a request, and returning a successful result)
    3. onApiError(VolleyError error): 用於請求失敗，並回傳錯誤結果 (An Volley Error after sending a request)
    4. postErrorExecute(): 用於請求失敗，並回傳失敗後的行為 (Actions after sending a request, and returning a fail result)
    5. withTimeOutSec(int timeOutSec): 設定該請求的逾時秒數 (Set time out)
    6. withRetryTimes(int retryTimes): 設定該請求的重送次數 (Set retry times)
    7. withAuth(): 設定驗證方式 (Set authorization)
    8. cancelRequest(): 取消請求 (Cancel the request)
    9. cancelAll(): 取消全部請求 (Cancel all requests)
    
## 使用範例 (Sample):
```
String testUrl = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire";
Map<String, String> params = new HashMap<>();
params.put("rid", "bf073841-c734-49bf-a97f-3757a6013812");
params.put("limit", "50");

CustomCommonVolleyRequestBuilder<MyDataResult> mGetStringCustomCommonVolleyRequest = new CustomCommonVolleyRequestBuilder<MyDataResult>(this, Request.Method.GET, CustomCommonVolleyRequestBuilder.encodingQueryURLString(testUrl, params), MyDataResult.class) {
			@Override
      public void preExecute() {
      		super.preExecute();
          mLoadingProgress.setVisibility(View.VISIBLE);
      }

      @Override
      public void onApiSuccess(MyDataResult response) {
          Log.d(MainActivity.class.getName(), "Size: " + response.getResult().getResults().size());
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
```
