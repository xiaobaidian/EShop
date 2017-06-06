package com.feicuiedu.eshop_20170518.feature.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseActivity;
import com.feicuiedu.eshop_20170518.base.utils.LogUtils;
import com.feicuiedu.eshop_20170518.base.widgets.SimpleSearchView;
import com.feicuiedu.eshop_20170518.base.wrapper.PtrWrapper;
import com.feicuiedu.eshop_20170518.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop_20170518.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop_20170518.feature.goods.GoodsActivity;
import com.feicuiedu.eshop_20170518.network.EShopClient;
import com.feicuiedu.eshop_20170518.network.api.ApiSearch;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.core.UICallback;
import com.feicuiedu.eshop_20170518.network.entity.Filter;
import com.feicuiedu.eshop_20170518.network.entity.Paginated;
import com.feicuiedu.eshop_20170518.network.entity.Pagination;
import com.feicuiedu.eshop_20170518.network.entity.SearchReq;
import com.feicuiedu.eshop_20170518.network.entity.SearchRsp;
import com.feicuiedu.eshop_20170518.network.entity.SimpleGoods;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;

public class SearchGoodsActivity extends BaseActivity {

    private static final String EXTRA_SEARCH_FILTER = "extra_search_filter";
    @BindView(R.id.search_view)
    SimpleSearchView mSearchView;
    @BindView(R.id.list_goods)
    ListView mGoodsListView;
    @BindViews({R.id.text_is_hot,R.id.text_most_expensive,R.id.text_cheapest})
    List<TextView> mTvOrderList;
    private Filter mFilter;
    private PtrWrapper mPtrWrapper;
    private Pagination mPagination = new Pagination();// 分页参数
    private Call mSearchCall;
    private Paginated mPaginated;
    private SearchGoodsAdapter mGoodsAdapter;
    private long mLastRefreshTime;

    // 对外提供一个跳转的方法：因为涉及到传递数据，为了规范数据的传递
    // 将Filter转换为字符串传递：不将Filter序列化
    public static Intent getStartIntent(Context context, Filter filter) {
        Intent intent = new Intent(context, SearchGoodsActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER, new Gson().toJson(filter));
        return intent;
    }
    @Override
    protected void initView() {
        // 视图的初始化
        // toolbar的展示
        new ToolbarWrapper(this);

        // 一进入，默认选择热销
        mTvOrderList.get(0).setActivated(true);

        // 传递的数据取出来
        String filterStr = getIntent().getStringExtra(EXTRA_SEARCH_FILTER);
        mFilter = new Gson().fromJson(filterStr, Filter.class);

        // 刷新和加载的处理
        // 进行网络数据的获取
        // 进行网络数据的获取
        mPtrWrapper = new PtrWrapper(this, true) {
            @Override
            protected void onRefresh() {
                // 进行网络数据的获取
                searchGoods(true);
            }

            @Override
            protected void onLoadMore() {
                // 进行网络数据的加载，判断上次请求的结果里面是不是还有更多数据
                if (mPaginated.hasMore()){
                    // 加载更多数据
                    searchGoods(false);
                }else {
                    // 没有更多数据
                    mPtrWrapper.stopRefresh();
                    ToastWrapper.show(R.string.msg_load_more_complete);
                }
            }
        };

        // 搜索控件的监听
        mSearchView.setOnSearchListener(new SimpleSearchView.OnSearchListener() {
            // 搜索的时候触发
            @Override
            public void search(String query) {
                // 将输入的关键字填充到搜索过滤器Filter里面，然后自动刷新
                mFilter.setKeywords(query);
                mPtrWrapper.autoRefresh();
            }
        });

        // listView设置适配器
        mGoodsAdapter = new SearchGoodsAdapter();
        mGoodsListView.setAdapter(mGoodsAdapter);

        // 自动刷新
        mPtrWrapper.postRefreshDelayed(50);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_search_goods;
    }


    @OnItemClick(R.id.list_goods)
    public void goodsItemClick(int position){
        // 跳转到详情页
        int id = mGoodsAdapter.getItem(position).getId();
        Intent intent = GoodsActivity.getStartIntent(this, id);
        startActivity(intent);
    }

    // 搜索商品的方法中进行网络获取
    private void searchGoods(boolean isRefresh) {

        if (mSearchCall!=null){
            mSearchCall.cancel();
        }

        if (isRefresh){
            mLastRefreshTime = System.currentTimeMillis();

            // 刷新的页数从1开始
            mPagination.reset();
            // 将ListView定位到第一条
            mGoodsListView.setSelection(0);
        }else {
            // 页数要进行+1
            mPagination.next();
            LogUtils.debug("Load More Page = "+mPagination.getPage());
        }

        SearchReq searchReq = new SearchReq();
        searchReq.setFilter(mFilter);
        searchReq.setPagination(mPagination);

        mSearchCall = EShopClient.getInstance().enqueue(new ApiSearch(mFilter,mPagination), mUICallback,getClass().getSimpleName());
    }

    private UICallback mUICallback = new UICallback() {
        @Override
        protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
            mPtrWrapper.stopRefresh();
            mSearchCall = null;
            if (isSuccess){
                // 拿出当前的数据
                SearchRsp searchRsp = (SearchRsp) responseEntity;

                // 将当前分页结果取出，便于下次进行加载的时候判断是否还需要进行
                mPaginated = searchRsp.getPaginated();

                // 设置给适配器
                List<SimpleGoods> goodsList = searchRsp.getData();
                if (mPagination.isFirst()){
                    // 刷新得到的数据
                    mGoodsAdapter.reset(goodsList);
                }else {
                    // 加载出来的数据
                    mGoodsAdapter.addAll(goodsList);
                }
            }
        }
    };


    // 排序的切换
    @OnClick({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    public void chooseGoodsOrder(View view) {


        // 如果当前显示是此项，不去触发
        if (view.isActivated()) return;

        // 如果正在刷新，不去执行
        if (mPtrWrapper.isRefreshing()) return;

        // 遍历将三个都设置为inActivited
        for (TextView sortView:
                mTvOrderList) {
            sortView.setActivated(false);
        }

        // 选择的某项设置为Activited
        view.setActivated(true);

        // 排序字段
        String sortBy;
        switch (view.getId()) {
            case R.id.text_is_hot:
                sortBy = Filter.SORT_IS_HOT;// 热销
                break;
            case R.id.text_most_expensive:
                sortBy = Filter.SORT_PRICE_DESC;// 价格降序
                break;
            case R.id.text_cheapest:
                sortBy = Filter.SORT_PRICE_ASC;// 价格升序
                break;
            default:
                throw new UnsupportedOperationException();
        }



        mFilter.setSortBy(sortBy);

        // 如果刷新数据，可能会出现切换过快，Tab切换了，但数据没有及时更新
        // 看一下本次刷新和上次刷新时间之间间隔2秒以上，立即刷新，如果没有，延时刷新
        long time = mLastRefreshTime + 2000 - System.currentTimeMillis();
        time = time<0?0:time;
        mPtrWrapper.postRefreshDelayed(time);

    }
}
