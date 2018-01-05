package com.cryptopay.prototype.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.db.DBHelper;
import com.cryptopay.prototype.db.sqllitedomain.Template;
import com.cryptopay.prototype.domain.Wallet;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.TimeUnit;

public class RecoveryActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_recovery;
    private Toolbar toolbar;
    private Button btnChooseFile, btnRecovery;
    private EditText etPassword;
    private ImageView ivShowPass;
    private File targetFile = new File("");
    private final static int REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    private final static int REQUEST_READ_EXTERNAL_STORAGE = 4;
    private SharedPreferences sPref;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        ivShowPass = (ImageView) findViewById(R.id.iv_show_pass);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRecovery = (Button) findViewById(R.id.btn_recovery);
        btnChooseFile = (Button) findViewById(R.id.btn_choose_keyfile);
        sPref = getPreferences(MODE_PRIVATE);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(RecoveryActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
//            startActivity();
        }
//        ivShowPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
//            }
//        });

        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().isEmpty()){
                    Toast.makeText(RecoveryActivity.this, "Input your password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!targetFile.exists()){
                    Toast.makeText(RecoveryActivity.this, "Choose your keyfile", Toast.LENGTH_LONG).show();
                    return;
                }
                mLoadingView.showLoadingIndicator();
                new RecoveryWallet().execute(etPassword.getText().toString(), targetFile);
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.recovery_toolbar);
        toolbar.setTitle(R.string.recovery_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                startActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity();
    }

    private void startActivity(){
        Intent intent = new Intent(RecoveryActivity.this, StartActivity.class);
        startActivity(intent);
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Key File"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path;
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    try {
                        readFile(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void readFile(Uri uri) throws IOException {

        InputStream initialStream = getContentResolver().openInputStream(uri);

        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        targetFile = new File(getApplicationContext().getFilesDir().getAbsolutePath(), "key");
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.flush();
        outStream.close();
        initialStream.close();




    }

    public class RecoveryWallet extends AsyncTask<Object, Void, Wallet> {

        @Override
        protected Wallet doInBackground(Object... objects) {
            SharedPreferences sPref;
            String password = (String) objects[0];
            File targetFile = (File) objects[1];
            Wallet wallet = new Wallet();
            String TAG = "web3";
            Log.d(TAG, "testWeb3: ");
            Web3j web3j = Web3jFactory.build(new HttpService(
                    Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
            try {
                Log.d(TAG, "Connected to Ethereum client version: "
                        + web3j.web3ClientVersion().send().getWeb3ClientVersion());
                Log.d(TAG, "connect");

                Log.d(TAG, targetFile.getAbsolutePath());
//        // We then need to load our Ethereum wallet file
//        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
                Credentials credentials =
                        org.web3j.crypto.WalletUtils.loadCredentials(
                                password,
                                targetFile);
                wallet.setAddress(credentials.getAddress());
                wallet.setFile(targetFile.getAbsoluteFile().toString());//TODO переписать на правильный путь
                wallet.setPassword(password);
                wallet.setPublicKey(credentials.getEcKeyPair().getPublicKey());
                wallet.setPrivateKey(credentials.getEcKeyPair().getPrivateKey());
                return  wallet;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Wallet wallet) {
//            super.onPostExecute(wallet);
            mLoadingView.hideLoadingIndicator();
            if(wallet!=null) {
                Constants.wallet = wallet;


                startActivity();
            } else {
                Toast.makeText(RecoveryActivity.this, "Error validate wallet", Toast.LENGTH_LONG).show();
            }
        }
    }
}
