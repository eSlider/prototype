package com.prototype.prototype.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prototype.prototype.Constants;
import com.prototype.prototype.R;
import com.prototype.prototype.activity.MainActivity;
import com.prototype.prototype.domain.Advert;
import com.prototype.prototype.domain.Item;
import com.prototype.prototype.domain.dto.AdvertDTO;
import com.prototype.prototype.domain.dto.ItemDTO;

import org.web3j.utils.Convert;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopViewHolder> {

    private ItemDTO itemDTO = new ItemDTO();
    private Context context;

    public ShopListAdapter() {
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        context = parent.getContext();
        itemDTO = Constants.itemDTO;
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        final Item item = itemDTO.getData().get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(Convert.fromWei(Long.toString(item.getPrice()), Convert.Unit.ETHER).toPlainString() + " eth");
//        byte[] pic = item.getPic();
//        Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
//        holder.pic.setImageBitmap(bMap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = itemDTO.getData().get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(itemDTO == null){
            return Constants.itemDTO.getData().size();
        }
        return itemDTO.getData().size();
    }

    public void setData(ItemDTO itemDTO) {

        this.itemDTO = itemDTO;
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
