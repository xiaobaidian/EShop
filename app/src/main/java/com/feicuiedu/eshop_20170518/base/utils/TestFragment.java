package com.feicuiedu.eshop_20170518.base.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.widgets.SimpleNumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 蔡传飞 on 2017-05-19.
 */

public class TestFragment extends Fragment {
    @BindView(R.id.text)
    TextView text;
    Unbinder unbinder;
    private static final String ARGUMENTS_TEXT = "arguments_text";
    @BindView(R.id.numberPicker)
    SimpleNumberPicker mNumberPicker;


    public static TestFragment newInstance(String text){
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENTS_TEXT,text);
        testFragment.setArguments(bundle);// 传递数据
        return testFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        unbinder = ButterKnife.bind(this, view);

        // 切换到不同的Fragment，展示不同的文本
        //text.setText(getArgumentsText());
        text.setText(String.valueOf(mNumberPicker.getNumber()));
        mNumberPicker.setOnNumberChangeListener(new SimpleNumberPicker.OnNumberChangeListener() {
            @Override
            public void onNumberChanged(int number) {
                text.setText(String.valueOf(number));
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // 拿到传递的数据
    public String getArgumentsText(){
        return getArguments().getString(ARGUMENTS_TEXT);
    }
}
