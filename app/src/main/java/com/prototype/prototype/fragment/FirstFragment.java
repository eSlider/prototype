package com.prototype.prototype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototype.prototype.R;
import com.prototype.prototype.adapter.AdvertListAdapter;
import com.prototype.prototype.dto.AdvertDTO;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends AbstractTabFragment {

    public static final int FRAGMENT = R.layout.fragment_adverts;


    public FirstFragment() {
    }

    public static FirstFragment getInstance(Context context){
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_1));
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(FRAGMENT, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new AdvertListAdapter(createMockData()));
        return view;
    }

    private List<AdvertDTO> createMockData() {
        ArrayList<AdvertDTO> list = new ArrayList<AdvertDTO>();
        list.add(new AdvertDTO("test 1"));
        list.add(new AdvertDTO("test test 2"));
        list.add(new AdvertDTO("test 3"));
        list.add(new AdvertDTO("test 1"));
        list.add(new AdvertDTO("test test 2"));
        list.add(new AdvertDTO("test 3"));
        list.add(new AdvertDTO("test 1"));
        list.add(new AdvertDTO("test test 2"));
        list.add(new AdvertDTO("test 3"));
        return list;
    }
}
