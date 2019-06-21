package com.example.npl.wifi_scanner;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FingerprintActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FingerprintFragment();
    }
}
