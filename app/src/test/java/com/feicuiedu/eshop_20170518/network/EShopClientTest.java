package com.feicuiedu.eshop_20170518.network;

import com.feicuiedu.eshop_20170518.network.api.ApiCategory;
import com.feicuiedu.eshop_20170518.network.api.ApiGoodsInfo;
import com.feicuiedu.eshop_20170518.network.api.ApiHomeBanner;
import com.feicuiedu.eshop_20170518.network.api.ApiHomeCategory;
import com.feicuiedu.eshop_20170518.network.api.ApiSearch;
import com.feicuiedu.eshop_20170518.network.entity.CategoryRsp;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfoRsp;
import com.feicuiedu.eshop_20170518.network.entity.HomeBannerRsp;
import com.feicuiedu.eshop_20170518.network.entity.HomeCategoryRsp;
import com.feicuiedu.eshop_20170518.network.entity.SearchReq;
import com.feicuiedu.eshop_20170518.network.entity.SearchRsp;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
/**
 * Created by 蔡传飞 on 2017-05-23.
 */
public class EShopClientTest {
    @Test
    public void getCategory() throws Exception {
        CategoryRsp categoryRsp = EShopClient.getInstance().excute(new ApiCategory());

        // 怎么去验证数据是不是正确的
        // 断言类:断言方法，主要是为我们做一个判断
        assertTrue(categoryRsp.getStatus().isSucceed());
    }
    // 首页：banner
    @Test
    public void getHomeBanner() throws Exception{

        HomeBannerRsp homeBannerRsp = EShopClient.getInstance()
                .excute(new ApiHomeBanner());

        assertTrue(homeBannerRsp.getStatus().isSucceed());
    }

    //首页：分类商品
    @Test
    public void getHomeCategory() throws Exception{
        HomeCategoryRsp homeCategoryRsp = EShopClient.getInstance()
                .excute(new ApiHomeCategory());
        assertTrue(homeCategoryRsp.getStatus().isSucceed());
    }

    //搜索商品
    @Test
    public void getSearch() throws Exception{
        SearchReq searchReq=new SearchReq();
        SearchRsp searchRsp = EShopClient.getInstance()
                .excute(new ApiSearch(null,null));
        assertTrue(searchRsp.getStatus().isSucceed());
    }

    // 商品详情的请求
    @Test
    public void getGoodsInfo() throws Exception{
        ApiGoodsInfo apiGoodsInfo = new ApiGoodsInfo(78);
        GoodsInfoRsp goodsInfoRsp = EShopClient.getInstance().excute(apiGoodsInfo);
        assertTrue(goodsInfoRsp.getStatus().isSucceed());
    }
}