package com.cryptopay.prototype.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.db.DBHelper;
import com.cryptopay.prototype.db.sqllitedomain.Template;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class SendActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_send;
    private Toolbar toolbar;
    private EditText etAddressRecepient, etAmount, etTemplate;
    private CheckBox cbSaveTemplate;
    private TextView tvNameTemplate, tvBalance, tvComission;
    private Button btnCopyAddressRecepient;
    private FloatingActionButton fabSend;
    public static final int CHOOSE_ADDRESS_FROM_QR = 100;
    public static final int CHOOSE_ADDRESS_FROM_TEMPLATES = 101;
    private double eth_price, totalPrice;
    private LoadingView mLoadingView;
    private Intent intent;
    private String comission = "0";
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        intent = getIntent();
        totalPrice = intent.getDoubleExtra("totalPrice", .0d);
        eth_price = intent.getDoubleExtra("eth_price", .0d);
        address = intent.getStringExtra("address");

        initToolbar();
        etAddressRecepient = (EditText) findViewById(R.id.et_address_recepient);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etTemplate = (EditText) findViewById(R.id.et_template);
        cbSaveTemplate = (CheckBox) findViewById(R.id.cb_save_template);
        tvNameTemplate = (TextView) findViewById(R.id.tv_name_template);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvComission = (TextView) findViewById(R.id.tv_comission);
        fabSend = (FloatingActionButton) findViewById(R.id.fab_send);
        tvBalance.setText(getResources().getString(R.string.text_your_balance) + " :" + String.format("%.18f", Constants.balance).replace(",", ".") + " eth");
        btnCopyAddressRecepient = (Button) findViewById(R.id.btnCopyAddressRecepient);
        btnCopyAddressRecepient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("address", etAddressRecepient.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

//        etAddressRecepient.setText("0xE44c4cf797505AF1527B11e4F4c6f95531b4Be24");
        if (totalPrice > 0.0d && !address.isEmpty()) {

            etAddressRecepient.setText(address.trim());
            comission = String.format("%.18f" ,totalPrice * 0.01).replace(",", ".");
            tvComission.setText("Comission: " + comission.replace(",", ".") + " (1%)");
            etAmount.setText(String.format("%.18f", totalPrice).replace(",", "."));
            fabSend.requestFocus();
            etAmount.setEnabled(false);
            etAddressRecepient.setEnabled(false);
        }
        cbSaveTemplate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tvNameTemplate.setVisibility(View.VISIBLE);
                    etTemplate.setVisibility(View.VISIBLE);
                } else {
                    tvNameTemplate.setVisibility(View.INVISIBLE);
                    etTemplate.setVisibility(View.INVISIBLE);
                    etTemplate.setText("");
                }
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPrice = Double.parseDouble(etAmount.getText().toString());
                if (etAddressRecepient.equals("")) {
                    Toast.makeText(SendActivity.this, R.string.danger_address_recepient_isempty, Toast.LENGTH_LONG).show();

                } else if (Constants.balance <= (Double.parseDouble(etAmount.getText().toString().replace(",", "."))+Double.parseDouble(comission.replace(",", ".")))) {
                    Toast.makeText(SendActivity.this, R.string.danger_not_enough_money, Toast.LENGTH_LONG).show();

                } else {
//                    BigDecimal amount = Convert.toWei(etAmount.getText(), Convert.Unit.ETHER);
                    mLoadingView.showLoadingIndicator();
                    new Send().execute(etAddressRecepient.getText().toString(), etAmount.getText().toString(), String.valueOf(eth_price));
                    //sabe template
                    if (cbSaveTemplate.isChecked() && !etTemplate.getText().equals("")) {
                        new DBHelper(SendActivity.this).saveTemplate(new Template(etTemplate.getText().toString(), etAddressRecepient.getText().toString()));
                    }


                }
            }
        });
