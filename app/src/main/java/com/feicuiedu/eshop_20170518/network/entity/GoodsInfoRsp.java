package com.feicuiedu.eshop_20170518.network.entity;


import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.google.gson.annotations.SerializedName;

// 商品详情的响应体
public class GoodsInfoRsp extends ResponseEntity {

    @SerializedName("data")
    private GoodsInfo mData;

    public GoodsInfo getData() {
        return mData;
    }

}
