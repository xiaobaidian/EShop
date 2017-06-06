package com.feicuiedu.eshop_20170518.feature.category;

import android.view.View;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseListAdapter;
import com.feicuiedu.eshop_20170518.network.entity.CategoryPrimary;

import butterknife.BindView;

/**
 * Created by gqq on 2017/5/23.
 */

// 一级分类的适配器
public class CategoryAdapter extends BaseListAdapter<CategoryPrimary,CategoryAdapter.ViewHolder> {

    // item的布局
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_primary_category;
    }

    // viewHolder
    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.text_category)
        TextView mTextCategory;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        // 绑定数据
        @Override
        protected void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }
}
