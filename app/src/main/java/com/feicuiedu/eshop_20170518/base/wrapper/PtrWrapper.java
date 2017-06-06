package com.feicuiedu.eshop_20170518.base.wrapper;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.feicuiedu.eshop_20170518.R;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by 蔡传飞 on 2017-05-25.
 */
//下拉刷新和加载的包装类
public abstract class PtrWrapper {
    /**
     * 1.找到刷新加载的控件：构造方法中绑定
     * 2.设置刷新的头布局、加载的尾布局（如果有加载才设置）
     * 3.刷新的处理：设置ptrHandler，去获取数据（交给调用者处理）
     * 4.延时然后自动刷新
     * 5.停止刷新
     * 6.是不是在刷新等
     */
    private PtrFrameLayout mRefreshLayout;


    //activity
    public PtrWrapper(Activity activity,boolean isNeedLoad) {
        mRefreshLayout= ButterKnife.findById(activity, R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    //fragment
    public PtrWrapper(Fragment fragment,boolean isNeedLoad) {
        mRefreshLayout = ButterKnife.findById(fragment.getView(),R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    //初始化操作
    private void initPtr(boolean isNeedLoad){
        if(mRefreshLayout!=null){
            mRefreshLayout.disableWhenHorizontalMove(true);
        }

        // 设置刷新的布局：头布局
        initPtrHeader();

        // 需要加载，设置尾部布局：加载的布局
        if (isNeedLoad){
            initPtrFooter();
        }

        // 设置刷新和加载的handler
        mRefreshLayout.setPtrHandler(mPtrHandler);
    }


    // 刷新和加载的Hanlder
    private PtrDefaultHandler2 mPtrHandler = new PtrDefaultHandler2() {

        // 加载会触发
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            onLoadMore();
        }

        // 刷新会触发
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            onRefresh();
        }
    };

    // 设置尾部布局(默认)
    private void initPtrFooter() {
        PtrClassicDefaultFooter footer = new PtrClassicDefaultFooter(mRefreshLayout.getContext());
        mRefreshLayout.setFooterView(footer);
        mRefreshLayout.addPtrUIHandler(footer);
    }

    // 设置头布局（默认）
    private void initPtrHeader() {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mRefreshLayout.getContext());
        mRefreshLayout.setHeaderView(header);
        mRefreshLayout.addPtrUIHandler(header);
    }


    // 自动刷新的方法
    public void autoRefresh(){
        mRefreshLayout.autoRefresh();
    }

    // 延时自动刷新
    public void postRefreshDelayed(long delay){
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.autoRefresh();
            }
        }, delay);
    }

    // 停止刷新
    public void stopRefresh(){
        if (isRefreshing()) {
            mRefreshLayout.refreshComplete();
        }
    }

    // 是不是正在刷新
    public boolean isRefreshing(){
        return mRefreshLayout.isRefreshing();
    }

    // 让调用者必须处理刷新的事件
    protected abstract void onRefresh();
    // 让调用者必须处理加载的事件
    protected abstract void onLoadMore();
}
