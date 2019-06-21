package com.example.npl.wifi_scanner;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.npl.wifi_scanner.model.Fingerprint;
import com.example.npl.wifi_scanner.model.FingerprintHttpLab;
import com.example.npl.wifi_scanner.model.Trajectory;
import com.example.npl.wifi_scanner.model.TrajectoryHttp;
import com.example.npl.wifi_scanner.tool.Constant;
import com.example.npl.wifi_scanner.tool.DateUtils;
import com.example.npl.wifi_scanner.tool.DeviceUtils;
import com.example.npl.wifi_scanner.tool.HttpUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LocateService extends Service {
    private static final String TAG = "LocateService";
    private LocateService.LocateThread locateThread;

    public LocateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
//        android.os.Debug.waitForDebugger();
        super.onCreate();
        this.locateThread = new LocateThread();
        this.locateThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocateThread extends  Thread{
        @Override
        public void run() {

            ScheduledExecutorService service= Executors.newScheduledThreadPool(4);
            service.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
//                    System.out.println("正在扫描附近的WIFI信号");
                    Fingerprint locate_Fingerprint = LocateFragment.returnFingerprint(getApplicationContext(), null);
                    if(locate_Fingerprint!=null){
                        System.out.println(locate_Fingerprint.getLocation()+":"+
                        locate_Fingerprint.getLocation_x()+":"+
                        locate_Fingerprint.getLocation_y());
                        if(!"未知".equals(locate_Fingerprint.getLocation())){
                            //发http请求：
                            Trajectory trajectory=new Trajectory("1",
                                    DeviceUtils.getUniqueId(getApplicationContext()),
                                    DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"),
                                    locate_Fingerprint.getFingerprint(),
                                    locate_Fingerprint.getLocation(),locate_Fingerprint.getLocation_x(),
                                    locate_Fingerprint.getLocation_y());
//
//                            try {
//                                //用HttpClient发送请求，分为五步
//                                //第一步：创建HttpClient对象
//                                HttpClient httpClient = new DefaultHttpClient();
//                                //第二步：创建代表请求的对象,参数是访问的服务器地址
//                                String strUrl= Constant.serviceAddress +"/saveTrajectoryData"+"?param="+ URLEncoder.encode(JSONObject.toJSONString(trajectory));
//                                URL url = new URL(strUrl);
//                                URI uri=new URI(url.getProtocol(),null,url.getHost(),url.getPort(),url.getPath(),url.getQuery(),null);
//                                HttpGet httpGet = new HttpGet(uri);
//                                HttpResponse httpRespons=httpClient.execute(httpGet);
//                                System.out.println(TAG+httpRespons.getStatusLine().getStatusCode());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            try {
                                HttpUtils.methodPost(Constant.serviceAddress +"/saveTrajectoryData",JSONObject.toJSONString(trajectory));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            },0,1000, TimeUnit.MILLISECONDS);

        }
    }
}
