package com.feicuiedu.eshop_20170518.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 蔡传飞 on 2017-05-24.
 */

public abstract class BaseListAdapter<T,V extends BaseListAdapter.ViewHolder> extends BaseAdapter {


    // 数据
    private List<T> mData = new ArrayList<>();
    // 数据的填充
    public void reset(List<T> data){
        mData.clear();
        if (data!=null) mData.addAll(data);
        notifyDataSetChanged();
    }
    // 数据的添加
    public void addAll(List<T> data){
        if (data!=null) mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            // 布局的填充
            convertView= LayoutInflater.from(parent.getContext()).inflate(getItemViewLayout(),parent,false);
            viewHolder = getItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        // 绑定数据，对外提供一个方法，让子类来实现
        viewHolder.bind(position);

        return convertView;
    }
    // 告诉我们子类具体的ViewHolder
    protected abstract V getItemViewHolder(View view);
    // 得到item的布局
    @LayoutRes protected abstract int getItemViewLayout();


    //处理一下ViewHolder试图相关
    public abstract class ViewHolder{
        View mItemView;
        public ViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
            mItemView = itemView;
        }

        protected abstract void bind(int position);

        // 提供一个直接拿到上下文的方法
        protected final Context getContext(){
            return mItemView.getContext();
        }
    }
}
