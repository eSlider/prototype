package com.prototype.prototype.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.prototype.prototype.R;
import com.prototype.prototype.fragment.AbstractTabFragment;
import com.prototype.prototype.fragment.FirstFragment;
import com.prototype.prototype.fragment.SecondFragment;
import com.prototype.prototype.fragment.ThirdFragment;

import java.util.HashMap;
import java.util.Map;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;
    public  TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initTabs(context);

    }
    private void initTabs(Context context) {
        //добавляем закладки
        tabs = new HashMap<>();
        tabs.put(0, FirstFragment.getInstance(context));
        tabs.put(1, SecondFragment.getInstance(context));
        tabs.put(2, ThirdFragment.getInstance(context));
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
