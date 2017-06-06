package com.feicuiedu.eshop_20170518.network;

import com.feicuiedu.eshop_20170518.network.core.ApiInterface;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.core.UICallback;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 蔡传飞 on 2017-05-22.
 */

public class EShopClient {
    private static final String BASE_URL ="http://106.14.32.204/eshop/emobile/?url=" ;
    private static EShopClient mEShopClient;
    private final OkHttpClient mOkHttpClient;
    private Gson mGson;

   public static synchronized EShopClient getInstance(){
       if (mEShopClient == null) {
           mEShopClient=new EShopClient();
       }
       return mEShopClient;
    }
    private EShopClient(){
        mGson = new Gson();
        // 日志拦截器的创建
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpclient的初始化
        mOkHttpClient=new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }




    //同步方法：请求的构建(地址、方式、请求体)、数据的解析(具体解析的响应体的类型)都写到这个方法里面
    public <T extends ResponseEntity> T excute(ApiInterface apiInterface) throws IOException {
        // 构建请求
        Response response = newApiCall(apiInterface,null).execute();

        // 解析写到一个方法里面
        Class<T> clazz = (Class<T>) apiInterface.getResponseEntity();
        return getResponseEntity(response,clazz);
    }
    /** 异步回调的方法：请求的构建，数据的解析(callback里面完成)
     * 1. 构建请求
     * 2. 执行:异步回调的方法
     * 3. 在uicallback重写的方法中：处理请求成功与失败
     * 4. 在uicallback重写的方法中：请求得到的响应体的数据解析，最终得到响应实体类
     *
     * 在构建异步回调的时候：
     * 请求构建(地址、请求体)、
     * 数据的解析(callback中有response，没有具体的实体类数据，需要我们告诉callback要转换的类型)
     */
    public Call enqueue(ApiInterface apiInterface, UICallback uiCallback,String tag){

        //构建请求
        Call call = newApiCall(apiInterface,tag);
        //告诉uiCallback要转换的数据类型是什么
        uiCallback.setResponseType(apiInterface.getResponseEntity());
        //为了规范，在当前的方法中直接执行
        call.enqueue(uiCallback);
        return call;
    }

    // 根据响应Response，将响应体解析为响应的具体的实体类
    public  <T extends ResponseEntity> T getResponseEntity(Response response, Class<T> clazz) throws IOException {
        // 没有成功
        if (!response.isSuccessful()){
            throw new IOException("Response code is "+response.code());
        }
        //成功
        return mGson.fromJson(response.body().string(),clazz);
    }

    // 构建请求的方法
    private Call newApiCall(ApiInterface apiInterface,String tag) {

        // 请求拆开
        Request.Builder builder = new Request.Builder();

        builder.url(BASE_URL+apiInterface.getPath());// 请求地址

        // 如果请求体不为空，是post请求
        if (apiInterface.getRequestParam()!=null){
            // 构建post请求
            String json = mGson.toJson(apiInterface.getRequestParam());
            RequestBody requestBody = new FormBody.Builder()
                    .add("json",json)
                    .build();
            builder.post(requestBody);
        }
        builder.tag(tag);// 给请求设置tag，为了方便取消
        Request request = builder.build();
        return mOkHttpClient.newCall(request);
    }

    /**
     * 取消的方法：在请求的时候，通过欠条添加Tag,取消的时候判断来取消
     * 1.给请求设置tag
     * 2.取消的时候根据tag取消
     */
    public void cancelByTag(String tag){
        //获取调度器中等待执行和正在执行的call
        for (Call call :
                mOkHttpClient.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
        for (Call call:mOkHttpClient.dispatcher().runningCalls()){
            if (call.request().tag().equals(tag)){
                call.cancel();
            }
        }
    }
}
