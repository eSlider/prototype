package com.prototype.prototype.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prototype.prototype.R;
import com.prototype.prototype.domain.Transaction;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Transaction>{

    private List<Transaction> list;


    public HistoryAdapter(Context context, List<Transaction> list, boolean isPicker) {
        super(context, R.layout.history_item, R.id.tv_address, list);
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
//            viewHolder.txtUnit = (TextView) convertView.findViewById(R.id.txt_unit);
//            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_source);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        Transaction transaction = list.get(position);

//        if(isPicker){
////            if(list.contains(sprElement)){
//            holder.txtUnit.setText(sprElement.getUnit());
//            holder.txtAmount.setText(Integer.toString(sprElement.getAmount()));
//
////         }
//        }
        if(transaction.getFromAddress().equals("0x0")) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
        }else {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }
        holder.tvAddress.setText(transaction.getFromAddress());
        holder.tvAmount.setText(transaction.getValue());
        holder.tvDate.setText(String.valueOf(transaction.getBlockNumber()));

//            if(!sprElement.isRemove()){
//                holder.imageView.setImageResource(R.drawable.element);
//            }else{
//                holder.imageView.setImageResource(R.drawable.element_remove);
//            }

        return convertView;
    }

    static class ViewHolder {
        public TextView tvAmount;
        public TextView tvDate;
        public TextView tvAddress;



    }
}
