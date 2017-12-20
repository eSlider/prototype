package com.prototype.prototype.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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

import java.util.List;

public class AdvertListAdapter extends RecyclerView.Adapter<AdvertListAdapter.AdvertViewHolder> {

//    private AdvertDTO advertDTO = new AdvertDTO();
    private List<Advert> list;
    private Context context;
    private final OnItemClick mOnItemClick;

    public interface OnItemClick {

        void onItemClick(@NonNull Advert advert);

    }

    public AdvertListAdapter(List<Advert> list, OnItemClick mOnItemClick) {
        this.list = list;
        this.mOnItemClick = mOnItemClick;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Advert advert = (Advert) view.getTag();
            mOnItemClick.onItemClick(advert);
        }
    };

    public void changeDataSet(@NonNull List<Advert> adverts) {
        list.clear();
        list.addAll(adverts);
        notifyDataSetChanged();
        notifyItemChanged(1);
    }

    @Override
    public AdvertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);
        View view = inflater.inflate(R.layout.advert_item, parent, false);
        return new AdvertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvertViewHolder holder, final int position) {
        Advert advert = list.get(position);
        holder.title.setText(advert.getTitle());
        holder.description.setText(advert.getDescription());
        byte[] pic = advert.getPic();
        Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
        holder.pic.setImageBitmap(bMap);
        holder.itemView.setTag(advert);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Advert> list) {
        this.list = list;
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
