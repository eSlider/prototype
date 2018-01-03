package com.cryptopay.prototype.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;

public class ChangeActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_change;
    private Button btnGetEth;
    private Toolbar toolbar;
    private TextView tvChangeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
//        Intent intent = getIntent();
        initToolbar();
        tvChangeAddress = (TextView) findViewById(R.id.tv_change_address);
        tvChangeAddress.setText(Constants.wallet.getAddress());
        btnGetEth = (Button) findViewById(R.id.btn_get_eth);
        //ограничиваем получаему сумму с крана не меньше баланса
        if(Constants.balance == 0l){
            btnGetEth.setEnabled(true);
        }else {
            btnGetEth.setEnabled(false);
        }
        btnGetEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Web3Utils.GetTestEth().execute();
                btnGetEth.setEnabled(false);
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.send_toolbar);
        toolbar.setTitle(R.string.change_title);

//        toolbar.setBackgroundColor(getResources().getColor(R.color.whiteColor));
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


}
