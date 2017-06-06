package com.feicuiedu.eshop_20170518.feature;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseActivity;
import com.feicuiedu.eshop_20170518.base.utils.TestFragment;
import com.feicuiedu.eshop_20170518.feature.category.CategoryFragment;
import com.feicuiedu.eshop_20170518.feature.home.HomeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;

public class EShopMainActivity extends BaseActivity implements OnTabSelectListener {

    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;

    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;
    private TestFragment mCartFragment;
    private TestFragment mMineFragment;

    private Fragment mCurrentFragment;// 记录一下当前展示的Fragment

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_eshop_main;
    }

    protected void initView() {
        // 看一下FragmentManager里面是不是已经有了这些Fragment,如果有，手动恢复
        //直接设置为原来的Fragment不再重新创建
        retrieveFragment();

        bottomBar.setOnTabSelectListener(this);
    }
    //防止因系统重构造成的fragment重叠现象
    private void retrieveFragment() {
        FragmentManager manager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) manager.findFragmentByTag(HomeFragment.class.getName());
        mCategoryFragment = (CategoryFragment) manager.findFragmentByTag(CategoryFragment.class.getName());
        mCartFragment = (TestFragment) manager.findFragmentByTag("CartFragment");
        mMineFragment = (TestFragment) manager.findFragmentByTag("MineFragment");
    }
    @Override
    public void onTabSelected(@IdRes int tabId) {
        // 根据tabId,处理不同的事件：切换展示不同的Fragment
        switch (tabId){
            case R.id.tab_home://首页

                if (mHomeFragment==null){
                    mHomeFragment = HomeFragment.newInstance();
                }
                // 切换Fragment
                switchFragment(mHomeFragment);
                break;
            case R.id.tab_category://分类

                if (mCategoryFragment==null){
                    mCategoryFragment = CategoryFragment.newInstance();
                }
                // 切换
                switchFragment(mCategoryFragment);
                break;
            case R.id.tab_cart://购物车
                if (mCartFragment==null){
                    mCartFragment = TestFragment.newInstance("CartFragment");
                }
                switchFragment(mCartFragment);
                break;
            case R.id.tab_mine://我的
                if (mMineFragment==null){
                    mMineFragment = TestFragment.newInstance("MineFragment");
                }
                switchFragment(mMineFragment);
                break;
            default:
                throw new UnsupportedOperationException("unSupport");
        }
    }

    // 作用：就是帮助进行Fragment的切换
    private void switchFragment(Fragment fragment) {
        // replace       和add、show、hide的方式。

        if (mCurrentFragment==fragment) return;
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        // 隐藏当前Fragment
        if (mCurrentFragment!=null){
            transaction.hide(mCurrentFragment);
        }
        if (fragment.isAdded()){
            // 如果已经添加到FragmentManager里面，就展示
            transaction.show(fragment);
        }else {

            // 为了方便找到Fragment，设置Tag
            String tag;
            // 做一个判断，添加不同的tag
            if (fragment instanceof TestFragment){
                tag = ((TestFragment)fragment).getArgumentsText();
            }else {
                // 将fragment的类名作为tag
                tag = fragment.getClass().getName();
            }
            // 添加
            transaction.add(R.id.layout_container,fragment,tag);
        }
        transaction.commit();

        // 重新记录一下当前的Fragment
        mCurrentFragment = fragment;
    }

    // 处理一下返回键
    @Override
    public void onBackPressed() {
        if (mCurrentFragment!=mHomeFragment){
            // 如果不在首页上，就切换到首页
            bottomBar.selectTabWithId(R.id.tab_home);
            return;
        }

        // 如果不是首页，不关闭程序，退到后台运行
        moveTaskToBack(true);
    }
}
