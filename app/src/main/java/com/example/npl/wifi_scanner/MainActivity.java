package com.example.npl.wifi_scanner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.example.npl.wifi_scanner.tool.UserPermissionUtil;


public class MainActivity extends SingleFragmentActivity {
    private UserPermissionUtil util;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent LocateService=new Intent(this,com.example.npl.wifi_scanner.LocateService.class);
        startService(LocateService);
        //'首先判断当前的权限问题
        util = new UserPermissionUtil(this);
        util.getVersion();

    }

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    public static Intent newIntent(Context context,String Target){
        Intent intent;
        switch (Target){
            case "look_around":
                intent=new Intent(context,WifiInfoListActivity.class);
                break;
            case "formingFingerprint":
                intent=new Intent(context,FingerprintActivity.class);
                break;
            case "begin_locate":
                intent=new Intent(context,LocateActivity.class);
                break;
            case "show_Trajectory":
                intent=new Intent(context,TrajectoryActivity.class);
                break;
            default:
                intent=new Intent();
        }
        return intent;
    }

}
