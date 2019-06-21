package com.example.npl.wifi_scanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.npl.wifi_scanner.model.WifiInfo;
import com.example.npl.wifi_scanner.model.WifiInfoLab;


public class WifiInfoFragment extends Fragment {
    private static final String ARG_WIFIINFO_MAC  = "wifiInfo_MAC";

    private TextView mWifiInfoMac;
    private TextView mWifiInfoName;
    private TextView mWifiInfoRss;
    private Button mWifiInfoDateBtn;
    private CheckBox Target;

    private WifiInfo mWifiInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mac=(String)getActivity().getIntent()
                .getSerializableExtra(WifiInfoActivity.EXTRA_WIFIINFO_MAC);
        mWifiInfo= WifiInfoLab.get(getActivity()).getWifiInfo(mac);
    }

    public static WifiInfoFragment newInstance(String mac){
        Bundle args=new Bundle();
        args.putSerializable(ARG_WIFIINFO_MAC,mac);

        WifiInfoFragment fragment=new WifiInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_wifi_info,container,false);
        mWifiInfoMac=(TextView) v.findViewById(R.id.wifi_mac);
        mWifiInfoMac.setText(mWifiInfo.getMac());

        mWifiInfoName=(TextView) v.findViewById(R.id.wifi_name);
        mWifiInfoName.setText(mWifiInfo.getName());

        mWifiInfoRss=(TextView) v.findViewById(R.id.wifi_rss);
        mWifiInfoRss.setText(mWifiInfo.getRss());

        mWifiInfoDateBtn=(Button) v.findViewById(R.id.wifi_measure_date);
        mWifiInfoDateBtn.setText(mWifiInfo.getMeasureDate().toString());
        mWifiInfoDateBtn.setEnabled(false);

        Target=(CheckBox)v.findViewById(R.id.wifi_is_target);
        Target.setChecked(mWifiInfo.getTarget());
        Target.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mWifiInfo.setTarget(isChecked);
            }
        });
        return v;
    }

}
