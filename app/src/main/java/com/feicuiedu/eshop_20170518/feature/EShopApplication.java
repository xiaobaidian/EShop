package com.feicuiedu.eshop_20170518.feature;

import android.app.Application;

import com.feicuiedu.eshop_20170518.base.wrapper.ToastWrapper;

/**
 * Created by 蔡传飞 on 2017-05-25.
 */

public class EShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Toast包装类的初始化
        ToastWrapper.init(this);
    }
}
