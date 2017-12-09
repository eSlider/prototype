package com.prototype.prototype.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.prototype.prototype.Constants;
import com.prototype.prototype.R;
import com.prototype.prototype.adapter.TabsFragmentAdapter;
import com.prototype.prototype.domain.Advert;
import com.prototype.prototype.domain.Wallet;
import com.prototype.prototype.domain.dto.AdvertDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_main;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabsFragmentAdapter adapter;
    private FloatingActionButton fab;
    public SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPurse();
            }
        });
        initWallet();

    }

    private void initWallet() {
        sPref = getPreferences(MODE_PRIVATE);
        Constants.wallet = new Wallet();
        if (!sPref.getString(Constants.wallet_address, "").equals("")) {
            Constants.wallet.setAddress(sPref.getString(Constants.wallet_address, ""));
            Constants.wallet.setPassword(sPref.getString(Constants.wallet_password, ""));
            Constants.wallet.setFile(sPref.getString(Constants.wallet_file, ""));
            Constants.wallet.setPublicKey(new BigInteger(sPref.getString(Constants.wallet_publicKey, "0")));
            Constants.wallet.setPrivateKey(new BigInteger(sPref.getString(Constants.wallet_privateKey, "0")));
            new Web3Utils.GetBalanceWallet().execute();
        } else {
            new Web3Utils.GenerateNewWallet().execute(getApplicationContext().getFilesDir().getAbsolutePath(), "123123123", getPreferences(MODE_PRIVATE));
        }


    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////                Toast.makeText(MainActivity.this, "scroll pos "+position+" offset "+ positionOffset+ " offsetPixel "+ positionOffsetPixels, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                switch (position){
////                    case 0:
////                        adapter.getAdvertFragment().getAdvertListAdapter().notifyDataSetChanged();
////
////                    case 1:
////                        break;
////                    case 2:
////                        break;
////                    default:
////                        break;
////                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
////                Toast.makeText(MainActivity.this,"state " +state, Toast.LENGTH_LONG).show();
//            }
//        });
        new AdvertTask().execute(); // делаем асинхрон запрос к рест серверу со списком всех объявлений

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_nav_open, R.string.view_nav_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.main:
                        showNotificationTab();
                    case R.id.settings:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_ONE);
    }

    public void switchSend() {
        Intent intent = new Intent(MainActivity.this, SendActivity.class);
        startActivity(intent);
    }

    public void switchPurse() {
        Intent intent = new Intent(MainActivity.this, PurseActivity.class);
        startActivity(intent);
    }

    public void switchContent(Advert advert) {
        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
        intent.putExtra("id", advert.getId());
        intent.putExtra("title", advert.getTitle());
        startActivity(intent);
    }

    private class AdvertTask extends AsyncTask<Void, Void, AdvertDTO> {

        @Override
        protected AdvertDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return template.getForObject(Constants.URL.FIND_ALL_ADVERT, AdvertDTO.class);
        }

        @Override
        protected void onPostExecute(AdvertDTO advertDTO) {
            adapter.setData(advertDTO);
        }
    }

//    public void testWeb3() throws Exception {
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            //your codes here
//
//
//            String TAG = "web3";
//            Log.d(TAG, "testWeb3: ");
//            // We start by creating a new web3j instance to connect to remote nodes on the network.
//            Web3j web3j = Web3jFactory.build(new HttpService(
//                    "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
//            Log.d(TAG, "Connected to Ethereum client version: "
//                    + web3j.web3ClientVersion().send().getWeb3ClientVersion());
//            Log.d(TAG, "connect");
//            File key_1 = new File(getApplicationContext().getFilesDir().getAbsolutePath());
//
//            String s = WalletUtils.generateLightNewWalletFile("123123123"
//                    , key_1);
//            Log.d(TAG, s);
//            String filePath = getApplicationContext().getFilesDir() + "/" + s;
////
//            File fileKey = new File(filePath);
//            Log.d(TAG,fileKey.getAbsolutePath());
////        // We then need to load our Ethereum wallet file
////        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
//        Credentials credentials =
//                WalletUtils.loadCredentials(
//                        "123123123",
//                        fileKey);
//        Log.d(TAG,"Credentials loaded");
//            Log.d(TAG,credentials.getAddress());
//            Log.d(TAG,"----------------");
//            Log.d(TAG, credentials.getEcKeyPair().getPublicKey().toString());
//            Log.d(TAG, credentials.getEcKeyPair().getPrivateKey().toString());
//            Log.d(TAG, credentials.getEcKeyPair().toString());
////        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
//////        log.info("Sending 1 Wei ("
//////                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
////        Log.d(TAG,"Sending 1 Eth ("
////                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
////        TransactionReceipt transferReceipt = Transfer.sendFunds(
////                web3j, credentials,
////                "0x3596ddf5181c9F6Aa1bcE87D967Bf227DDE70ddf",  // you can put any address here
////                BigDecimal.ONE, Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
////                .send();
////        Log.d(TAG,"Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
////                + transferReceipt.getTransactionHash());
//        }
//
//    }


}
