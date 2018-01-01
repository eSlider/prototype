package com.cryptopay.prototype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptopay.prototype.R;
import com.cryptopay.prototype.adapter.AdvertListAdapter;
import com.cryptopay.prototype.domain.dto.AdvertDTO;

public class AdvertFragment extends AbstractTabFragment {

    public static final int FRAGMENT = R.layout.fragment_adverts;
    private AdvertListAdapter advertListAdapter;

    public AdvertFragment() {
    }

    public AdvertListAdapter getAdvertListAdapter() {
        return advertListAdapter;
    }

    public void refreshList(AdvertDTO advertDTO){
//        this.advertListAdapter.setData(advertDTO);
        this.advertListAdapter.notifyDataSetChanged();
    }

    public static AdvertFragment getInstance(Context context){
        Bundle args = new Bundle();
        AdvertFragment fragment = new AdvertFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
//        fragment.setAdvertDTO(data);
        fragment.setTitle(context.getString(R.string.advert_tab));
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
        rv.setAdapter(this.advertListAdapter);
        return view;
    }

}
