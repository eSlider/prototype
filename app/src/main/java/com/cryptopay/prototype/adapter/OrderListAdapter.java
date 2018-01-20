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
import com.cryptopay.prototype.domain.OrderItem;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private List<OrderItem> items;
    private final OrderListAdapter.OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(@NonNull Item item);
    }

    public OrderListAdapter(List<OrderItem> items, OrderListAdapter.OnItemClick mOnItemClick) {
        this.items = items;
        this.mOnItemClick = mOnItemClick;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Item item = (Item) view.getTag();
            mOnItemClick.onItemClick(item);
        }
    };


    public void changeDataSet(@NonNull List<OrderItem> items) {
        items.clear();
        items.addAll(items);
        notifyDataSetChanged();
        notifyItemChanged(1);
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);
        View view = inflater.inflate(R.layout.shop_item, parent, false);
        return new OrderListAdapter.OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderListViewHolder holder, final int position) {

        Item item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.price.setText(String.format("%.2f", item.getPrice()).replace(",", ".") + " euro");
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<OrderItem> items) {

        this.items = items;
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView price;
//        ImageView pic;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.shop_card_view);
            title = (TextView) itemView.findViewById(R.id.shop_tv_title);
            price = (TextView) itemView.findViewById(R.id.shop_tv_title);
//            pic = (ImageView) itemView.findViewById(R.id.shop_iv_pic);
        }
    }
}
