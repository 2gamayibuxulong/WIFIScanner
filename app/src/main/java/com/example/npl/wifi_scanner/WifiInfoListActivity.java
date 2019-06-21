package com.example.npl.wifi_scanner;

import android.support.v4.app.Fragment;

public class WifiInfoListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new WifiInfoListFragment();
    }
}
