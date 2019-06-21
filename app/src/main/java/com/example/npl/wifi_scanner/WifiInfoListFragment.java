package com.example.npl.wifi_scanner;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npl.wifi_scanner.model.WifiInfo;
import com.example.npl.wifi_scanner.model.WifiInfoLab;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.WIFI_SERVICE;


public class WifiInfoListFragment extends Fragment {
    private RecyclerView mWifiInfoRecyclerView;
    private WifiInfoAdapter mAdapter;

    public List<WifiInfo> scan(Context context){
        WifiManager wm = (WifiManager)context.getSystemService(WIFI_SERVICE);
        List<WifiInfo> mWifiInfos=new ArrayList<>();
        wm.startScan();
        List<ScanResult> scanList = wm.getScanResults();
        for(ScanResult scanResult : scanList){
            WifiInfo info=new WifiInfo();
            info.setMac(scanResult.BSSID);
            info.setName(scanResult.SSID);
            info.setRss(String.valueOf(scanResult.level));
            mWifiInfos.add(info);
        }
        return mWifiInfos;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_wifi_refrash,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                updateUI();
                Toast.makeText(getActivity(),"附近WiFi信号已更新",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_wifiinfo_list,container,false);
        mWifiInfoRecyclerView=(RecyclerView)view.findViewById(R.id.wifiInfo_recycler_view);
        mWifiInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        List<WifiInfo> infos=scan(getContext());
        if(mAdapter==null){
            mAdapter=new WifiInfoAdapter(infos);
            mWifiInfoRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setInfos(infos);
            mAdapter.notifyDataSetChanged();
        }
        WifiInfoLab.get(getActivity()).setWifiInfos(infos);
    }


    private class WifiInfoHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private WifiInfo mWifiInfo;
        private TextView mMacTextView;
        private TextView mRssTextView;

        public WifiInfoHolder(LayoutInflater inflater,ViewGroup parent) {
           super(inflater.inflate(R.layout.list_item_wifiinfo,parent,false));
           itemView.setOnClickListener(this);
           mMacTextView=(TextView) itemView.findViewById(R.id.wifi_mac);
           mRssTextView=(TextView)itemView.findViewById(R.id.wifi_rss);
        }

        public void bind(WifiInfo wifiInfo){
            mWifiInfo=wifiInfo;
            mMacTextView.setText(mWifiInfo.getMac());
            mRssTextView.setText(mWifiInfo.getRss());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),mWifiInfo.getMac()+" clicked!",Toast.LENGTH_SHORT)
                    .show();
            Intent intent=WifiInfoActivity.newIntent(getActivity(),mWifiInfo.getMac());
            startActivity(intent);
        }
    }

    private class WifiInfoAdapter extends  RecyclerView.Adapter<WifiInfoHolder>{
        private List<WifiInfo> mWifiInfos;

        public void setInfos(List<WifiInfo> infos){
            mWifiInfos=infos;
        }

        public WifiInfoAdapter(List<WifiInfo> WifiInfos){
            mWifiInfos=WifiInfos;
        }

        @NonNull
        @Override
        public WifiInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
           LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
           return new WifiInfoHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull WifiInfoHolder wifiInfoHolder, int i) {
            WifiInfo info=mWifiInfos.get(i);
            wifiInfoHolder.bind(info);
        }
        @Override
        public int getItemCount() {
            return mWifiInfos.size();
        }
    }




}
