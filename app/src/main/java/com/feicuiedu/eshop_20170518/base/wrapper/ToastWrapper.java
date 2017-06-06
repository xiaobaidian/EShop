package com.feicuiedu.eshop_20170518.base.wrapper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 蔡传飞 on 2017-05-25.
 */

public class ToastWrapper {

    private static Toast mToast;
    private static Context mContext;
    //Toast的初始化，使用之前需要调用
    public static void init(Context context){
        mContext=context;
        mToast=Toast.makeText(context,null,Toast.LENGTH_SHORT);
    }
    //提供show方法展示
    public static void show(int resId,Object... args){
        String string = mContext.getString(resId, args);
        mToast.setText(string);
        mToast.show();
    }

    // 对外提供show方法
    public static void show(CharSequence charSequence,Object... args){
        String text = String.format(charSequence.toString(), args);
        mToast.setText(text);
        mToast.show();

    }
}
