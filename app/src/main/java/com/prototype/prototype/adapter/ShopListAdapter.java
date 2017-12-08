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

import com.prototype.prototype.R;
import com.prototype.prototype.activity.MainActivity;
import com.prototype.prototype.domain.Advert;
import com.prototype.prototype.domain.dto.AdvertDTO;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopViewHolder> {

    private AdvertDTO advertDTO = new AdvertDTO();
    private Context context;

    public ShopListAdapter() {
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        context = parent.getContext();
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        final Advert advert = advertDTO.getData().get(position);
        holder.title.setText(advert.getTitle());
        holder.description.setText(advert.getDescription());
        byte[] pic = advert.getPic();
        Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
        holder.pic.setImageBitmap(bMap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (context == null)
//                    return;
//                if (context instanceof MainActivity) {
//                    MainActivity mainActivity = (MainActivity) context;
//
//                    mainActivity.switchContent(advert);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertDTO.getData().size();
    }

    public void setData(AdvertDTO advertDTO) {
        this.advertDTO = advertDTO;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView description;
        ImageView pic;

        public ShopViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.shop_card_view);
            title = (TextView) itemView.findViewById(R.id.shop_tv_title);
            description = (TextView) itemView.findViewById(R.id.shop_tv_description);
            pic = (ImageView) itemView.findViewById(R.id.shop_iv_pic);
        }
    }
}
