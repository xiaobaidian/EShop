package com.feicuiedu.eshop_20170518.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.eshop_20170518.network.EShopClient;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gqq on 2017/5/23.
 */

// 通用的Fragment的基类
public abstract class BaseFragment extends Fragment {

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(),container,false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消网络请求
        EShopClient.getInstance().cancelByTag(getClass().getSimpleName());
        mUnbinder.unbind();
        mUnbinder=null;
    }

    protected abstract void initView();
    @LayoutRes
    protected abstract int getContentViewLayout();
}
