package com.feicuiedu.eshop_20170518.feature.category;

import android.view.View;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseListAdapter;
import com.feicuiedu.eshop_20170518.network.entity.CategoryBase;

import butterknife.BindView;

/**
 * Created by gqq on 2017/5/23.
 */

// 子分类的适配器
public class ChildrenAdapter extends BaseListAdapter<CategoryBase,ChildrenAdapter.ViewHolder> {

    // item布局
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_children_category;
    }

    // 子类的具体的ViewHolder
    @Override
    protected ChildrenAdapter.ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder{

        @BindView(R.id.text_category)
        TextView mTextCategory;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        // 数据的绑定
        @Override
        protected void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }

}
