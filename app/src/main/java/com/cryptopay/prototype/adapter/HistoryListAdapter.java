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
import com.cryptopay.prototype.domain.Transaction;
import com.cryptopay.prototype.R;

import org.web3j.utils.Convert;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {

    private List<Transaction> list;
    private final HistoryListAdapter.OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(@NonNull Transaction transaction);
    }

    public HistoryListAdapter(List<Transaction> list, HistoryListAdapter.OnItemClick mOnItemClick) {
        this.list = list;
        this.mOnItemClick = mOnItemClick;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Transaction transaction = (Transaction) view.getTag();
            mOnItemClick.onItemClick(transaction);
        }
    };

    public void changeDataSet(@NonNull List<Transaction> transactions) {
        list.clear();
        list.addAll(transactions);
        notifyDataSetChanged();
        notifyItemChanged(1);
    }

    @Override
    public HistoryListAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryListAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryListAdapter.HistoryViewHolder holder, int position) {
        Transaction transaction = list.get(position);

        if (!transaction.getFromAddress().equals(Constants.wallet.getAddress())) {
            holder.cardView.setBackgroundColor(Color.GREEN);
            holder.tvAddress.setText("from " + transaction.getFromAddress());
        } else {
            holder.cardView.setBackgroundColor(Color.YELLOW);
            holder.tvAddress.setText("to " + transaction.getToAddress());
        }

        holder.tvAmount.setText(Convert.fromWei(transaction.getValue(), Convert.Unit.ETHER).toPlainString() + " cc€");
        holder.tvDate.setText(String.valueOf(transaction.getDateTx()));

        holder.itemView.setTag(transaction);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    @Override
    public int getItemCount() {
            return list.size();
    }


    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public TextView tvAmount;
        public TextView tvDate;
        public TextView tvAddress;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.history_card_view);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}
