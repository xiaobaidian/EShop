package com.feicuiedu.eshop_20170518.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop_20170518.network.EShopClient;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 蔡传飞 on 2017-05-23.
 */

public abstract class BaseActivity extends TransitiononActivity {
    private Unbinder mUnbinder;

    /**
     * 1. 重复的视图绑定：butterknife
     * 2. 解绑等
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置视图：作为基类，是不是需要让子类来告诉我
        setContentView(getContentViewLayout());

        mUnbinder = ButterKnife.bind(this);

        // 调用一个initView();让子类必须重写，在initView里面处理视图
        initView();

    }

    protected abstract void initView();

    // 让子类告诉我们视图
    @LayoutRes
    protected abstract int getContentViewLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消请求
        EShopClient.getInstance().cancelByTag(getClass().getSimpleName());
        mUnbinder.unbind();
        mUnbinder=null;
    }
}