//        fabSend.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.send_toolbar);
        toolbar.setTitle(R.string.send_title);

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
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.qrcode:
                        intent = new Intent(SendActivity.this, QRReadActivity.class);
                        startActivityForResult(intent, CHOOSE_ADDRESS_FROM_QR);
                        break;
                    case R.id.templates:
                        intent = new Intent(SendActivity.this, TemplateActivity.class);
                        startActivityForResult(intent, CHOOSE_ADDRESS_FROM_TEMPLATES);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.send_menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (etAddressRecepient.isEnabled()) {
            if (requestCode == CHOOSE_ADDRESS_FROM_QR) {
                if (resultCode == RESULT_OK) {
                    etAddressRecepient.setText(data.getStringExtra(QRReadActivity.ADDRESS));
                }
            } else if (requestCode == CHOOSE_ADDRESS_FROM_TEMPLATES) {
                if (resultCode == RESULT_OK) {
                    etAddressRecepient.setText(data.getStringExtra(TemplateActivity.ADDRESS));
                }
            }
        }
    }


    private class Send extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... args) {
            try {
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
                Credentials credentials =
                        WalletUtils.loadCredentials(
                                Constants.wallet.getPassword(),
                                new File(Constants.wallet.getFile()));//TODO заменить на внутренний файл-ключ

                BigDecimal amount = Convert.toWei(args[1], Convert.Unit.ETHER);


                System.out.println("Sending " + args[1] + " ("
                        + Convert.fromWei(args[1], Convert.Unit.ETHER).toPlainString() + " eth)");

                TransactionReceipt transferReceipt = Transfer.sendFunds(
                        web3j, credentials,
                        args[0],  // you can put any address here
                        amount, Convert.Unit.WEI) // 1 wei = 10^-18 Ether
                        .send();

                RestTemplate template = new RestTemplate();
                template.exchange(Constants.URL.SAVE_TRANSACTION
                        + transferReceipt.getFrom() + "/"
                        + transferReceipt.getTo() + "/"
                        + amount + "/"
                        + transferReceipt.getTransactionHash() + "/"
                        + args[2], HttpMethod.GET, null, Void.class);

                System.out.println("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                        + transferReceipt.getTransactionHash());

//                template.exchange(Constants.URL.SAVE_PAID
//                        + transferReceipt.getFrom() + "/"
//                        + transferReceipt.getTo() + "/"
//                        + args[3] + "/"
//                        + transferReceipt.getTransactionHash(), HttpMethod.GET, null, Void.class);

            } catch (Exception e) {
                return e.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingView.hideLoadingIndicator();
            if (s != null) {
                Toast.makeText(SendActivity.this, s, Toast.LENGTH_LONG).show();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_CANCELED, intent);
                finish();
            } else {
                if (!comission.equals("")) {
                    new SendComission().execute(Constants.URL.COMISSION_WALLET, comission, String.valueOf(eth_price));
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private class SendComission extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... args) {

            try {
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
                Credentials credentials =
                        WalletUtils.loadCredentials(
                                Constants.wallet.getPassword(),
                                new File(Constants.wallet.getFile()));//TODO заменить на внутренний файл-ключ



                RestTemplate template = new RestTemplate();

                //комиссия

                    BigDecimal amount_comission = Convert.toWei(args[1], Convert.Unit.ETHER);

                    TransactionReceipt transferReceipt = Transfer.sendFunds(
                            web3j, credentials,
                            Constants.URL.COMISSION_WALLET,  // you can put any address here
                            amount_comission, Convert.Unit.WEI) // 1 wei = 10^-18 Ether
                            .send();

                    template.exchange(Constants.URL.SAVE_TRANSACTION
                            + transferReceipt.getFrom() + "/"
                            + transferReceipt.getTo() + "/"
                            + amount_comission + "/"
                            + transferReceipt.getTransactionHash() + "/"
                            + args[2], HttpMethod.GET, null, Void.class);


                System.out.println("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                        + transferReceipt.getTransactionHash());

//                template.exchange(Constants.URL.SAVE_PAID
//                        + transferReceipt.getFrom() + "/"
//                        + transferReceipt.getTo() + "/"
//                        + args[3] + "/"
//                        + transferReceipt.getTransactionHash(), HttpMethod.GET, null, Void.class);

            } catch (Exception e) {
                return e.toString();
            }

            return null;
        }

    }

}
