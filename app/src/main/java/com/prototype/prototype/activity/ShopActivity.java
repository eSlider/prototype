package com.prototype.prototype.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.prototype.prototype.Constants;
import com.prototype.prototype.R;
import com.prototype.prototype.adapter.AdvertListAdapter;
import com.prototype.prototype.adapter.ShopListAdapter;
import com.prototype.prototype.adapter.TabsFragmentAdapter;
import com.prototype.prototype.domain.Item;

import java.util.List;

public class ShopActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_shop;
    public TextView tvId;
    public TextView tvTitle;
    public TextView tvShopCart;
    public long totalPrice = 0;
    private Toolbar toolbar;
    private ShopListAdapter shopListAdapter  = new ShopListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Intent intent = getIntent();
        shopListAdapter.setData(Constants.itemDTO);
        initToolbar();
        RecyclerView rv = (RecyclerView) findViewById(R.id.shop_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(shopListAdapter);


//        tvId.setText(String.valueOf(intent.getIntExtra("id", 0)));
//        tvTitle.setText(intent.getStringExtra("title"));
    }

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
//        toolbar.inflateMenu(R.menu.shop_menu);
    }


}
