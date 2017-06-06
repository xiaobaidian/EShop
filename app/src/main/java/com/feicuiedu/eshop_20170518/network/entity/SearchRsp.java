package com.feicuiedu.eshop_20170518.network.entity;


import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// 搜索商品的响应体
public class SearchRsp extends ResponseEntity{



    @SerializedName("data") private List<SimpleGoods> mData;

    @SerializedName("paginated") private Paginated mPaginated;

    public List<SimpleGoods> getData() {
        return mData;
    }

    public Paginated getPaginated() {
        return mPaginated;
    }


}
