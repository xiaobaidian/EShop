package com.feicuiedu.eshop_20170518.feature.goods;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feicuiedu.eshop_20170518.base.utils.TestFragment;
import com.feicuiedu.eshop_20170518.feature.goods.info.GoodsInfoFragment;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfo;

/**
 * Created by gqq on 2017/5/31.
 */

// 商品页面的适配器
public class GoodsInfoAdapter extends FragmentPagerAdapter {

    private GoodsInfo mGoodsInfo;// 数据

    public GoodsInfoAdapter(FragmentManager fm, GoodsInfo goodsInfo) {
        super(fm);
        this.mGoodsInfo = goodsInfo;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                // 商品
                return GoodsInfoFragment.newInstance(mGoodsInfo);
            case 1:
                // 详情
                return TestFragment.newInstance("商品详情");
            case 2:
                // 评价
                return TestFragment.newInstance("商品评价");
            default:
                throw new UnsupportedOperationException("Position:"+position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
