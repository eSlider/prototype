package com.cryptopay.prototype.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.adapter.ShopListAdapter;
import com.cryptopay.prototype.domain.Advert;
import com.cryptopay.prototype.domain.Item;
import com.cryptopay.prototype.domain.dto.ItemDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements ShopListAdapter.OnItemClick {
    public static final int LAYOUT = R.layout.activity_shop;

    public float totalPrice = 0.0f;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Item> items;
    private ShopListAdapter mAdapter;
    private LoadingView mLoadingView;
    public static Advert advert;

    private Button btnBuy;
    private TextView tvShopcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
//        Intent intent = getIntent();
        initToolbar();

        btnBuy = (Button) findViewById(R.id.btn_buy);
        tvShopcart = (TextView) findViewById(R.id.tv_shopcart);

        mAdapter = new ShopListAdapter(new ArrayList<Item>(), this);
        recyclerView = (RecyclerView) findViewById(R.id.shop_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();
        new ShopTask().execute(advert);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPrice <= 0) {
                    Toast.makeText(ShopActivity.this, R.string.danger_make_you_order, Toast.LENGTH_LONG).show();
                    return;
                }
                if (Constants.balance < totalPrice) {
                    Toast.makeText(ShopActivity.this, R.string.danger_not_enough_money, Toast.LENGTH_LONG).show();
                    totalPrice = 0;
                    tvShopcart.setText("make your order");
                    return;
                }
                if (advert == null || advert.getWallet() == null || advert.getWallet().isEmpty()) {
                    Toast.makeText(ShopActivity.this, R.string.danger_address_recepient_isempty, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(ShopActivity.this, SendActivity.class);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("address", advert.getWallet());
                startActivityForResult(intent, BUY_FROM_CART);
            }
        });
    }

    public static final int BUY_FROM_CART = 200;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BUY_FROM_CART) {
            if (resultCode == RESULT_OK) {
                totalPrice = 0;
                tvShopcart.setText("order has been paid");
            } else {

            }
        }
    }

    public static final int SWITCH_FROM_SHOP = 301;

    private void initToolbar() {
//        tvShopCart = (TextView) findViewById(R.id.tv_shop_cart);
        toolbar = (Toolbar) findViewById(R.id.shop_toolbar);
        toolbar.setTitle("Cart");
//        tvShopCart.setText(R.string.cart_title+Constants.CART_COUNT);

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
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                List<Item> data = Constants.itemDTO.getData();
//                long price = data.get(item.getItemId()).getPrice();
//                tvShopCart.setText(getResources().getString(R.string.cart_title)+" "+(totalPrice+=price));
////                tvShopCart.setText(R.string.cart_title+(++Constants.CART_COUNT));
//                return false;
//            }
//        });
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.location:
                        if (advert != null) {
                            Intent intent = new Intent(ShopActivity.this, MapsActivity.class);
                            ArrayList<Advert> adverts = new ArrayList<>();
                            adverts.add(advert);
                            Constants.advertDTO.setData(adverts);
                            intent.putExtra("from", SWITCH_FROM_SHOP);
                            startActivityForResult(intent, SWITCH_FROM_SHOP);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(@NonNull Item item) {
        float price = item.getPrice();
        totalPrice += price;
        tvShopcart.setText("Total :" + String.format("%.3f", totalPrice).replace(",", ".") + " cc€");
    }

    private class ShopTask extends AsyncTask<Advert, Void, ItemDTO> {

        @Override
        protected ItemDTO doInBackground(Advert... args) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                return template.getForObject(Constants.URL.FIND_ALL_ITEM + "/" + args[0].getId(), ItemDTO.class);
            } catch (RuntimeException exception) {
                System.out.println(exception.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ItemDTO itemDTO) {
            if (itemDTO == null || itemDTO.getData() == null) {
                showError();
                return;
            }
            mLoadingView.hideLoadingIndicator();
            items = itemDTO.getData();
            mAdapter.changeDataSet(items);
        }
    }

    private void showError() {
        mLoadingView.hideLoadingIndicator();
        Snackbar snackbar = Snackbar.make(recyclerView, "Error loading items", Snackbar.LENGTH_LONG)
                .setAction("Retry", v -> new ShopActivity.ShopTask().execute());
        snackbar.setDuration(4000);
        snackbar.show();
    }
}
