package com.example.npl.wifi_scanner;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.npl.wifi_scanner.model.Fingerprint;
import com.example.npl.wifi_scanner.model.FingerprintHttpLab;
import com.example.npl.wifi_scanner.model.Trajectory;
import com.example.npl.wifi_scanner.model.TrajectoryLab;
import com.example.npl.wifi_scanner.model.WifiInfo;
import com.example.npl.wifi_scanner.tool.DateUtils;
import com.example.npl.wifi_scanner.tool.DeviceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

public class LocateFragment extends Fragment {

    private static final int SUCCESS = 4;
    private static final int FAIL = 890;
    private EditText request_LocateZone;
    private Button request_Locate;
    private Button request_Size;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_locate,container,false);
        request_LocateZone=(EditText)view.findViewById(R.id.request_locateZone);
        request_Locate=(Button)view.findViewById(R.id.request_locate);
        request_Size=(Button)view.findViewById(R.id.request_size);

        request_Locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //获取当前位置
//                List<WifiInfo> locateTarget=scan(getContext());
//                //形成没有位置信息的指纹点：
//                Fingerprint  locate_Fingerprint=getFingerprintByWifiInfo(locateTarget);
//                //获取当前位置(可选) 通过KNN算法比对指纹
//                String[] args={request_LocateZone.getText().toString()};
//                locate_Fingerprint=Knn
//                        (locate_Fingerprint, FingerprintLab.get(getActivity()).getFingerprints());
                Fingerprint  locate_Fingerprint=returnFingerprint(getContext(),null);
                if(locate_Fingerprint!=null){
                    Toast.makeText(getContext(),locate_Fingerprint.getLocation()+":"+locate_Fingerprint.getLocation_x()+":"+locate_Fingerprint.getLocation_y(),Toast.LENGTH_LONG).show();

//                    Trajectory trajectory=new Trajectory("631507020316",
//                            DeviceUtils.getUniqueId(getActivity()), DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"),locate_Fingerprint.getFingerprint(),
//                            locate_Fingerprint.getLocation(),locate_Fingerprint.getLocation_x(),
//                            locate_Fingerprint.getLocation_y());
//                    TrajectoryLab.get(getActivity()).addTrajectory(trajectory);
                }else{
                    Toast.makeText(getContext(),"错误:"+locate_Fingerprint,Toast.LENGTH_SHORT).show();
                }}
        });

        return view;
    }
    public static Fingerprint returnFingerprint(final Context context, final String request_LocateZone ){


        List<WifiInfo> locateTarget=scan(context);
        if(locateTarget.size()==0){
            return null;
        }
        //形成没有位置信息的指纹点：
        Fingerprint  locate_Fingerprint=getFingerprintByWifiInfo(locateTarget);

        List<Fingerprint> data= FingerprintHttpLab.getFingerprintList();

        if(data!=null){
            Fingerprint knnReturn=Knn(locate_Fingerprint, data);
            knnReturn.setFingerprint(locate_Fingerprint.getFingerprint());
            return knnReturn;
        }else{
            return null;
        }
    }




    //输入定位请求的指纹   样本库指纹
    //KNN算法比对 相似度较高指纹
    public static Fingerprint Knn(Fingerprint fingerprint,List<Fingerprint> fingerprints){


        Map<String,Integer> targetMap=StringToMap(fingerprint);
        List<Map<String,Integer>> all=new ArrayList<>();
        for(int i=0;i<fingerprints.size();i++){
            all.add(StringToMap(fingerprints.get(i)));
        }

        List<Double> distance= new ArrayList<Double>();
        for(int i=0;i<all.size();i++){
            Map<String,Integer> temp=all.get(i);
            //寻求相似度函数
            distance.add(funSimilarity(temp,targetMap));
        }


        List<Double> temp= new ArrayList<Double>();
        for(int i=0;i<distance.size();i++){
            temp.add(distance.get(i));
        }



        Collections.sort(temp);


        if(Integer.MAX_VALUE==temp.get(0)){
            //未匹配到
            return new Fingerprint();
        }

        //获取N个点
        int[]  targetIndex=new int[11];
        for(int i=0;i<targetIndex.length;i++){
            targetIndex[i]=distance.lastIndexOf(temp.get(i));
        }

        return funALL(targetIndex,fingerprints,distance);

    }
//targetIndex 存放距离最近的下标  fingerprints即数据库所有指纹 distnce 存放距离
    private static Fingerprint funALL(int[] targetIndex, List<Fingerprint> fingerprints, List<Double> distance) {
        //得到了  最小几个的index
        List<Fingerprint>  tempFingerprints=new ArrayList<>();
        List<Double> distanceFingerprints=new ArrayList<>();
        for(int i=0;i<targetIndex.length;i++){
            //样本：
            tempFingerprints.add(fingerprints.get(targetIndex[i]));
            //样本距离：
            distanceFingerprints.add(distance.get(targetIndex[i]));
            //离得近的样本 距离更低
        }
        //分成两类  能分出来
        for(int i=0;i<tempFingerprints.size();i++){
            int count=0;
            Fingerprint temp=tempFingerprints.get(i);
            for(int j=0;j<tempFingerprints.size();j++){
                if(temp.getLocation_x().equals(tempFingerprints.get(j).getLocation_x())&&
                        temp.getLocation_y().equals(tempFingerprints.get(j).getLocation_y())){
                        count++;
                }
            }
            if(count>=tempFingerprints.size()/2+1){
                return temp;
            }
            if(count==0){
                return new Fingerprint(temp.getFingerprint());
            }
        }
        return tempFingerprints.get(0);
    }


    //距离  temp  数据库样本  target请求定位指纹 Map存放 mac地址和 RSSI信号强度
    private static double funSimilarity(Map<String,Integer> temp,Map<String,Integer> target) {
        int distance=0;
        int count=0;
        for (Map.Entry<String, Integer> targetEntry : target.entrySet()) {
            for (Map.Entry<String, Integer> tempEntry : temp.entrySet()){
                if(targetEntry.getKey().equals(tempEntry.getKey())){
                    distance+=Math.pow((targetEntry.getValue()-tempEntry.getValue()),2);
                    count++;
                }
                else{
                    distance+=Math.pow(targetEntry.getValue(),2);
                }
            }
        }
        if(count==0){
            return Integer.MAX_VALUE;
        }
        return Math.sqrt(distance);
    }

    //获取此处指纹点
    public static  List<WifiInfo> scan(Context context){
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

    //通过WIFI信息 拿到没有位置信息的指纹
    public static  Fingerprint getFingerprintByWifiInfo(List<WifiInfo> infos){

        Map<String,String> result=new HashMap<>();

        for(WifiInfo info:infos){
            result.put(info.getMac(),info.getRss());
        }
        return new Fingerprint(result.toString());
    }


    public static Map<String, Integer> StringToMap(Fingerprint fingerprint) {
        Map<String, Integer> result=new HashMap<>();
        String order=fingerprint.getFingerprint();
        order=order.substring(1, order.length()-1);
        String[] strs=order.split(",");
        for (String string : strs) {
            String key=string.split("=")[0];
            Integer value=Integer.valueOf(string.split("=")[1]);
            result.put(key, value);
        }
        return result;
    }


}
