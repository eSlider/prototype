package com.prototype.prototype.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prototype.prototype.R;

public class PurseActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_purse;
    private Toolbar toolbar;
    private TextView tvBalance;

    private Button btnSend, btnReceive, btnChange;
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        Intent intent = getIntent();
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvBalance.setText(""+this.balance +" ETH");
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
                        break;
                    default:break;
                }
            }
        };

        btnSend.setOnClickListener(onClickListener);
        btnReceive.setOnClickListener(onClickListener);
        btnChange.setOnClickListener(onClickListener);
    }

    public void switchSend(){
        Intent intent = new Intent(PurseActivity.this, SendActivity.class);
        intent.putExtra("balance", this.balance);
        startActivity(intent);
    }

    public void switchReceive(){
        Intent intent = new Intent(PurseActivity.this, QRGeneratorActivity.class);
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


}
