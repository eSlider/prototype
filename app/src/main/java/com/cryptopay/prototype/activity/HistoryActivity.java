package com.cryptopay.prototype.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.adapter.HistoryListAdapter;
import com.cryptopay.prototype.domain.Transaction;
import com.cryptopay.prototype.domain.dto.TransactionDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryListAdapter.OnItemClick, SwipeRefreshLayout.OnRefreshListener {
    public static final int LAYOUT = R.layout.activity_history;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Transaction> transactions;
    private HistoryListAdapter mAdapter;
    private LoadingView mLoadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        mAdapter = new HistoryListAdapter(new ArrayList<Transaction>(), this);
        recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        new HistoryTask().execute();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        toolbar.setTitle("History");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(@NonNull Transaction transaction) {
        String address = transaction.getHashTx();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL.TRANSACTION_VIEWER + address));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new HistoryActivity.HistoryTask().execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 4000);
    }

    private class HistoryTask extends AsyncTask<Void, Void, TransactionDTO> {

        @Override
        protected TransactionDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
            return template.getForObject(Constants.URL.FIND_ALL_TRANSACTION, TransactionDTO.class);
            } catch (RuntimeException exception) {
                System.out.println(exception.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(TransactionDTO transactionDTO) {
            if (transactionDTO == null || transactionDTO.getData() == null) {
                showError();
                return;
            }
            mLoadingView.hideLoadingIndicator();
            transactions = transactionDTO.getData();
            Collections.sort(transactions, new Comparator<Transaction>() {
                @Override
                public int compare(Transaction transaction, Transaction t1) {
                    return t1.getDateTx().compareTo(transaction.getDateTx());
                }
            });
            mAdapter.changeDataSet(transactions);
        }
    }


    private void showError() {
        mLoadingView.hideLoadingIndicator();
        Snackbar snackbar = Snackbar.make(recyclerView, "Error loading history", Snackbar.LENGTH_LONG)
                .setAction("Retry", v -> new HistoryActivity.HistoryTask().execute());
        snackbar.setDuration(4000);
        snackbar.show();
    }
}
