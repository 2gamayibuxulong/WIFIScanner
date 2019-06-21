package com.example.npl.wifi_scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class WifiInfoActivity extends SingleFragmentActivity {

    public static final String EXTRA_WIFIINFO_MAC=
            "com.example.npl.wifi_scanner.mac_Address";
    @Override
    protected Fragment createFragment() {
        String mac=(String)getIntent().getSerializableExtra(EXTRA_WIFIINFO_MAC);
        return WifiInfoFragment.newInstance(mac);
    }

    public static Intent newIntent(Context context,String mac){
        Intent intent=new Intent(context,WifiInfoActivity.class);
        intent.putExtra(EXTRA_WIFIINFO_MAC,mac);
        return intent;
    }
}
