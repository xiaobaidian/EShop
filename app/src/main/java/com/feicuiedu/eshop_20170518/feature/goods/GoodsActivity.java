package com.feicuiedu.eshop_20170518.feature.goods;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseActivity;
import com.feicuiedu.eshop_20170518.base.widgets.GoodsPopupWindow;
import com.feicuiedu.eshop_20170518.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop_20170518.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop_20170518.network.EShopClient;
import com.feicuiedu.eshop_20170518.network.api.ApiGoodsInfo;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.core.UICallback;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfo;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfoRsp;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by 蔡传飞 on 2017-05-31.
 */

// 商品页面
public class GoodsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String EXTRA_GOODS_ID = "goodsId";
    @BindView(R.id.pager_goods)
    ViewPager mGoodsPager;
    @BindViews({R.id.text_tab_goods,R.id.text_tab_details,R.id.text_tab_comments})
    List<TextView> mTvTabList;
    private GoodsInfo mGoodsInfo;
    private GoodsPopupWindow mGoodsPopupWindow;

    // 对外提供一个跳转的方法
    public static Intent getStartIntent(Context context, int goodsId){
        Intent intent = new Intent(context,GoodsActivity.class);
        intent.putExtra(EXTRA_GOODS_ID,goodsId);
        return intent;
    }

    // 布局的填充
    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_goods;
    }

    // 视图的初始化操作
    @Override
    protected void initView() {

        //toolbar的处理
        new ToolbarWrapper(this);
        //ViewPager设置监听
        mGoodsPager.addOnPageChangeListener(this);
        //拿到传递的数据
        int goodsId = getIntent().getIntExtra(EXTRA_GOODS_ID, 0);

        // 获取数据
        EShopClient.getInstance().enqueue(new ApiGoodsInfo(goodsId),mGoodsInfoCallback,getClass().getSimpleName());
    }
    // 请求的回调
    private UICallback mGoodsInfoCallback = new UICallback() {
        @Override
        protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
            if (isSuccess){
                GoodsInfoRsp goodsInfoRsp = (GoodsInfoRsp) responseEntity;
                // 拿到商品详情的响应数据
                mGoodsInfo = goodsInfoRsp.getData();
                // 拿到数据了，数据给ViewPager的适配器设置，展示
                mGoodsPager.setAdapter(new GoodsInfoAdapter(getSupportFragmentManager(),mGoodsInfo));
                // 默认选择tab的第一项
                chooseTab(0);
            }
        }
    };


    // 主要是改变选中的Tab的样式：选择器和字体大小
    private void chooseTab(int position) {
        Resources resources = getResources();
        for (int i = 0; i < mTvTabList.size(); i++) {
            mTvTabList.get(i).setSelected(i==position);
            // 改变字体的大小
            float textSize = i==position?resources.getDimension(R.dimen.font_large):resources.getDimension(R.dimen.font_normal);
            mTvTabList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        }
    }


    //三个TextView的Tab标题切换事件
    @OnClick({R.id.text_tab_goods,R.id.text_tab_details,R.id.text_tab_comments})
    public void onClickTab(TextView textView){
        // 拿到点击的是哪一项，ViewPager切换到当前项，改变选中的样式
        int position = mTvTabList.indexOf(textView);
        mGoodsPager.setCurrentItem(position,false);
        chooseTab(position);
    }

    // button的点击事件
    @OnClick({R.id.button_add_cart,R.id.button_buy})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_add_cart:
                showGoodsPopupWindow();
                break;
            case R.id.button_buy:
                showGoodsPopupWindow();
                break;
        }
    }

    // 展示商品选择的弹窗
    private void showGoodsPopupWindow(){
        if (mGoodsInfo==null) return;
        if (mGoodsPopupWindow==null) {
            mGoodsPopupWindow = new GoodsPopupWindow(this, mGoodsInfo);
        }
        mGoodsPopupWindow.show(new GoodsPopupWindow.OnConfirmListener() {
            @Override
            public void onConfirm(int number) {
                // 具体操作
                ToastWrapper.show("Confirm:"+number);
                mGoodsPopupWindow.dismiss();
            }
        });
    }

    // 填充选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_goods,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 处理选项菜单的item选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.menu_share){
            ToastWrapper.show(R.string.action_share);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    //------------------------------ViewPager监听重写的方法----------------------------------------------
//页面滑动中
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
//页面选择
    @Override
    public void onPageSelected(int position) {
        //  处理上面三个Tab的切换样式
        chooseTab(position);
    }
    // 页面滑动状态变化后
    @Override
    public void onPageScrollStateChanged(int state) {

    }
//对外提供一个切换页面的方法
    public void selectPage(int position) {
        mGoodsPager.setCurrentItem(position,false);
        chooseTab(position);
    }
}