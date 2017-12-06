package com.prototype.prototype.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prototype.prototype.R;
import com.prototype.prototype.dto.AdvertDTO;

import java.util.List;

public class AdvertListAdapter extends RecyclerView.Adapter<AdvertListAdapter.AdvertViewHolder> {

    private List<AdvertDTO> data;

    public AdvertListAdapter(List<AdvertDTO> data) {
        this.data = data;
    }

    @Override
    public AdvertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);

        return new AdvertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvertViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<AdvertDTO> data) {
        this.data = data;
    }

    public static class AdvertViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;

        public AdvertViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
