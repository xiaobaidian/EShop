package com.feicuiedu.eshop_20170518.feature.goods.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseFragment;
import com.feicuiedu.eshop_20170518.base.widgets.banner.BannerAdapter;
import com.feicuiedu.eshop_20170518.feature.goods.GoodsActivity;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfo;
import com.feicuiedu.eshop_20170518.network.entity.Picture;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by 蔡传飞 on 2017-06-01.
 */
//商品的详细信息
public class GoodsInfoFragment extends BaseFragment{

    private static final String GOODS_INFO = "goods_info";
    @BindView(R.id.pager_goods_pictures)
    ViewPager mPicturesPager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    @BindView(R.id.button_favorite)
    ImageButton mBtnFavorite;
    @BindView(R.id.text_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.text_goods_price)
    TextView mTvGoodsPrice;
    @BindView(R.id.text_market_price)
    TextView mTvMarketPrice;
    private GoodsInfo mGoodsInfo;

//提供一个创建方法：传递数据
    public static GoodsInfoFragment newInstance(@NonNull GoodsInfo goodsInfo) {
        GoodsInfoFragment goodsInfoFragment=new GoodsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOODS_INFO, new Gson().toJson(goodsInfo));
        goodsInfoFragment.setArguments(bundle);
        return goodsInfoFragment;
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_goods_info;
    }


    @Override
    protected void initView() {
//拿到传递的数据
        String string = getArguments().getString(GOODS_INFO);
        mGoodsInfo = new Gson().fromJson(string, GoodsInfo.class);
        // Viewpager的处理
        BannerAdapter<Picture> adapter =new BannerAdapter<Picture>() {
            @Override
            protected void bind(ViewHolder holder, Picture data) {
                //数据和视图的绑定
                Picasso.with(getContext()).load(data.getLarge()).into(holder.mImageView);
            }
        };
        // 设置适配器的数据
        adapter.reset(mGoodsInfo.getPictures());
        mPicturesPager.setAdapter(adapter);
        //給圆点指示器设置ViewPager
        mIndicator.setViewPager(mPicturesPager);
        adapter.registerDataSetObserver(mIndicator.getDataSetObserver());
        // 商品的名称
        mTvGoodsName.setText(mGoodsInfo.getName());
        // 商品的价格
        mTvGoodsPrice.setText(mGoodsInfo.getShopPrice());
        // 商品的商场价格
        String marketPrice = mGoodsInfo.getMarketPrice();
        // 给商场的价格设置删除线
        SpannableString spannableString = new SpannableString(marketPrice);
        spannableString.setSpan(new StrikethroughSpan(),0,marketPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置
        mTvMarketPrice.setText(spannableString);
        // 是否有加入收藏
        mBtnFavorite.setSelected(mGoodsInfo.isCollected());
    }


    @OnClick({R.id.text_goods_details, R.id.text_goods_comments})
    public void onClick(View view) {
        //通过GoodsActivity来完成,在Activity里面提供切换的方法
        GoodsActivity goodsActivity = (GoodsActivity) getActivity();
        switch (view.getId()) {
            case R.id.text_goods_details:
                //切换到商品详情Fragment
                goodsActivity.selectPage(1);
                break;
            case R.id.text_goods_comments:
                //切换到商品评价Fragment
                goodsActivity.selectPage(2);
                break;
        }
    }


}
