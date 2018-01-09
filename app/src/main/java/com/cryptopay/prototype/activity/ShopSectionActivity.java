package com.cryptopay.prototype.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.R;
import com.cryptopay.prototype.activity.general.LoadingDialog;
import com.cryptopay.prototype.activity.general.LoadingView;
import com.cryptopay.prototype.adapter.ShopListAdapter;
import com.cryptopay.prototype.adapter.ShopSectionListAdapter;
import com.cryptopay.prototype.domain.Advert;
import com.cryptopay.prototype.domain.Item;
import com.cryptopay.prototype.domain.Section;
import com.cryptopay.prototype.domain.dto.ItemDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShopSectionActivity extends AppCompatActivity implements ShopSectionListAdapter.OnItemClick {
    public static final int LAYOUT = R.layout.activity_shopsection;

    public double totalPrice = 0.0d;
    public static int cartCount = 0;
    public static double cartSumma = 0.0d;

    private Toolbar toolbar;
    private LinearLayout llPushOrder;
    private RecyclerView recyclerView;
    public static List<Item> items = new ArrayList<>();
    private ShopSectionListAdapter mAdapter;
    private LoadingView mLoadingView;
    public static Advert advert;


    private ImageView ivPic;
    private TextView tvTitle;

    private TextView tvCount, tvSumma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
//        Intent intent = getIntent();
        initToolbar();

        ivPic = (ImageView) findViewById(R.id.iv_pic);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvSumma = (TextView) findViewById(R.id.tv_summa);
        llPushOrder = (LinearLayout) findViewById(R.id.ll_push_order);
        byte[] pic = advert.getPic();
        if (pic != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            ivPic.setImageBitmap(bMap);
        }
        tvTitle.setText(advert.getTitle());


        mAdapter = new ShopSectionListAdapter(new ArrayList<Section>(), this);
        recyclerView = (RecyclerView) findViewById(R.id.section_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();
        new ShopTask().execute(advert);

        ShopSectionActivity.cartSumma = 0.0d;
        ShopSectionActivity.cartCount = 0;
        updateCart();

        llPushOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopSectionActivity.cartCount > 0) {
                    Intent intent = new Intent(ShopSectionActivity.this, OrderActivity.class);
                    startActivityForResult(intent, BUY_FROM_CART);
                }
            }
        });
    }

    public static final int BUY_FROM_CART = 200;

    public void updateCart() {
        tvCount.setText("Cart: " + String.valueOf(ShopSectionActivity.cartCount));
        tvSumma.setText("Total: " + String.valueOf(ShopSectionActivity.cartSumma) + " euro");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if(data.getBooleanExtra("paid",false)){
                mLoadingView.showLoadingIndicator();
                new ShopTask().execute(advert);
            } else {
                updateCart();
            }
        }

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.shop_toolbar);
//        toolbar.setTitle(advert.getTitle());
        toolbar.setTitle(advert.getTitle());
//
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
        toolbar.inflateMenu(R.menu.shop_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info:
                        if (advert != null) {
                            Intent intent = new Intent(ShopSectionActivity.this, InfoActivity.class);
                            Constants.advert = advert;
                            intent.putExtra("from", SWITCH_FROM_SHOP);
                            startActivityForResult(intent, SWITCH_FROM_SHOP);
                        }
                        break;
                    case R.id.location:
                        if (advert != null) {
                            Intent intent = new Intent(ShopSectionActivity.this, MapsActivity.class);
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

    public static final int SWITCH_FROM_SHOP = 301;
    public static final String TITLE_FOR_SHOP = "Title";

    @Override
    public void onItemClick(@NonNull Section section) {
        Intent intent = new Intent(ShopSectionActivity.this, ShopActivity.class);
        ShopActivity.items = new ArrayList<Item>();
        for (Item item : this.items) {
            if (item.getSection().getId() == section.getId()) {
                ShopActivity.items.add(item);
            }
        }

        intent.putExtra(TITLE_FOR_SHOP, section.getTitle());
        startActivityForResult(intent, SWITCH_FROM_SHOP);
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
            TreeSet<Section> set = new TreeSet<>(new Comparator<Section>() {
                @Override
                public int compare(Section section, Section t1) {
                    return section.getTitle().compareTo(t1.getTitle());
                }
            });
            for (Item item : items) {
                set.add(item.getSection());
            }

            mAdapter.changeDataSet(new ArrayList<Section>(set));
        }
    }

    private void showError() {
        mLoadingView.hideLoadingIndicator();
        Snackbar snackbar = Snackbar.make(recyclerView, "Error loading items", Snackbar.LENGTH_LONG)
                .setAction("Retry", v -> new ShopSectionActivity.ShopTask().execute());
        snackbar.setDuration(4000);
        snackbar.show();
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

    }
}
