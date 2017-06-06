package com.feicuiedu.eshop_20170518.feature;

import android.animation.Animator;
import android.content.Intent;
import android.widget.ImageView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseActivity;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements Animator.AnimatorListener {

    @BindView(R.id.image_splash)
    ImageView imageSplash;

    //视图填充
    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_splash;
    }

    protected void initView() {
        imageSplash.setAlpha(0.3f);
        imageSplash.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setListener(this)
                .start();
    }



    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Intent intent = new Intent(this,EShopMainActivity.class);
        startActivity(intent);
        finishWithDefault();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
