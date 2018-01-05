package com.cryptopay.prototype.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.db.DBHelper;
import com.cryptopay.prototype.db.sqllitedomain.Template;

import java.util.concurrent.TimeUnit;

public class SendActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_send;
    private Toolbar toolbar;
    private EditText etAddressRecepient, etAmount, etTemplate;
    private CheckBox cbSaveTemplate;
    private TextView tvNameTemplate, tvBalance;
    private Button btnCopyAddressRecepient;
    private FloatingActionButton fabSend;
    public static final int CHOOSE_ADDRESS_FROM_QR = 100;
    public static final int CHOOSE_ADDRESS_FROM_TEMPLATES = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Intent intent = getIntent();
        double totalPrice = intent.getDoubleExtra("totalPrice", .0d);
        String address = intent.getStringExtra("address");

        initToolbar();
        etAddressRecepient = (EditText) findViewById(R.id.et_address_recepient);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etTemplate = (EditText) findViewById(R.id.et_template);
        cbSaveTemplate = (CheckBox) findViewById(R.id.cb_save_template);
        tvNameTemplate = (TextView) findViewById(R.id.tv_name_template);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
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
            etAmount.setText(String.format("%.18f", totalPrice).replace(",", "."));
            etAddressRecepient.setText(address.trim());
            fabSend.requestFocus();
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
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                if (etAddressRecepient.equals("")) {
                    Toast.makeText(SendActivity.this, R.string.danger_address_recepient_isempty, Toast.LENGTH_LONG).show();

                } else if (Constants.balance < totalPrice) {
                    Toast.makeText(SendActivity.this, R.string.danger_not_enough_money, Toast.LENGTH_LONG).show();

                } else {
//                    BigDecimal amount = Convert.toWei(etAmount.getText(), Convert.Unit.ETHER);
                    new Web3Utils.Send().execute(etAddressRecepient.getText().toString(), etAmount.getText().toString());
                    //sabe template
                    if (cbSaveTemplate.isChecked() && !etTemplate.getText().equals("")) {
                        new DBHelper(SendActivity.this).saveTemplate(new Template(etTemplate.getText().toString(), etAddressRecepient.getText().toString()));
                    }
                    setResult(RESULT_OK, intent);

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    finish();
                }
            }
        });
        etAmount.requestFocus();
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
