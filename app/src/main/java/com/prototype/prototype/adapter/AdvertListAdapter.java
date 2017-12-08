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

public class AdvertListAdapter extends RecyclerView.Adapter<AdvertListAdapter.AdvertViewHolder> {

    private AdvertDTO advertDTO = new AdvertDTO();
    private Context context;

    public AdvertListAdapter() {
    }

    @Override
    public AdvertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);
        context = parent.getContext();
        return new AdvertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvertViewHolder holder, final int position) {
        final Advert advert = advertDTO.getData().get(position);
        holder.title.setText(advert.getTitle());
        holder.description.setText(advert.getDescription());
        byte[] pic = advert.getPic();
        Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
        holder.pic.setImageBitmap(bMap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context == null)
                    return;
                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;

                    mainActivity.switchContent(advert);
                }
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

    public static class AdvertViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView description;
        ImageView pic;

        public AdvertViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            pic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }
    }
}
