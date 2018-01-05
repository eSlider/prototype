package com.cryptopay.prototype.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptopay.prototype.R;
import com.cryptopay.prototype.domain.Item;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopViewHolder> {

    private List<Item> list;
    private final ShopListAdapter.OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(@NonNull Item item);
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
        holder.description.setText(String.format("%.2f",item.getPriceCurrency()).replace(",",".")+" euro");
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Item> list) {

        this.list = list;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView description;
//        ImageView pic;

        public ShopViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.shop_card_view);
            title = (TextView) itemView.findViewById(R.id.shop_tv_title);
            description = (TextView) itemView.findViewById(R.id.shop_tv_description);
//            pic = (ImageView) itemView.findViewById(R.id.shop_iv_pic);
        }
    }
}
