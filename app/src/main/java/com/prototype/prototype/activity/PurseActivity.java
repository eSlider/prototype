package com.prototype.prototype.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prototype.prototype.Constants;
import com.prototype.prototype.R;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

public class PurseActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_purse;
    private Toolbar toolbar;
    private TextView tvBalance;

    private Button btnSend, btnReceive, btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        Intent intent = getIntent();

        new Utility.GenerateNewWallet();

        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvBalance.setText(""+ Constants.balance +" wei");
        btnSend = (Button) findViewById(R.id.btn_send);
        btnReceive = (Button) findViewById(R.id.btn_receive);
        btnChange = (Button) findViewById(R.id.btn_change);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_send:
                        switchSend();
                        break;
                    case R.id.btn_receive:
                        switchReceive();
                        break;
                    case R.id.btn_change:
                        switchChange();
                        break;
                    default:break;
                }
            }
        };

        btnSend.setOnClickListener(onClickListener);
        btnReceive.setOnClickListener(onClickListener);
        btnChange.setOnClickListener(onClickListener);
        new UpdateBalanceAsync().execute(tvBalance);
    }

    public void switchSend(){
        Intent intent = new Intent(PurseActivity.this, SendActivity.class);
        startActivity(intent);
    }

    public void switchReceive(){
        Intent intent = new Intent(PurseActivity.this, QRGeneratorActivity.class);
        startActivity(intent);
    }
    public void switchChange(){
        Intent intent = new Intent(PurseActivity.this, ChangeActivity.class);
        startActivity(intent);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.purse_toolbar);
        toolbar.setTitle(R.string.purse_title);

//        toolbar.setBackgroundColor(getResources().getColor(R.color.whiteColor));
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });
//        toolbar.inflateMenu(R.menu.shop_menu);
    }

    public static class UpdateBalanceAsync extends AsyncTask<TextView, Void, Void> {
        TextView tvBalance;
        @Override
        protected Void doInBackground(TextView... textViews) {
            tvBalance = textViews[0];
            new Web3Utils.GetBalanceWallet().execute();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(tvBalance!=null){
                tvBalance.setText(""+ Constants.balance +" wei");
            }
        }

    }

}
