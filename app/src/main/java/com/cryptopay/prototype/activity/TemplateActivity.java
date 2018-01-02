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
import com.cryptopay.prototype.adapter.TemplateListAdapter;
import com.cryptopay.prototype.db.DBHelper;
import com.cryptopay.prototype.db.DBQueryManager;
import com.cryptopay.prototype.db.sqllitedomain.Template;
import com.cryptopay.prototype.domain.Transaction;
import com.cryptopay.prototype.domain.dto.TransactionDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TemplateActivity extends AppCompatActivity implements TemplateListAdapter.OnItemClick {
    public static final int LAYOUT = R.layout.activity_template;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private List<Template> templates;
    private TemplateListAdapter mAdapter;
    private LoadingView mLoadingView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        mAdapter = new TemplateListAdapter(new ArrayList<Template>(), this);
        recyclerView = (RecyclerView) findViewById(R.id.template_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();

        dbHelper = new DBHelper(this);

        templates = dbHelper.query().getTemplates(null, null, null);
        mAdapter.changeDataSet(templates);
        mLoadingView.hideLoadingIndicator();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.template_toolbar);
        toolbar.setTitle("Templates");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static final String ADDRESS = "Address from template";

    @Override
    public void onItemClick(@NonNull Template template) {
        String address = template.getAddress();
        Intent intent = new Intent();
        intent.putExtra(ADDRESS, address);
        setResult(RESULT_OK, intent);
        finish();
    }



}
