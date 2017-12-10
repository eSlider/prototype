package com.prototype.prototype.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prototype.prototype.Constants;
import com.prototype.prototype.R;
import com.prototype.prototype.adapter.HistoryAdapter;
import com.prototype.prototype.domain.Transaction;

import java.util.ArrayList;

public class HistoryFragment extends AbstractTabFragment {

    public static final int FRAGMENT = R.layout.fragment_history;
    public ListView lvHistory;
    private HistoryAdapter historyAdapter;
    private ArrayList<Transaction> historyAdapterArrayList = new ArrayList<>();

    public HistoryFragment() {

    }

    public static HistoryFragment getInstance(Context context){
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();

        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.history_tab));

        return fragment;
    }

    public HistoryAdapter getHistoryAdapter() {
        return historyAdapter;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(FRAGMENT, container, false);
//        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        historyAdapter = new HistoryAdapter(context, historyAdapterArrayList, false);
        historyAdapterArrayList = new ArrayList<>();
        //TODO stub заливаем тестовыми транзакциями, убрать потом в продакшене
        Transaction tx1 = new Transaction();
        tx1.setBlockNumber(1234);
        tx1.setFromAddress("0x186a19e5c3cc4c575ce5f2fe2c8adf4e09a6057be7b96d2ea79beeb4cf9c557f");
        tx1.setValue("3.04 ETH");
        Transaction tx2 = new Transaction();
        tx2.setBlockNumber(1234);
        tx2.setFromAddress("0x0");
        tx2.setValue("15.04 ETH");
        historyAdapterArrayList.add(tx1);
        historyAdapterArrayList.add(tx1);
        historyAdapterArrayList.add(tx2);
        historyAdapterArrayList.add(tx2);
        historyAdapterArrayList.add(tx1);

        lvHistory = (ListView) view.findViewById(R.id.lv_history);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String address = historyAdapterArrayList.get(i).getFromAddress();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL.TRANSACTION_VIEWER+address));
                startActivity(intent);
            }
        });
//        listView.setOnItemLongClickListener(new ListViewLongClickListener());
        lvHistory.setEmptyView(view.findViewById(R.id.rl_emptyView));
//        lvHistory(new LinearLayoutManager(context));
        lvHistory.setAdapter(historyAdapter);
        return view;
    }
}
