package com.example.npl.wifi_scanner;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.npl.wifi_scanner.model.Fingerprint;
import com.example.npl.wifi_scanner.model.WifiInfo;
import com.example.npl.wifi_scanner.model.WifiInfoLab;
import com.example.npl.wifi_scanner.tool.Constant;
import com.example.npl.wifi_scanner.tool.HttpUtils;
import com.example.npl.wifi_scanner.tool.aesrsa.util.EncryptionService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import static android.content.Context.WIFI_SERVICE;

public class FingerprintFragment extends Fragment {

    private static final int SUCCESS = 9;
    private static final int FAIL = 768;

    private List<WifiInfo> targetAps=new ArrayList<>();
    private EditText location;
    private EditText location_X;
    private EditText location_Y;
    private TextView targetAP;
    private Button begin_locate;
    private Button delete_location;


    private EditText EasyCount;
    private EditText EasyTime;

    private Button begin_locate_easy;
    private EditText service_address;
    private Button setup_service_addressBtn;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String response = (String) msg.obj;
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                    break;
                case FAIL:
                    Toast.makeText(getContext(),"发生错误:"+response,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_fingerprint,container,false);
        List<WifiInfo> wifiInfos = WifiInfoLab.get(getActivity()).getWifiInfos();

        StringBuilder builder=new StringBuilder();
        for(WifiInfo info:wifiInfos){
            if(info.getTarget()){
                targetAps.add(info);
                builder.append(info.getName()+"：IS READY"+"\n");
            }
        }

        targetAP=view.findViewById(R.id.targetAP);
        targetAP.setText(builder);

        location=view.findViewById(R.id.location);
        location_X =view.findViewById(R.id.location_X);
        location_Y=view.findViewById(R.id.location_Y);
        begin_locate=(Button)view.findViewById(R.id.begin_locate);
        delete_location=(Button)view.findViewById(R.id.delete_locate);
        EasyCount=view.findViewById(R.id.EasyCount);
        EasyTime=view.findViewById(R.id.EasyTime);
        begin_locate_easy=(Button)view.findViewById(R.id.begin_locate_Easy);

