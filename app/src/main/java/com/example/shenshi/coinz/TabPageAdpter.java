package com.example.shenshi.coinz;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPageAdpter extends FragmentStatePagerAdapter {

    String[] tabArray = new String[]{"SHIL", "DOLR", "QUID", "PENY"};
    int tabnumber = 4;

    public TabPageAdpter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                WalletShilFragment walletShilFragment = new WalletShilFragment();
                return walletShilFragment;
            case 1:
                WalletDollarFragment walletDolrFragment = new WalletDollarFragment();
                return walletDolrFragment;
            case 2:
                WalletQuidFragment walletQuidFragment = new WalletQuidFragment();
                return walletQuidFragment;
            case 3:
                WalletPenyFragment walletPenyFragment = new WalletPenyFragment();
                return walletPenyFragment;

        }

        return null;
    }

    @Override
    public int getCount() {
        return tabnumber;
    }
}
