package com.cryptopay.prototype.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.db.sqllitedomain.Template;
import com.cryptopay.prototype.domain.Transaction;

import org.web3j.utils.Convert;

import java.util.List;

public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.TemplateViewHolder> {

    private List<Template> list;
    private final TemplateListAdapter.OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(@NonNull Template template);
    }

    public TemplateListAdapter(List<Template> list, TemplateListAdapter.OnItemClick mOnItemClick) {
        this.list = list;
        this.mOnItemClick = mOnItemClick;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Template template = (Template) view.getTag();
            mOnItemClick.onItemClick(template);
        }
    };

    public void changeDataSet(@NonNull List<Template> templates) {
        list.clear();
        list.addAll(templates);
        notifyDataSetChanged();
        notifyItemChanged(1);
    }

    @Override
    public TemplateListAdapter.TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_item, parent, false);
        return new TemplateListAdapter.TemplateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TemplateViewHolder holder, int position) {
        Template template = list.get(position);

        holder.tvName.setText(template.getName());
        holder.tvAddress.setText(template.getAddress());

        holder.itemView.setTag(template);
        holder.itemView.setOnClickListener(mInternalListener);
    }


    @Override
    public int getItemCount() {
            return list.size();
    }


    public static class TemplateViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public TextView tvName;
        public TextView tvAddress;

        public TemplateViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.template_card_view);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}
