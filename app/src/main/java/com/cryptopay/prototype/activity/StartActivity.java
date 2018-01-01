package com.cryptopay.prototype.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chaos.view.PinView;
import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.fragment.SplashFragment;
import com.jungly.gridpasswordview.GridPasswordView;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.domain.Wallet;

import java.math.BigInteger;


public class StartActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_start;

    private PinView pinView;
    private GridPasswordView gpv, pswViewPass1, pswViewPass2;
    private SharedPreferences sPref;
    private LinearLayout llStart1;
    private LinearLayout llStart2;
    private ImageView ivLogo;
    private ConstraintLayout clContainer;
    private FragmentManager fragmentManager;
    private String newPassword;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        sPref = getPreferences(MODE_PRIVATE);
        clContainer = (ConstraintLayout) findViewById(R.id.start_container);
        llStart1 = (LinearLayout) findViewById(R.id.ll_start1);
        llStart2 = (LinearLayout) findViewById(R.id.ll_start2);
        fragmentManager = getSupportFragmentManager();
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

//        if (sPref.getBoolean(Constants.settings_show_splash, false)) {
//            runSplash();
//        }
        pswViewPass1 = (GridPasswordView) findViewById(R.id.pswViewPass1);
        pswViewPass2 = (GridPasswordView) findViewById(R.id.pswViewPass2);
        gpv = (GridPasswordView) findViewById(R.id.pswView);

        initWallet();
//        gpv.requestFocus();
        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() != 4) {
                    clContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onInputFinish(String psw) {
//                if(psw.equals(Constants.wallet_password))

                if (psw.equals(Constants.wallet.getPassword())) {
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    clContainer.setBackgroundColor(getResources().getColor(R.color.passwordAccess));
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    clContainer.setBackgroundColor(getResources().getColor(R.color.passwordDenied));
                }
            }
        });
        pswViewPass1.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                pswViewPass2.requestFocus();
            }
        });
        pswViewPass2.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                if (pswViewPass2.getPassWord().equals(pswViewPass1.getPassWord())) {
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    newPassword = pswViewPass2.getPassWord();
//                    mLoadingView.showLoadingIndicator();
                    AsyncTask<Object, Void, Wallet> execute = new Web3Utils.GenerateNewWallet();
                    execute.execute(getApplicationContext().getFilesDir().getAbsolutePath(), newPassword, getPreferences(MODE_PRIVATE));
//                    while (Constants.wallet.getAddress()==null);
//                    try {
//                        TimeUnit.SECONDS.sleep(3);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);

//                    mLoadingView.hideLoadingIndicator();
                } else {
                    pswViewPass2.clearPassword();
                    pswViewPass1.clearPassword();
                    pswViewPass1.requestFocus();
                }
            }
        });

    }

    private void initWallet() {

        Constants.wallet = new Wallet();
        if (!sPref.getString(Constants.wallet_address, "").equals("")) {
            Constants.wallet.setAddress(sPref.getString(Constants.wallet_address, ""));
            Constants.wallet.setPassword(sPref.getString(Constants.wallet_password, ""));
            Constants.wallet.setFile(sPref.getString(Constants.wallet_file, ""));
            Constants.wallet.setPublicKey(new BigInteger(sPref.getString(Constants.wallet_publicKey, "0")));
            Constants.wallet.setPrivateKey(new BigInteger(sPref.getString(Constants.wallet_privateKey, "0")));
            new Web3Utils.GetBalanceWallet().execute();
            llStart1.setVisibility(View.VISIBLE);
            if (gpv.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(gpv, 2);
            }
        } else {
            llStart2.setVisibility(View.VISIBLE);
            if (pswViewPass1.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(pswViewPass1, InputMethodManager.SHOW_IMPLICIT);
            }

        }

    //TODO: изменить наименование валюты eth -> cc$  ccCoin -> Cryptopaid
    }

    public void runSplash() {
        SplashFragment splashFragment = new SplashFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.start_container, splashFragment)
                .addToBackStack(null)
                .commit();
    }




//
//        pinView = (PinView) findViewById(R.id.pinView);
//        pinView.setTextColor(
//                ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()));
//        pinView.setTextColor(
//                ResourcesCompat.getColorStateList(getResources(), R.color.plus, getTheme()));
//        pinView.setLineColor(
//                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()));
//        pinView.setLineColor(
//                ResourcesCompat.getColorStateList(getResources(), R.color.plus, getTheme()));
//        pinView.setItemCount(4);
//        pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
//        pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
//        pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
//        pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
//        pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
//        pinView.setAnimationEnable(true);// start animation when adding text
}



