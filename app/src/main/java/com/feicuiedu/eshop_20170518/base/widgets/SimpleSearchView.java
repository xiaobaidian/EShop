package com.feicuiedu.eshop_20170518.base.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 蔡传飞 on 2017-05-27.
 */

public class SimpleSearchView extends LinearLayout implements TextWatcher, TextView.OnEditorActionListener {



    /**
     * 1. 输入文本，有文本显示清除的按钮，没有文本不显示
     * EditText设置文本变化的监听
     * 2. 调启软键盘，软件盘显示操作设置“搜索”，点击软键盘的搜索，触发搜索，并隐藏软键盘
     * 设置软件盘的操作：EditText.setImeOptions()
     * 设置软件盘监听：setOnEditorActionListener();
     * 隐藏软件盘：InputMethodManager.hideSoftInputFromWindow();
     * 3. 搜索要执行搜索的事件：比如将输入的内容设置为关键字，在搜索页面进行数据获取
     * 搜索的具体事件，调用者来去实现，需要定义一个接口，接口里面定义一个方法，让调用者去实现
     */

    @BindView(R.id.button_search)
    ImageButton mButtonSearch;
    @BindView(R.id.edit_query)
    EditText mEditQuery;
    @BindView(R.id.button_clear)
    ImageButton mButtonClear;
    private OnSearchListener mOnSearchListener;



    public SimpleSearchView(Context context) {
        super(context);
        init(context);
    }

    public SimpleSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_simple_search_view, this);
        ButterKnife.bind(this);
        //视图的处理
        mEditQuery.addTextChangedListener(this);// 设置文本变化的监听

        // 设置软键盘的样式：回车的图标变成搜索图标
        mEditQuery.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        // 设置输入的类型:文本类型
        mEditQuery.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        // 设置软键盘的事件监听
        mEditQuery.setOnEditorActionListener(this);
    }


    // 搜索和清除的点击事件
    @OnClick({R.id.button_search, R.id.button_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                // 搜索的事件处理
                search();
                break;
            case R.id.button_clear:
                // 清除文本，搜索
                mEditQuery.setText(null);
                search();
                break;
        }
    }
    //--------------------------文本监听重写的方法----------------------------
    // 文本变化前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    // 文本变化中
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    // 文本输入完
    @Override
    public void afterTextChanged(Editable s) {
        // 根据文本有没有，来决定是否显示清除图标
        String query = mEditQuery.getText().toString();
        int visiable = TextUtils.isEmpty(query) ? View.INVISIBLE : VISIBLE;
        mButtonClear.setVisibility(visiable);
    }

    // -----------------软件盘操作监听重写的方法-------------------
    // 输入键盘的操作
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            // 进行搜索事件的处理
            search();
            return true;
        }

        return false;
    }

//拿到字符串，然后搜索的方法
    private void search(){
        String query=mEditQuery.getText().toString();
        if (mOnSearchListener != null) {
            mOnSearchListener.search(query);
        }
        // 关闭软键盘
        closeKeyBoard();
    }

    // 关闭软键盘
    private void closeKeyBoard() {
        // 失去焦点
        mEditQuery.clearFocus();

        // 关闭软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditQuery.getWindowToken(),0);
    }

    // 给控件提供的一个设置搜索监听的方法
    public void setOnSearchListener(OnSearchListener onSearchListener) {
        mOnSearchListener = onSearchListener;
    }

    //利用接口回调，为控件提供一个设置搜索监听的方法，让调用者实现监听，实现里面搜索的方法
    public interface OnSearchListener{
        //搜索的方法，调用者作具体的实现
        void search(String query);
    }
}
