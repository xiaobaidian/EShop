package com.feicuiedu.eshop_20170518.base.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gqq on 2017/6/1.
 */

// 商品数量的选择器：组合控件
public class SimpleNumberPicker extends RelativeLayout {

    @BindView(R.id.image_minus)
    ImageView mIvMinus;
    @BindView(R.id.text_number)
    TextView mTvNumber;
    @BindView(R.id.image_plus)
    ImageView mIvPlus;
    private int mMin = 0;

    private OnNumberChangeListener mOnNumberChangeListener;


    /**
     * 1. 视图：+ - 的图标、展示数量的文本
     * 2. + - ：点击事件，相应的对数量进行增减
     * 3. 对外提供一个数量变化的监听，用于将变化后的商品数量提供出去
     * <p>
     * 在这个自定义的控件中，学习自定义属性
     */

    // 代码中使用：new
    public SimpleNumberPicker(Context context) {
        super(context);
        init(context,null);
    }

    // 布局中使用
    public SimpleNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    // 布局中使用，设置了style
    public SimpleNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    // 布局的填充和初始化
    public void init(Context context,AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_simple_number_picker, this, true);
        ButterKnife.bind(this);

        /**
         * 自定义属性的流程
         * 1. 需要创建属性集及属性值：比如min_number
         * 在res/values下创建一个文件：attrs.xml,attrs_xxx.xml
         * 2. 布局中作为一个属性使用
         * 3. 代码中获取设置的属性值
         */

        if (attrs==null) return;
        // 属性集
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleNumberPicker);
        // 获取定义的属性的值
        mMin = typedArray.getInteger(R.styleable.SimpleNumberPicker_min_number, 0);
        typedArray.recycle();// 重复循环使用TypedArray

        // 处理视图
        setNumber(mMin);
        mIvMinus.setImageResource(R.drawable.btn_minus_gray);
    }

    // 设置数量的方法
    private void setNumber(int number) {

        if (number<mMin){
            throw new IllegalArgumentException("Min Number is "+mMin+" while number is "+number);
        }

        mTvNumber.setText(String.valueOf(number));
    }

    // 获取数量的方法
    public int getNumber(){
        String string = mTvNumber.getText().toString();
        return Integer.parseInt(string);

    }

    @OnClick({R.id.image_minus, R.id.image_plus})
    public void onClick(View view) {
        if (view.getId()==R.id.image_minus){
            // 点击减少
            if (getNumber()==mMin){
                // 不能再减少了
                return;
            }
            // 数量减1
            setNumber(getNumber()-1);
        }else {
            // 点击增加,数量加1
            setNumber(getNumber()+1);
        }

        // 减少的图标的样式
        if (getNumber()==mMin){
            mIvMinus.setImageResource(R.drawable.btn_minus_gray);
        }else {
            mIvMinus.setImageResource(R.drawable.btn_minus);
        }

        // 选择的数量发生变化后，实时的告诉别人：将数量传递出去
        if (mOnNumberChangeListener!=null){
            mOnNumberChangeListener.onNumberChanged(getNumber());
        }
    }

    // 提供一个设置监听的方法
    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener){
        mOnNumberChangeListener= onNumberChangeListener;
    }

    // 对外提供一个接口：利用接口回调，写一个方法：用于传递数量的变化
    public interface OnNumberChangeListener{
        void onNumberChanged(int number);
    }
}
