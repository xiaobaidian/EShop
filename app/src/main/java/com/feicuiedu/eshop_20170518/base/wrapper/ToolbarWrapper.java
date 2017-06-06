package com.feicuiedu.eshop_20170518.base.wrapper;


import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseActivity;
import com.feicuiedu.eshop_20170518.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by 蔡传飞 on 2017-05-25.
 */

public class ToolbarWrapper {
    /**
     * 1. 绑定视图：找到toolbar，标题的TextView也要绑定
     * 2. 设置toolbar作为ActionBar展示
     * 3. 设置标题：隐藏默认的标题，展示自己的TextView
     * 4. 返回箭头的展示和隐藏
     */
    private BaseActivity mBaseActivity;
    private TextView mTvTitle;
    //activity使用
    public ToolbarWrapper(BaseActivity activity) {
        mBaseActivity=activity;
        Toolbar toolbar = ButterKnife.findById(activity, R.id.standard_toolbar);
        init(toolbar);
        //默认标题的不展示,有返回箭头
        setShowBack(true);
        setShowTitle(false);

    }


    //fragment使用
    public ToolbarWrapper(BaseFragment fragment) {
        mBaseActivity = (BaseActivity) fragment.getActivity();
        Toolbar toolbar = ButterKnife.findById(fragment.getView(), R.id.standard_toolbar);
        init(toolbar);
        // Fragment设置显示选项菜单
        fragment.setHasOptionsMenu(true);

        // 不显示标题、不显示返回箭头
        setShowBack(false);
        setShowTitle(false);
    }

//绑定TextView和设置ActionBar
    private void init(Toolbar toolbar) {
        //找到标题的TextView
       mTvTitle=ButterKnife.findById(toolbar,R.id.standard_toolbar_title);
        //设置toolbar作为actionbar展示
        mBaseActivity.setSupportActionBar(toolbar);
    }

    //设置标题是否展示：链式调用
    public ToolbarWrapper setShowTitle(boolean isShowTitle){
        mBaseActivity.getSupportActionBar().setDisplayShowTitleEnabled(isShowTitle);
        return this;
    }
    //设置返回箭头是否展示
    public ToolbarWrapper setShowBack(boolean isShowBack){
        mBaseActivity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(isShowBack);
        return this;
    }
    // 设置自定义的标题
    public ToolbarWrapper setCustomTitle(int resId){
        if (mTvTitle==null){
            throw new UnsupportedOperationException("No title TextView in toolbar");
        }
        mTvTitle.setText(resId);
        return this;
    }
}