        service_address=view.findViewById(R.id.service_address);
        setup_service_addressBtn=(Button)view.findViewById(R.id.setup_service_addressBtn);
        setup_service_addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.serviceAddress=service_address.getText().toString();
            }
        });
        begin_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpClient();
            }
        });

        delete_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpDeleteClient();
            }
        });




        begin_locate_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=11;
                if(!TextUtils.isEmpty(EasyCount.getText())){
                     count=Integer.valueOf(EasyCount.getText().toString());
                }
                int time=1000;
                if(!TextUtils.isEmpty(EasyTime.getText()))
                {
                    time=Integer.valueOf(EasyTime.getText().toString());
                }
                sendRequestWithHttpClientEasy(count,time);
            }
        });

        Toast.makeText(getContext(),"当前服务器地址:"+ Constant.serviceAddress,Toast.LENGTH_SHORT).show();
        return view;
    }

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

    private void sendRequestWithHttpDeleteClient(){
        if(TextUtils.isEmpty(location.getText())||TextUtils.isEmpty(location_X.getText())||TextUtils.isEmpty(location_Y.getText())){
            Message message = new Message();
            message.what = FAIL;
            message.obj = "信息缺失";
            handler.sendMessage(message);
            return;
        }




        new Thread(new Runnable() {
            @Override
            public void run() {



//
//                Map<String,String> map=new HashMap<>();
//                map.put("param",URLEncoder.encode(JSONArray.toJSONString(result)));
////                    String response=HttpUtils.getResultForHttpGet(Constant.serviceAddress+"/makeSome",map);
//                String response=HttpUtils.methodPost(Constant.serviceAddress+"/makeSomeByPost",JSONArray.toJSONString(result));



                final List<Fingerprint> result=new ArrayList<>();
                Message message = new Message();
                try{
                    Map<String,String> map=new HashMap<>();
                    result.add(new Fingerprint("delete",location.getText().toString(),location_X.getText().toString(),location_Y.getText().toString()));

                    //加密
                    TreeMap<String,Object> treeMap=new TreeMap<>();
                    treeMap.put("data",result);
                    String data=EncryptionService.client(treeMap);

                    //编码
                    data=URLEncoder.encode(data);

                    //传输
                    String response=HttpUtils.methodPost(Constant.serviceAddress+"/delete",data);
                    if(response!=null&&"deleted".equals(response)){
                        message.what = SUCCESS;
                        message.obj = response;
                    }else {
                        //数据库服务器错误
                        message.what = FAIL;
                        message.obj = "发生错误:"+response;
                    }


                }catch (Exception e){
                    message.what = FAIL;
                    message.obj = "发生错误："+e.toString();
                }
                handler.sendMessage(message);


            }
        }).start();







    }


    private void sendRequestWithHttpClient() {
        final List<Fingerprint> result=new ArrayList<>();
        if(TextUtils.isEmpty(location.getText())||TextUtils.isEmpty(location_X.getText())||TextUtils.isEmpty(location_Y.getText())){
            Message message = new Message();
            message.what = FAIL;
            message.obj = "信息缺失";
            handler.sendMessage(message);
            return;
        }
        if(targetAps.size()==0){
            Message message = new Message();
            message.what = FAIL;
            message.obj = "未选中AP";
            handler.sendMessage(message);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                ScheduledExecutorService service= Executors.newScheduledThreadPool(4);
                service.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        //统计数据
                            Map<String, String> Fingerprint_result = new HashMap<>();
                            List<WifiInfo> infos = scan(getContext());
                            for (WifiInfo targetInfo : targetAps) {
                                for (WifiInfo scanInfo : infos) {
                                    if (targetInfo.getMac().equals(scanInfo.getMac())) {
                                        Fingerprint_result.put(targetInfo.getMac(), scanInfo.getRss());
                                    }
                                }
                            }
                            if(targetAps.size()==Fingerprint_result.size()&&!Fingerprint_result.isEmpty()){
                                Fingerprint fingerprint = new Fingerprint(Fingerprint_result.toString(),
                                        location.getText().toString(), location_X.getText().toString(), location_Y.getText().toString());
                                result.add(fingerprint);
                            }

                    }
                },0,500, TimeUnit.MILLISECONDS);


                try{
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                service.shutdown();
//                try {
//                    //用HttpClient发送请求，分为五步
//                    //第一步：创建HttpClient对象
//                    HttpClient httpClient = new DefaultHttpClient();
//                    //第二步：创建代表请求的对象,参数是访问的服务器地址
//                    String strUrl=Constant.serviceAddress+"/makeSome"+"?param="+ URLEncoder.encode(JSONArray.toJSONString(result));
//                    URL url = new URL(strUrl);
////                    URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
//                    URI uri=new URI(url.getProtocol(),null,url.getHost(),url.getPort(),url.getPath(),url.getQuery(),null);
//                    HttpGet httpGet = new HttpGet(uri);
//
//
//
//                    //第三步：执行请求，获取服务器发还的相应对象
//                    Message message = new Message();
//                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
//                    if (httpResponse!=null&&httpResponse.getStatusLine().getStatusCode() == 200) {
//                       //第五步：从相应对象当中取出数据，放到entity当中
//                       HttpEntity entity = httpResponse.getEntity();
//                       String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
//                       //在子线程中将Message对象发出去
//                       message.what = SUCCESS;
//                       message.obj = response.toString();
//                    }else{
//                        message.what = FAIL;
//                        message.obj = "发生错误";
//                    }
//                    handler.sendMessage(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Message message = new Message();
                try{
                    Map<String,String> map=new HashMap<>();
                    if(result.size()!=0){
                        map.put("param",URLEncoder.encode(JSONArray.toJSONString(result)));
                        String response=HttpUtils.getResultForHttpGet(Constant.serviceAddress+"/makeSome",map);
                        if(response!=null&&"saved".equals(response)){
                            message.what = SUCCESS;
                            message.obj = response;
                        }else {
                            //数据库服务器错误
                            message.what = FAIL;
                            message.obj = "发生错误:"+response;
                        }
                    }else{
                        //
                        message.what = FAIL;
                        message.obj = "发生错误:指纹质量不达标";
                    }

                }catch (Exception e){
                    message.what = FAIL;
                    message.obj = "发生错误："+e.toString();
                }
                handler.sendMessage(message);
            }
        }).start();
    }



    private void sendRequestWithHttpClientEasy(final int count, final int time) {
        if(TextUtils.isEmpty(location.getText())||TextUtils.isEmpty(location_X.getText())||TextUtils.isEmpty(location_Y.getText())){
            Message message = new Message();
            message.what = FAIL;
            message.obj = "信息缺失";
            handler.sendMessage(message);
            return;
        }
        final List<Fingerprint> result=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final ScheduledExecutorService service= Executors.newScheduledThreadPool(4);
                service.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        //统计数据
                        Map<String, String> Fingerprint_result = new HashMap<>();
                        List<WifiInfo> infos = scan(getContext());
                        if(infos.size()==0){
                            Message message = new Message();
                            message.what = FAIL;
                            message.obj = "扫描到空WIFI列表";
                            handler.sendMessage(message);
                            service.shutdown();
                            return;
                        }

                        for(int i=0;i<count;i++){
                            Fingerprint_result.put(infos.get(i).getMac(), infos.get(i).getRss());
                        }

                        Fingerprint fingerprint = new Fingerprint(Fingerprint_result.toString(),
                                location.getText().toString(), location_X.getText().toString(), location_Y.getText().toString());
                        result.add(fingerprint);

                    }
                },0,500, TimeUnit.MILLISECONDS);


                try{
                    Thread.sleep(time);
                }catch (Exception e){
                    e.printStackTrace();
                }
                service.shutdown();



