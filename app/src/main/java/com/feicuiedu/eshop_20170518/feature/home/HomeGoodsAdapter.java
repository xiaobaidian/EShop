package com.feicuiedu.eshop_20170518.feature.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseListAdapter;
import com.feicuiedu.eshop_20170518.feature.goods.GoodsActivity;
import com.feicuiedu.eshop_20170518.network.entity.CategoryHome;
import com.feicuiedu.eshop_20170518.network.entity.Picture;
import com.feicuiedu.eshop_20170518.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by 蔡传飞 on 2017-05-25.
 */

public class HomeGoodsAdapter extends BaseListAdapter<CategoryHome,HomeGoodsAdapter.ViewHolder> {
    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_home_goods;
    }

    class ViewHolder extends BaseListAdapter.ViewHolder{

        @BindView(R.id.text_category)
        TextView mTvCategory;

        @BindViews({
                R.id.image_goods_01,
                R.id.image_goods_02,
                R.id.image_goods_03,
                R.id.image_goods_04
        })
        ImageView[] mImageViews;
        private CategoryHome mCategoryHome;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(int position) {
            // 拿到item数据
            mCategoryHome = getItem(position);
            mTvCategory.setText(mCategoryHome.getName());

            // 简单的商品对象
            final List<SimpleGoods> goodsList = mCategoryHome.getHotGoodsList();

            for (int i=0;i<mImageViews.length;i++){

                // 取出list里面的图片
                Picture picture = goodsList.get(i).getImg();
                // Picasso加载图片
                Picasso.with(getContext()).load(picture.getLarge()).into(mImageViews[i]);

                // 设置点击事件
                final int index = i;
                mImageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情页
                        SimpleGoods simpleGoods = goodsList.get(index);
                        Intent intent = GoodsActivity.getStartIntent(getContext(), simpleGoods.getId());
                        getContext().startActivity(intent);
                    }
                });
            }
        }

        @OnClick(R.id.text_category)
        void onClick(){
            Toast.makeText(getContext(), mCategoryHome.getName(), Toast.LENGTH_SHORT).show();
        }
    }


}
