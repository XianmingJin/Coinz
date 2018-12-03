package com.example.shenshi.coinz;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WalletFragment extends Fragment  {

    private String tag = "WalletFragment";
    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInfo) {
        myView = inflater.inflate(R.layout.fragment_wallet, container, false);

        TabLayout tabLayout = myView.findViewById(R.id.tabs1);
        ViewPager pager = myView.findViewById(R.id.viewPager);
        TabPageAdpter tabPageAdpter = new TabPageAdpter(getActivity().getSupportFragmentManager());
        pager.setAdapter(tabPageAdpter);
        tabLayout.setupWithViewPager(pager);

        return myView;
    }


}
