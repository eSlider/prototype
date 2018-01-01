package com.cryptopay.prototype.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

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

        tvBalance = (TextView) findViewById(R.id.tv_balance);
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
//        updateBalance();

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
        private TextView tvBalance;
        @Override
        protected Void doInBackground(TextView... tv) {
            tvBalance = tv[0];
            if (Constants.wallet != null) {

                String TAG = "web3";
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
                try {
                    EthGetBalance ethGetBalance = web3j
                            .ethGetBalance(Constants.wallet.getAddress(), DefaultBlockParameterName.LATEST)
                            .sendAsync()
                            .get();

                    Constants.balance = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER).floatValue();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "testWeb3: compeleted update balance");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(tvBalance!=null){
                tvBalance.setText(String.format("%.3f",Constants.balance).replace(",","."));
            }
            tvBalance = null;
        }

    }
    public void updateBalance(){
//        Wallet wallet = Constants.wallet;

    }

}
