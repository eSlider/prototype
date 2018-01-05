package com.cryptopay.prototype.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;

public class InfoActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_info;
    private Toolbar toolbar;
    private TextView tvInfoDescription, tvInfoAddress, tvInfoAddAddress, tvInfoTel, tvInfoSite, tvInfoEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        tvInfoDescription = (TextView) findViewById(R.id.tv_info_description);
        tvInfoAddress = (TextView) findViewById(R.id.tv_info_address);
        tvInfoAddAddress = (TextView) findViewById(R.id.tv_info_addaddress);
        tvInfoTel = (TextView) findViewById(R.id.tv_info_tel);
        tvInfoSite = (TextView) findViewById(R.id.tv_info_site);
        tvInfoEmail = (TextView) findViewById(R.id.tv_info_email);

        tvInfoDescription.setText(Constants.advert.getDescription());
        tvInfoAddress.setText("Address: " +Constants.advert.getAddress());
        tvInfoAddAddress.setText("Addition address: "+Constants.advert.getAddAddress());
        tvInfoTel.setText("Phone number: "+Constants.advert.getTel());
        tvInfoSite.setText("Site page: "+Constants.advert.getSite());
        tvInfoEmail.setText("Email: "+Constants.advert.getEmail());
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitle(Constants.advert.getTitle());
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }


}
