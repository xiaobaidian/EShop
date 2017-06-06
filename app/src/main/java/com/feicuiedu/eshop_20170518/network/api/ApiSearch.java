package com.feicuiedu.eshop_20170518.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop_20170518.network.core.ApiInterface;
import com.feicuiedu.eshop_20170518.network.core.ApiPath;
import com.feicuiedu.eshop_20170518.network.core.RequestParam;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.entity.Filter;
import com.feicuiedu.eshop_20170518.network.entity.Pagination;
import com.feicuiedu.eshop_20170518.network.entity.SearchReq;
import com.feicuiedu.eshop_20170518.network.entity.SearchRsp;

/**
 * Created by 蔡传飞 on 2017-05-31.
 */
//服务器接口：搜索商品，post请求
public class ApiSearch implements ApiInterface{
    private SearchReq mSearchReq;//请求体

    //构造方法中完成数据的初始化与数据的填充
    public ApiSearch(Filter filter, Pagination pagination) {
        mSearchReq=new SearchReq();
        mSearchReq.setFilter(filter);
        mSearchReq.setPagination(pagination);
    }

    @NonNull
    @Override
    public String getPath() {
        return ApiPath.SEARCH;
    }

    @Nullable
    @Override
    public RequestParam getRequestParam() {
        return mSearchReq;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return SearchRsp.class;
    }
}
