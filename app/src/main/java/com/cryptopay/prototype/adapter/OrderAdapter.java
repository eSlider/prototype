package com.cryptopay.prototype.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.ShopSectionActivity;
import com.cryptopay.prototype.domain.Item;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Item> {


    private List<Item> list;
    private final OrderAdapter.OnItemClick mOnItemClick;
    private String prevHeader = "";

    public interface OnItemClick {
        void onItemClick(@NonNull Item item);
        void update();
    }


    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Item item = (Item) view.getTag();
            mOnItemClick.onItemClick(item);
            mOnItemClick.update();
        }

    };

    public OrderAdapter(Context context, List<Item> list) {
        super(context, R.layout.order_item, R.id.shop_tv_title, list);
        this.mOnItemClick = (OrderAdapter.OnItemClick) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_item, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.shop_card_view);
            viewHolder.header = (TextView) convertView.findViewById(R.id.tv_order_header);
            viewHolder.title = (TextView) convertView.findViewById(R.id.shop_tv_title);
            viewHolder.price = (TextView) convertView.findViewById(R.id.shop_tv_price);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_shop_amount);
            viewHolder.btnPlus = (Button) convertView.findViewById(R.id.btn_shop_plus);
            viewHolder.btnMinus = (Button) convertView.findViewById(R.id.btn_shop_minus);
            viewHolder.ivItem = (ImageView) convertView.findViewById(R.id.iv_item);
//            viewHolder.TodoDate = (TextView) convertView.findViewById(R.id.todo_date);
//            viewHolder.ImagePriority = (ImageView) convertView.findViewById(R.id.imageTask);
            convertView.setTag(viewHolder);
//            viewHolder.hasImage = (TextView) convertView.findViewById(R.id.txt_has_image);
//            viewHolder.checkBox.setOnClickListener(checkListener);
//            viewHolder.checkBox.setOnClickListener(checkListener);

        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Item item = getItem(position);

        if (item.getSection() != null && item.getSection().getTitle()!=null && !item.getSection().getTitle().equals(prevHeader)) {
            holder.header.setVisibility(View.VISIBLE);
            holder.header.setText(item.getSection().getTitle());
            prevHeader = item.getSection().getTitle();
        }

        holder.title.setText(item.getTitle());
        holder.amount.setText(String.valueOf(item.getAmount()));
        byte[] pic = item.getPic();
        if (pic != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            holder.ivItem.setImageBitmap(bMap);
        }

        holder.price.setText(String.format("%.2f", item.getSumma()).replace(",", ".") + " euro");
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setAmount(item.getAmount() + 1);
                holder.amount.setText(String.valueOf(item.getAmount()));
                item.setSumma(item.getSumma()+item.getPriceCurrency());
                holder.price.setText(String.format("%.2f", item.getSumma()).replace(",", ".") + " euro");
                ShopSectionActivity.cartCount++;
                ShopSectionActivity.cartSumma += item.getPriceCurrency();
                mOnItemClick.update();
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    holder.amount.setText(String.valueOf(item.getAmount()));
                    item.setSumma(item.getSumma()-item.getPriceCurrency());
                    holder.price.setText(String.format("%.2f", item.getSumma()).replace(",", ".") + " euro");
                    ShopSectionActivity.cartCount--;
                    ShopSectionActivity.cartSumma -= item.getPriceCurrency();
                    mOnItemClick.update();
                } else if (item.getAmount() == 1) {
                    item.setAmount(0);
                    holder.amount.setText("0");
                    item.setSumma(0d);
                    holder.price.setText("0 euro");
                    ShopSectionActivity.cartCount--;
                    ShopSectionActivity.cartSumma -= item.getPriceCurrency();
                    mOnItemClick.update();
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {

        public CardView cardView;
        public TextView title, price, amount, header;
        public Button btnPlus, btnMinus;
        public ImageView ivItem;
    }
}
