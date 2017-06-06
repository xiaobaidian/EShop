package com.feicuiedu.eshop_20170518.network.core;

import android.os.Handler;
import android.os.Looper;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.utils.LogUtils;
import com.feicuiedu.eshop_20170518.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop_20170518.network.EShopClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gqq on 2017/5/22.
 */

public abstract class UICallback implements Callback{

    // 创建一个运行在主线程的Handler：通过构造方法中传入主线程的Looper
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Class<? extends ResponseEntity> mResponseType;// 要转换的数据类型

    @Override
    public void onFailure(final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // 运行在主线程，可以更新UI了
                onFailureInUI(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // 运行在主线程，可以更新UI
                try {
                    onResponseInUI(call, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 对成功和失败进行处理
    public  void onFailureInUI(Call call, IOException e){
        // 对于请求失败进行处理
        ToastWrapper.show(R.string.error_network);
        LogUtils.error("onFailureInUI",e);
        onBusinessResponse(false,null);
    }
    // 请求成功
    public  void onResponseInUI(Call call, Response response) throws IOException{
        // 判断是不是真正的成功
        if (response.isSuccessful()){
            // response转换为具体的实体类：response有，具体的实体类型ResponseType
            ResponseEntity responseEntity = EShopClient.getInstance().getResponseEntity(response, mResponseType);

            // 判断这个类是不是为空
            if (responseEntity==null || responseEntity.getStatus()==null){
                throw new RuntimeException("Fatal Api Error");
            }

            // 判断是不是响应数据正确
            if (responseEntity.getStatus().isSucceed()){
                // 成功的，数据拿到了
                onBusinessResponse(true,responseEntity);
            }else {
                ToastWrapper.show(responseEntity.getStatus().getErrorDesc());
                onBusinessResponse(false,responseEntity);
            }
        }
    }

    // 给调用者实现的方法：拿到数据并处理数据
    protected abstract void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity);


    //告诉ui要转换的实体类型
    public void setResponseType(Class<? extends ResponseEntity> responseType){
        mResponseType = responseType;
    }
}
