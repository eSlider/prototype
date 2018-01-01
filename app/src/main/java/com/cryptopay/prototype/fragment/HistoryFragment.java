package com.cryptopay.prototype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cryptopay.prototype.R;
import com.cryptopay.prototype.adapter.HistoryListAdapter;
import com.cryptopay.prototype.domain.dto.TransactionDTO;

public class HistoryFragment extends AbstractTabFragment {

    public static final int FRAGMENT = R.layout.fragment_history;
    public ListView lvHistory;
    private HistoryListAdapter historyListAdapter;


    public HistoryFragment() {
//        this.historyListAdapter = new HistoryListAdapter(context, new ArrayList<Transaction>());
    }

    public static HistoryFragment getInstance(Context context) {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();

        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.history_tab));

        return fragment;
    }

    public HistoryListAdapter getHistoryListAdapter() {
        return historyListAdapter;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void refreshList(TransactionDTO transactionDTO) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }
}
