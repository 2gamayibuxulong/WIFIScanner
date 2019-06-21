package com.example.npl.wifi_scanner.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;



public class WifiInfoLab {
    private static WifiInfoLab wifiInfoLab;
    private static List<WifiInfo> mWifiInfos;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private WifiInfoLab(Context context) {
        mWifiInfos=new ArrayList<>();
//        for(int i=0;i<100;i++){
//            WifiInfo info=new WifiInfo();
//            info.setMac(String.valueOf(i));
//            info.setRss(String.valueOf(i));
//            mWifiInfos.add(info);
//        }
    }
    public List<WifiInfo> getWifiInfos(){
        return mWifiInfos;
    }

    public  synchronized void  setWifiInfos(List<WifiInfo> info){
        if(mWifiInfos.size()==0){
            mWifiInfos=info;
        }else{
            //取并集mWifiInfos
            mWifiInfos.removeAll(info);
            mWifiInfos.addAll(info);
            for(int i=0;i<mWifiInfos.size();i++){
                for(int j=i+1;j<mWifiInfos.size();j++){
                    if(mWifiInfos.get(i).getMac().equals(mWifiInfos.get(j).getMac())){
                        if(mWifiInfos.get(i).getTarget()){
                            mWifiInfos.remove(mWifiInfos.get(j));
                        }
                        else {
                            mWifiInfos.remove(mWifiInfos.get(i));
                        }
                    }
                }
            }
        }

    }

    public WifiInfo getWifiInfo(String mac){
        for (WifiInfo info:mWifiInfos){
            if(info.getMac().equals(mac)){
                return info;
            }
        }
        return null;
    }

    public static synchronized WifiInfoLab get(Context context){
        if(wifiInfoLab ==null){
            wifiInfoLab =new WifiInfoLab(context);
        }
        return wifiInfoLab;
    }

    public static synchronized void set(List<WifiInfo> infos){
        mWifiInfos=infos;
    }


}
