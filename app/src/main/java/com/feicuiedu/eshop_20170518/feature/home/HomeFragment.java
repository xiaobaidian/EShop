package com.feicuiedu.eshop_20170518.feature.home;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseFragment;
import com.feicuiedu.eshop_20170518.base.widgets.banner.BannerAdapter;
import com.feicuiedu.eshop_20170518.base.widgets.banner.BannerLayout;
import com.feicuiedu.eshop_20170518.base.wrapper.PtrWrapper;
import com.feicuiedu.eshop_20170518.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop_20170518.feature.goods.GoodsActivity;
import com.feicuiedu.eshop_20170518.network.EShopClient;
import com.feicuiedu.eshop_20170518.network.api.ApiHomeBanner;
import com.feicuiedu.eshop_20170518.network.api.ApiHomeCategory;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.core.UICallback;
import com.feicuiedu.eshop_20170518.network.entity.Banner;
import com.feicuiedu.eshop_20170518.network.entity.HomeBannerRsp;
import com.feicuiedu.eshop_20170518.network.entity.HomeCategoryRsp;
import com.feicuiedu.eshop_20170518.network.entity.Picture;
import com.feicuiedu.eshop_20170518.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * Created by 蔡传飞 on 2017-05-24.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_home_goods)
    ListView mListHomeGoods;

    private ImageView[] mIvPromotes = new ImageView[4];
    private TextView mTvPromotesGoods;
    private BannerAdapter<Banner> mBannerAdapter;
    private HomeGoodsAdapter mGoodsAdapter;
    private PtrWrapper mPtrWrapper;

    private boolean mBannerRefreshed = false;
    private boolean mCategoryRefreshed = false;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        // toolbar的处理
        // 利用Toolbar的包装类
        new ToolbarWrapper(this).setCustomTitle(R.string.home_title);

        // 处理刷新相关
        // 刷新数据
        // 加载数据
        mPtrWrapper = new PtrWrapper(this,false) {
            @Override
            protected void onRefresh() {
                mBannerRefreshed = false;
                mCategoryRefreshed = false;
                // 刷新数据
                getHomeData();
            }

            @Override
            protected void onLoadMore() {
                // 加载数据
            }
        };
        mPtrWrapper.postRefreshDelayed(50);

        // 设置适配器和头布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.partial_home_header,mListHomeGoods,false);
        // 找到头布局的控件
        BannerLayout bannerLayout = ButterKnife.findById(view,R.id.layout_banner);
// 设置适配器
        mBannerAdapter = new BannerAdapter<Banner>() {
            @Override
            protected void bind(ViewHolder holder, Banner data) {
                Picasso.with(getContext()).load(data.getPicture().getLarge()).into(holder.mImageView);
            }
        };
        bannerLayout.setAdapter(mBannerAdapter);
        // 促销商品
        mIvPromotes[0] = ButterKnife.findById(view,R.id.image_promote_one);
        mIvPromotes[1] = ButterKnife.findById(view,R.id.image_promote_two);
        mIvPromotes[2] = ButterKnife.findById(view,R.id.image_promote_three);
        mIvPromotes[3] = ButterKnife.findById(view,R.id.image_promote_four);

        // 促销单品的TextView
        mTvPromotesGoods = ButterKnife.findById(view, R.id.text_promote_goods);

        // 给ListView设置头布局
        mListHomeGoods.addHeaderView(view);
        // 给ListView设置适配器
        mGoodsAdapter = new HomeGoodsAdapter();
        mListHomeGoods.setAdapter(mGoodsAdapter);
    }



    private PtrDefaultHandler2 mPtrHandler = new PtrDefaultHandler2() {

        // 加载的时候触发
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {

        }

        // 刷新的时候触发
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            // 请求数据
            getHomeData();
        }
    };



    // 获取数据
    private void getHomeData() {

        // 轮播图和促销单品的callback
        UICallback bannerCallback = new UICallback() {
            @Override
            protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                mBannerRefreshed = true;
                if (isSuccess) {
                    HomeBannerRsp homeBannerRsp = (HomeBannerRsp) responseEntity;
                    // 拿到了数据，首先给Bannerlayout,另外是促销单品
                    mBannerAdapter.reset(homeBannerRsp.getData().getBanners());
                    setPromotesGoods(homeBannerRsp.getData().getGoodsList());
                }
                if (mBannerRefreshed && mCategoryRefreshed){
                    // 两个请求都拿到了数据，停止刷新
                    mPtrWrapper.stopRefresh();
                }
            }
        };

        // 分类和推荐商品的callback
        UICallback categoryCallback = new UICallback() {
            @Override
            protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                mCategoryRefreshed = true;
                if (isSuccess){
                    HomeCategoryRsp homeCategoryRsp = (HomeCategoryRsp) responseEntity;
                    mGoodsAdapter.reset(homeCategoryRsp.getData());
                }
                if (mBannerRefreshed&&mCategoryRefreshed){
                    // 两个数据都拿到了，停止刷新
                    mPtrWrapper.stopRefresh();
                }
            }
        };
        // 轮播图和促销单品
        EShopClient.getInstance().enqueue(new ApiHomeBanner(),bannerCallback,getClass().getSimpleName());
        // 分类和推荐商品
        EShopClient.getInstance().enqueue(new ApiHomeCategory(), categoryCallback,getClass().getSimpleName());

    }

    // 设置促销单品
    private void setPromotesGoods(List<SimpleGoods> goodsList) {
        mTvPromotesGoods.setVisibility(View.VISIBLE);
        for (int i = 0; i < mIvPromotes.length; i++) {
            mIvPromotes[i].setVisibility(View.VISIBLE);

            // 图片地址的资源填充
            final SimpleGoods simpleGoods = goodsList.get(i);
            Picture picture = simpleGoods.getImg();

            // 数据的填充
            Picasso.with(getContext()).load(picture.getLarge())
                    .transform(new CropCircleTransformation())// 圆形图片
                    .transform(new GrayscaleTransformation())// 灰度
                    .into(mIvPromotes[i]);

            mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到详情
                    int simpleGoodsId = simpleGoods.getId();
                    Intent intent = GoodsActivity.getStartIntent(getContext(), simpleGoodsId);
                    getActivity().startActivity(intent);
                }
            });
        }
    }
}





