package com.feicuiedu.eshop_20170518.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop_20170518.network.core.ApiInterface;
import com.feicuiedu.eshop_20170518.network.core.ApiPath;
import com.feicuiedu.eshop_20170518.network.core.RequestParam;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.entity.HomeCategoryRsp;

/**
 * Created by 蔡传飞 on 2017-05-31.
 */
//服务器接口：首页的分类推荐商品
public class ApiHomeCategory implements ApiInterface{
    @NonNull
    @Override
    public String getPath() {
        return ApiPath.HOME_CATEGORY;
    }

    @Nullable
    @Override
    public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return HomeCategoryRsp.class;
    }
}
