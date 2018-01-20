package com.cryptopay.prototype.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.adapter.OrderAdapter;
import com.cryptopay.prototype.adapter.OrderListAdapter;
import com.cryptopay.prototype.domain.Item;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.OnItemClick {
    public static final int LAYOUT = R.layout.activity_order;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Item> items;
    private OrderListAdapter mAdapter;
    private LoadingView mLoadingView;
    private double eth_price = 0.0d;
    private Button btnBuy;
    private TextView tvShopcart, tvShopcartEth, tvEth;
    private ListView listOrders;
    private OrderAdapter orderAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        intent = getIntent();
        initToolbar();

        btnBuy = (Button) findViewById(R.id.btn_buy);
        tvEth = (TextView) findViewById(R.id.tv_eth);
        tvShopcart = (TextView) findViewById(R.id.tv_shopcart);
        tvShopcartEth = (TextView) findViewById(R.id.tv_shopcart_eth);
        ArrayList<Item> list = new ArrayList<>();
        for (Item item : ShopSectionActivity.items) {
            if (item.getAmount() > 0) {
                list.add(item);
            }
        }

        Collections.sort(list, (t1, t2) -> t1.getTitle().compareTo(t2.getTitle()));
        Collections.sort(list, (t1, t2) -> t1.getSection().getTitle().compareTo(t2.getSection().getTitle()));

        orderAdapter = new OrderAdapter(this, list);
        listOrders = (ListView) findViewById(R.id.listOrders);
        listOrders.setAdapter(orderAdapter);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
//        mLoadingView.showLoadingIndicator();
//
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eth_price <= 0d) {
                    Toast.makeText(OrderActivity.this, R.string.danger_error_rate_eth, Toast.LENGTH_LONG).show();
                    return;
                }
                if (ShopSectionActivity.cartSumma <= 0d) {
                    Toast.makeText(OrderActivity.this, R.string.danger_make_you_order, Toast.LENGTH_LONG).show();
                    return;
                }
                if (Constants.balance < (ShopSectionActivity.cartSumma / eth_price)) {
                    Toast.makeText(OrderActivity.this, R.string.danger_not_enough_money, Toast.LENGTH_LONG).show();
                    return;
                }
                if (ShopSectionActivity.advert == null || ShopSectionActivity.advert.getWallet() == null || ShopSectionActivity.advert.getWallet().isEmpty()) {
                    Toast.makeText(OrderActivity.this, R.string.danger_address_recepient_isempty, Toast.LENGTH_LONG).show();
                    return;
                }
                if (ShopSectionActivity.advert.getWallet().length() != 42 || !ShopSectionActivity.advert.getWallet().substring(0, 2).equals("0x")) {
                    Toast.makeText(OrderActivity.this, R.string.danger_wrong_address_ethereum_wallet, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(OrderActivity.this, SendActivity.class);
                intent.putExtra("totalPrice", (ShopSectionActivity.cartSumma / eth_price));
                intent.putExtra("address", ShopSectionActivity.advert.getWallet());
                intent.putExtra("eth_price", eth_price);
                startActivityForResult(intent, BUY_FROM_CART);
            }
        });

        new GetEthPrice().execute();


    }


    public static final int BUY_FROM_CART = 200;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BUY_FROM_CART) {
            if (resultCode == RESULT_OK) {
                ShopSectionActivity.cartSumma = 0d;
                ShopSectionActivity.cartCount = 0;
                tvShopcart.setText("order has been paid");
                tvShopcartEth.setText("TOTAL 0.0 eth");
                setResult(RESULT_OK, intent);
                finish();
            } else {

            }
        }
    }


    private void initToolbar() {
//        tvShopCart = (TextView) findViewById(R.id.tv_shop_cart);
        toolbar = (Toolbar) findViewById(R.id.shop_toolbar);
        toolbar.setTitle(ShopSectionActivity.advert.getTitle());
//        tvShopCart.setText(R.string.cart_title+Constants.CART_COUNT);

//        toolbar.setBackgroundColor(getResources().getColor(R.color.whiteColor));
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));


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

    }

    @Override
    public void onItemClick(@NonNull Item item) {
//        double price = item.getPriceCurrency();
//        totalPrice += price;
//        tvShopcart.setText("Total :" + String.format("%.2f", totalPrice).replace(",", ".") + " euro");
//        if (eth_price > 0.0d) {
//            tvShopcartEth.setText("TOTAL: " + String.format("%.10f", (totalPrice / eth_price)).replace(",", ".") + " eth");
//        } else {
//            tvShopcartEth.setText("TOTAL: 0.0 eth");
//        }
    }

    @Override
    public void update() {

        updateCart();

    }

    public void updateCart() {
        tvShopcart.setText("Total " + String.format("%.2f", ShopSectionActivity.cartSumma).replace(",", ".") + " euro");
        if (eth_price > 0.0d) {
            tvShopcartEth.setText("TOTAL " + String.format("%.10f", (ShopSectionActivity.cartSumma / eth_price)).replace(",", ".") + " eth");
        } else {
            tvShopcartEth.setText("TOTAL 0.0 eth");
        }
    }

    private class GetEthPrice extends AsyncTask<Void, Void, Double> {


        @Override
        protected Double doInBackground(Void... voids) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                return template.getForObject(Constants.URL.GET_ETH_PRICE, Double.class);
            } catch (RuntimeException exception) {
                System.out.println(exception.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double price) {
            eth_price = price.doubleValue();
            tvEth.setText("Eth rate " + String.format("%.2f", eth_price));
            updateCart();
        }
    }
}