//                try {
//                    //用HttpClient发送请求，分为五步
//                    //第一步：创建HttpClient对象
//                    HttpClient httpClient = new DefaultHttpClient();
//                    //第二步：创建代表请求的对象,参数是访问的服务器地址
//                    String strUrl=Constant.serviceAddress+"/makeSome"+"?param="+ URLEncoder.encode(JSONArray.toJSONString(result));
//                    URL url = new URL(strUrl);
////                    URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
//                    URI uri=new URI(url.getProtocol(),null,url.getHost(),url.getPort(),url.getPath(),url.getQuery(),null);
//                    HttpGet httpGet = new HttpGet(uri);
//
//
//
//                    //第三步：执行请求，获取服务器发还的相应对象
//                    Message message = new Message();
//                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
//                    if (httpResponse!=null&&httpResponse.getStatusLine().getStatusCode() == 200) {
//                        //第五步：从相应对象当中取出数据，放到entity当中
//                        HttpEntity entity = httpResponse.getEntity();
//                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
//                        //在子线程中将Message对象发出去
//                        message.what = SUCCESS;
//                        message.obj = response.toString();
//                    }else{
//                        message.what = FAIL;
//                        message.obj = "发生错误";
//                    }
//                    handler.sendMessage(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                Message message = new Message();
                try{
//                    Map<String,String> map=new HashMap<>();
//                    map.put("param",URLEncoder.encode(JSONArray.toJSONString(result)));
//                    String response=HttpUtils.getResultForHttpGet(Constant.serviceAddress+"/makeSome",map);
                    String response=HttpUtils.methodPost(Constant.serviceAddress+"/makeSomeByPost",JSONArray.toJSONString(result));
                    if(response!=null&&"saved".equals(response)){
                        message.what = SUCCESS;
                        message.obj = response;
                    }else {
                        message.what = FAIL;
                        message.obj = "发生错误:"+response;
                    }
                }catch (Exception e){
                    message.what = FAIL;
                    message.obj = "发生错误:"+e.toString();
                }
                handler.sendMessage(message);
            }
        }).start();
    }


    public static void main(String[] args) {

        final List<Fingerprint> result=new ArrayList<>();
        try {
            Map<String, String> map = new HashMap<>();
            result.add(new Fingerprint("delete", "测试", "0", "1"));

            //加密
            TreeMap<String, Object> treeMap = new TreeMap<>();
            treeMap.put("data", "1");
            String data = EncryptionService.client(treeMap);

            System.out.println(data);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
