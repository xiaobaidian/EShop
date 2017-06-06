package com.feicuiedu.eshop_20170518.base.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseActivity;
import com.feicuiedu.eshop_20170518.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop_20170518.network.entity.GoodsInfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 蔡传飞 on 2017-06-02.
 */
//商品选择的弹窗
public class GoodsPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    @BindView(R.id.image_goods)
    ImageView mIvGoods;
    @BindView(R.id.text_goods_price)
    TextView mTvPrice;
    @BindView(R.id.text_inventory_value)
    TextView mTvInventory;
    @BindView(R.id.text_number_value)
    TextView mTvNumber;
    @BindView(R.id.number_picker)
    SimpleNumberPicker mNumberPicker;

    private BaseActivity mActivity;
    private GoodsInfo mGoodsInfo;
    private final ViewGroup mParent;
    private OnConfirmListener mOnConfirmListener;

    /**
     * 1.视图搭建和处理,布局的填充：构造方法中完成
     * 2.显示的方法：show方法，弹出展示
     * 3.显示和隐藏的时候，Activity背景透明度变化
     * 4.商品数据的展示：需要商品的数据，可以在构造方法将数据传递过来
     * 5.选择好了商品之后，选择的商品信息给使用者传递出去，让具体事件交给使用者处理
     */
    public GoodsPopupWindow(BaseActivity activity, @NonNull GoodsInfo goodsInfo) {
        this.mActivity=activity;
        this.mGoodsInfo=goodsInfo;


        //布局的填充
        //获取顶层视图
        mParent = (ViewGroup) activity.getWindow().getDecorView();
        Context context = mParent.getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_goods_spec, mParent, false);
        // 设置布局
        setContentView(view);
        // 设置宽和高
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(context.getResources().getDimensionPixelSize(R.dimen.size_400));

        // 设置背景
        setBackgroundDrawable(new ColorDrawable());

        // 设置得到焦点
        setFocusable(true);

        // 设置点击窗口外部，PopupWindow消失
        setOutsideTouchable(true);

        // 设置消失的监听：弹出和隐藏的时候改变透明度
        setOnDismissListener(this);

        ButterKnife.bind(this,view);

        // 视图的初始化
        initView();

    }


    // 视图的初始化
    private void initView() {
    // 商品的图片、价格、库存、商品的数量
        Picasso.with(mParent.getContext()).load(mGoodsInfo.getImg().getLarge()).into(mIvGoods);
        mTvPrice.setText(mGoodsInfo.getShopPrice());
        mTvInventory.setText(String.valueOf(mGoodsInfo.getNumber()));

        //商品的数量：通过数量选择器决定
        mTvNumber.setText(String.valueOf(mNumberPicker.getNumber()));
        mNumberPicker.setOnNumberChangeListener(new SimpleNumberPicker.OnNumberChangeListener() {
            //可以拿到商品的数量

            @Override
            public void onNumberChanged(int number) {
                //实时拿到商品数量并展示
                mTvNumber.setText(String.valueOf(number));

            }
        });
    }


    // 确定的按钮点击事件
    @OnClick(R.id.button_ok)
    public void onClickOk(View view){
        // 具体选择了多少商品需要给使用者传递出去，具体的操作事件我们并不知道
        // 具体的事件交给使用者处理
        int number = mNumberPicker.getNumber();

        // 商品的数量为0，不做操作
        if (number==0){
            ToastWrapper.show(R.string.goods_msg_must_choose_number);
            return;
        }

        mOnConfirmListener.onConfirm(number);
    }

    // show方法：展示PopupWindow,参数中将监听传递过来
    public void show(OnConfirmListener onConfirmListener){

        this.mOnConfirmListener = onConfirmListener;

        // 从哪显示出来
        showAtLocation(mParent, Gravity.BOTTOM,0,0);
        //设置透明度
        backgroundAlpha(0.5f);
    }

    // 改变透明度的方法：根据传入的透明度变化：0.0f--1.0f
    private void backgroundAlpha(float bgAlpha) {
        // 设置给activity的窗体的透明度
        WindowManager.LayoutParams layoutParams = mActivity.getWindow().getAttributes();
        layoutParams.alpha = bgAlpha;
        mActivity.getWindow().setAttributes(layoutParams);
    }

    // ------------------窗体消失的时候触发-------------------------

    // 弹窗消失的时候，改变透明度
    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
    }

    // 利用接口回调：将选择的数量传递出去
    public interface OnConfirmListener{
        void onConfirm(int number);
    }
}
