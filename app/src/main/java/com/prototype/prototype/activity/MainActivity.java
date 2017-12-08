package com.prototype.prototype.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.prototype.prototype.Constants;
import com.prototype.prototype.R;
import com.prototype.prototype.adapter.TabsFragmentAdapter;
import com.prototype.prototype.domain.Advert;
import com.prototype.prototype.domain.dto.AdvertDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_main;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabsFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();
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
                switch (item.getItemId()){
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

    private void showNotificationTab(){
        viewPager.setCurrentItem(Constants.TAB_ONE);
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

}
