package com.feicuiedu.eshop_20170518.base.widgets.banner;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feicuiedu.eshop_20170518.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 蔡传飞 on 2017-05-24.
 */

public abstract class BannerAdapter<T> extends PagerAdapter {
    // 数据集合
    private List<T> mData = new ArrayList<>();

    /**
     * banner的适配器：为了更广泛的应用，通用的基类
     * 数据不是确定，视图是确定
     * 数据用泛型来表示
     */

    //数据的填充
    public void reset(List<T> data){
        mData.clear();
        if (data!=null) mData.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ViewHolder viewHolder = (ViewHolder) object;
        return view==viewHolder.itemView;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner,container,false);
        container.addView(view);

        ViewHolder viewHolder = new ViewHolder(view);

        // 绑定视图和数据
        bind(viewHolder,mData.get(position));

        return viewHolder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewHolder viewHolder = (ViewHolder) object;
        container.removeView(viewHolder.itemView);
    }

    // 必须让子类实现的方法：数据的填充
    protected abstract void bind(ViewHolder holder,T data);

    public static class ViewHolder{

        @BindView(R.id.image_banner_item)
        public ImageView mImageView;
        private View itemView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }
    }
}
