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
import com.cryptopay.prototype.domain.Section;

import java.util.List;

public class ShopSectionListAdapter extends RecyclerView.Adapter<ShopSectionListAdapter.ShopSectionViewHolder> {

    private List<Section> list;
    private final ShopSectionListAdapter.OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(@NonNull Section section);
    }

    public ShopSectionListAdapter(List<Section> list, ShopSectionListAdapter.OnItemClick mOnItemClick) {
        this.list = list;
        this.mOnItemClick = mOnItemClick;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Section section = (Section) view.getTag();
            mOnItemClick.onItemClick(section);
        }
    };


    public void changeDataSet(@NonNull List<Section> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
        notifyItemChanged(1);
    }

    @Override
    public ShopSectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);
        View view = inflater.inflate(R.layout.section_item, parent, false);
        return new ShopSectionListAdapter.ShopSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopSectionViewHolder holder, final int position) {
        Section section = list.get(position);
        holder.title.setText(section.getTitle());
        holder.itemView.setTag(section);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Section> list) {

        this.list = list;
    }

    public static class ShopSectionViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;

        public ShopSectionViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.section_card_view);
            title = (TextView) itemView.findViewById(R.id.section_tv_title);
        }
    }
}
