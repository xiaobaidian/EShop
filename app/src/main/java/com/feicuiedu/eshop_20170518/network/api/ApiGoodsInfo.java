package com.feicuiedu.eshop_20170518.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop_20170518.network.core.ApiInterface;
import com.feicuiedu.eshop_20170518.network.core.ApiPath;
import com.feicuiedu.eshop_20170518.network.core.RequestParam;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfoReq;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfoRsp;

/**
 * Created by 蔡传飞 on 2017-05-31.
 */
//服务器接口：商品详情的请求
public class ApiGoodsInfo implements ApiInterface {
    private GoodsInfoReq mGoodsInfoReq;

    // 请求体数据的初始化和请求体数据填充
    public ApiGoodsInfo(int goodsId) {
        mGoodsInfoReq = new GoodsInfoReq();
        mGoodsInfoReq.setGoodsId(goodsId);
    }

    @NonNull
    @Override
    public String getPath() {
        return ApiPath.GOODS_INFO;
    }

    @Nullable
    @Override
    public RequestParam getRequestParam() {
        return mGoodsInfoReq;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return GoodsInfoRsp.class;
    }
}
