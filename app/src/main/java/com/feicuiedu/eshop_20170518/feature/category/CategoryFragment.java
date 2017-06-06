package com.feicuiedu.eshop_20170518.feature.category;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseFragment;
import com.feicuiedu.eshop_20170518.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop_20170518.feature.search.SearchGoodsActivity;
import com.feicuiedu.eshop_20170518.network.EShopClient;
import com.feicuiedu.eshop_20170518.network.api.ApiCategory;
import com.feicuiedu.eshop_20170518.network.core.ResponseEntity;
import com.feicuiedu.eshop_20170518.network.core.UICallback;
import com.feicuiedu.eshop_20170518.network.entity.CategoryPrimary;
import com.feicuiedu.eshop_20170518.network.entity.CategoryRsp;
import com.feicuiedu.eshop_20170518.network.entity.Filter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * Created by 蔡传飞 on 2017-05-23.
 */

public class CategoryFragment extends BaseFragment {
    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_category)
    ListView mListCategory;
    @BindView(R.id.list_children)
    ListView mListChildren;

    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    private List<CategoryPrimary> mData;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    // 布局填充
    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化视图操作
        initView();
    }
//视图的初始化
    protected void initView() {
        // toolbar的处理
        initToolbar();
        // listView设置适配器
        mCategoryAdapter = new CategoryAdapter();
        mListCategory.setAdapter(mCategoryAdapter);
        mChildrenAdapter = new ChildrenAdapter();
        mListChildren.setAdapter(mChildrenAdapter);
        // 去拿数据
        if (mData != null) {
            // 直接去更新UI

        } else {
            {
                // 去进行网络请求获取数据
                UICallback uiCallback = new UICallback() {
                    @Override
                    protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                        if (isSuccess) {
                            // 拿到具体的数据
                            CategoryRsp categoryRsp = (CategoryRsp) responseEntity;
                            mData = categoryRsp.getData();
                            // 数据有了之后更新UI：拿到的Data是一级分类的信息，一级分类里面又包括二级分类
                            // 数据先给一级分类，默认选择一级分类的第一条，二级分类数据才能展示。
                            updateCategory();
                        }
                    }
                };
                EShopClient.getInstance().enqueue(new ApiCategory(), uiCallback,getClass().getSimpleName());
            }
        }
    }



        //更新分类数据:数据的填充
    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        // 切换展示二级分类
        chooseCategory(0);
    }
    // 根据一级分类的某一项展示二级分类
    private void chooseCategory(int position) {
        mListCategory.setItemChecked(position,true);
        // 填充二级分类
        mChildrenAdapter.reset(mCategoryAdapter.getItem(position).getChildren());
    }

    // 点击一级分类的item：切换展示二级分类的信息
    @OnItemClick(R.id.list_category)
    public void onItemClick(int position){
        chooseCategory(position);
    }
    // 点击二级分类的item：跳转页面
    @OnItemClick(R.id.list_children)
    public void onChildrenClick(int position){
        int id = mChildrenAdapter.getItem(position).getId();
        navigateToSearch(id);
    }

    // toolbar的处理
    private void initToolbar() {
        new ToolbarWrapper(this).setCustomTitle(R.string.category_title);
    }

    // 处理搜索图标的展示：作为ActionBar的一个选项菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category,menu);
    }

    // 用于处理选项菜单的事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 点击的是返回箭头
        if (id==android.R.id.home){
            getActivity().onBackPressed();
            return true;
        }

        // 点击搜索图标的事件
        if (id==R.id.menu_search){
            int position = mListCategory.getCheckedItemPosition();
            // 拿到id
            int categoryId = mCategoryAdapter.getItem(position).getId();
            navigateToSearch(categoryId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 写一个跳转的方法
    private void navigateToSearch(int categoryId){
        // 根据id构建Filter，然后跳转页面
        Filter filter = new Filter();
        filter.setCategoryId(categoryId);
        Intent intent = SearchGoodsActivity.getStartIntent(getContext(), filter);
        getActivity().startActivity(intent);
    }
}
