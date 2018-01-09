package com.cryptopay.prototype.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.ShopActivity;
import com.cryptopay.prototype.activity.ShopSectionActivity;
import com.cryptopay.prototype.domain.Item;

import org.spongycastle.asn1.x509.Holder;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopViewHolder> {

    private List<Item> list;
    private final ShopListAdapter.OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(@NonNull Item item);
        void update();
    }

    public ShopListAdapter(List<Item> list, ShopListAdapter.OnItemClick mOnItemClick) {
        this.list = list;
        this.mOnItemClick = mOnItemClick;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Item item = (Item) view.getTag();
            mOnItemClick.onItemClick(item);
            mOnItemClick.update();
        }

    };


    public void changeDataSet(@NonNull List<Item> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
        notifyItemChanged(1);
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);
        View view = inflater.inflate(R.layout.shop_item, parent, false);
        return new ShopListAdapter.ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        Item item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.amount.setText(String.valueOf(item.getAmount()));

        byte[] pic = item.getPic();
        if (pic != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            holder.ivItem.setImageBitmap(bMap);
        }

        holder.price.setText(String.format("%.2f", item.getPriceCurrency()).replace(",", ".") + " euro");
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setAmount(item.getAmount()+1);
                holder.amount.setText(String.valueOf(item.getAmount()));
                ShopSectionActivity.cartCount++;
                item.setSumma(item.getSumma()+item.getPriceCurrency());
                holder.price.setText(String.format("%.2f", item.getSumma()).replace(",", ".") + " euro");
                ShopSectionActivity.cartSumma += item.getPriceCurrency();
                mOnItemClick.update();
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount()-1);
                    holder.amount.setText(String.valueOf(item.getAmount()));
                    item.setSumma(item.getSumma()-item.getPriceCurrency());
                    holder.price.setText(String.format("%.2f", item.getSumma()).replace(",", ".") + " euro");
                    ShopSectionActivity.cartCount--;
                    ShopSectionActivity.cartSumma -= item.getPriceCurrency();
                    mOnItemClick.update();
                } else {
                    item.setAmount(item.getAmount()-1);
                    if (holder.llOrder1.getVisibility() == View.GONE) {
                        holder.llOrder1.setVisibility(View.VISIBLE);
                        holder.llOrder2.setVisibility(View.GONE);
                        item.setSumma(0d);
                        ShopSectionActivity.cartCount--;
                        ShopSectionActivity.cartSumma -= item.getPriceCurrency();
                        mOnItemClick.update();
                    }
                }

            }
        });
        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.llOrder1.getVisibility() == View.VISIBLE) {
                    holder.llOrder1.setVisibility(View.GONE);
                    holder.llOrder2.setVisibility(View.VISIBLE);
                    item.setAmount(item.getAmount()+1);
                    holder.amount.setText(String.valueOf(item.getAmount()));
                    item.setSumma(item.getSumma()+item.getPriceCurrency());
                    holder.price.setText(String.format("%.2f", item.getSumma()).replace(",", ".") + " euro");
                    ShopSectionActivity.cartCount++;
                    ShopSectionActivity.cartSumma += item.getPriceCurrency();
                    mOnItemClick.update();
                }

            }
        });

        if(item.getAmount()>=1){
            if (holder.llOrder1.getVisibility() == View.VISIBLE) {
                holder.llOrder1.setVisibility(View.GONE);
                holder.llOrder2.setVisibility(View.VISIBLE);
                mOnItemClick.update();
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Item> list) {

        this.list = list;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView title, price, amount, order;
        public Button btnPlus, btnMinus;
        public LinearLayout llOrder1, llOrder2;
        public ImageView ivItem;
//        public int i = 0;
//        ImageView pic;

        public ShopViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.shop_card_view);
            order = (TextView) itemView.findViewById(R.id.shop_tv_order);
            llOrder1= (LinearLayout) itemView.findViewById(R.id.ll_order1);
            llOrder2= (LinearLayout) itemView.findViewById(R.id.ll_order2);
            title = (TextView) itemView.findViewById(R.id.shop_tv_title);
            price = (TextView) itemView.findViewById(R.id.shop_tv_price);
            amount = (TextView) itemView.findViewById(R.id.tv_shop_amount);
            btnPlus = (Button) itemView.findViewById(R.id.btn_shop_plus);
            btnMinus = (Button) itemView.findViewById(R.id.btn_shop_minus);
            ivItem = (ImageView) itemView.findViewById(R.id.iv_item);

        }
    }
}
