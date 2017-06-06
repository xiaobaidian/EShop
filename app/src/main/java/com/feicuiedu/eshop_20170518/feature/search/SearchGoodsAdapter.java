package com.feicuiedu.eshop_20170518.feature.search;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.eshop_20170518.R;
import com.feicuiedu.eshop_20170518.base.BaseListAdapter;
import com.feicuiedu.eshop_20170518.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by 蔡传飞 on 2017-05-27.
 */

// 搜索商品列表的适配器
public class SearchGoodsAdapter extends BaseListAdapter<SimpleGoods, SearchGoodsAdapter.ViewHolder> {

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_search_goods;
    }

    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {

        @BindView(R.id.image_goods)
        ImageView mImageGoods;
        @BindView(R.id.text_goods_name)
        TextView mTextGoodsName;
        @BindView(R.id.text_goods_price)
        TextView mTextGoodsPrice;
        @BindView(R.id.text_market_price)
        TextView mTextMarketPrice;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(int position) {

            // 名称和价格
            SimpleGoods simpleGoods = getItem(position);
            mTextGoodsName.setText(simpleGoods.getName());
            mTextGoodsPrice.setText(simpleGoods.getShopPrice());

            // 设置商场的价格：原有的字符串加了一个删除线,采用字符串的一个处理类:SpannableString
            String marketPrice = simpleGoods.getMarketPrice();
            SpannableString spannableString = new SpannableString(marketPrice);// 传入要出处理的字符
            // 设置删除线：StrikethroughSpan
            spannableString.setSpan(new StrikethroughSpan(),0,marketPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTextMarketPrice.setText(spannableString);// 处理好的文本设置给textXiew

            // 图片的加载
            Picasso.with(getContext()).load(simpleGoods.getImg().getLarge()).into(mImageGoods);

        }
    }

}