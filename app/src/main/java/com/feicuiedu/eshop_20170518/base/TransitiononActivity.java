package com.feicuiedu.eshop_20170518.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.feicuiedu.eshop_20170518.R;

/**
 * Created by 蔡传飞 on 2017-05-23.
 */

public class TransitiononActivity extends AppCompatActivity{

    // 处理返回箭头的事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // 设置转场效果
        setTransitionAnimator(true);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        // 设置转场效果
        setTransitionAnimator(true);
    }

    @Override
    public void finish() {
        super.finish();
        // 设置转场效果
        setTransitionAnimator(false);
    }

    // 不需要的时候，设置没有动画效果的
    public void finishWithDefault(){
        super.finish();
    }

    // 设置跳转的动画
    private void setTransitionAnimator(boolean isNewActivity) {
        if (isNewActivity){
            // 新的页面从右边进入
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        }else {
            // 回到上个页面
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
    }



}
