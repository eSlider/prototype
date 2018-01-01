package com.cryptopay.prototype.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;

public class SettingActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_setting;
    private Toolbar toolbar;

    private CheckBox cbSettingShowSplash;

    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        sPref = getPreferences(MODE_PRIVATE);
        cbSettingShowSplash = (CheckBox) findViewById(R.id.cb_setting_show_spash);
        cbSettingShowSplash.setChecked(sPref.getBoolean(Constants.settings_show_splash, true));
    }


    @Override
    public void onBackPressed() {

        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.settings_show_splash, cbSettingShowSplash.isChecked());
        ed.apply();
        super.onBackPressed();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitle(R.string.setting_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }


}
