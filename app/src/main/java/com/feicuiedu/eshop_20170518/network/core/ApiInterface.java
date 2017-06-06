package com.feicuiedu.eshop_20170518.network.core;

/**
 * Created by 蔡传飞 on 2017-05-31.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 将请求的路径、请求的参数、响应体的数据类型做一个统一的管理。
 * 具体的每一个实现类带表服务器接口，Api接口的抽象化
 */
public interface ApiInterface {
    @NonNull String getPath();//请求地址,规范返回值不能为null

    @Nullable RequestParam getRequestParam();//请求参数，返回值可以为null

    @NonNull Class<? extends ResponseEntity> getResponseEntity();//响应的实体类型，返回值不能为null
}
