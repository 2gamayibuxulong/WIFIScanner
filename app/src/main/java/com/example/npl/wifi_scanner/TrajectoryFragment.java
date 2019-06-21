package com.example.npl.wifi_scanner;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.example.npl.wifi_scanner.model.Fingerprint;
import com.example.npl.wifi_scanner.model.Trajectory;
import com.example.npl.wifi_scanner.model.TrajectoryLab;
import com.example.npl.wifi_scanner.tool.DateUtils;
import com.example.npl.wifi_scanner.tool.DeviceUtils;

import java.util.Date;
import java.util.List;

public class TrajectoryFragment extends Fragment {
    private WebView webContent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajectory, container, false);
        webContent = (WebView) view.findViewById(R.id.web_content);
        //获取设置
        WebSettings webSettings = webContent.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //AndroidToJS类对象映射到js的test对象
        webContent.addJavascriptInterface(new AndroidToJs(), "test");
        //加载
        webContent.loadUrl("file:///android_asset/index.html");

        //初始化最先最先定位点：
        webContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("开始加载初始定位点：");
                Fingerprint fingerprint = LocateFragment.returnFingerprint(getContext(), null);
                webContent.loadUrl("javascript:beginDraw(" + fingerprint.getLocation_x()+","+fingerprint.getLocation_y()+ ")");
//                    webContent.loadUrl("javascript:drawText(" +"\""+fingerprint.getLocation()+":"+DateUtils.DateToString(new Date(),"YYYY-MM-DD HH:MM:SS.SSS")+"\""+","+fingerprint.getLocation_x()+","+fingerprint.getLocation_y()+ ")");
                webContent.loadUrl("javascript:drawText(" +"\""+DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"\""+","+fingerprint.getLocation_x()+","+fingerprint.getLocation_y()+ ")");
                System.out.println("加载初始定位点完毕："+"x:"+fingerprint.getLocation_x()
                +"y:"+fingerprint.getLocation_y());


                Trajectory trajectory=new Trajectory("631507020316",
                        DeviceUtils.getUniqueId(getActivity()), DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"),fingerprint.getFingerprint(),
                        fingerprint.getLocation(),fingerprint.getLocation_x(),
                        fingerprint.getLocation_y());
                TrajectoryLab.get(getActivity()).addTrajectory(trajectory);
            }
        });

        return view;
    }

    class AndroidToJs {
        @JavascriptInterface
        public void request_locate() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("开始请求接下来的定位点");
                    Fingerprint fingerprint = LocateFragment.returnFingerprint(getContext(), null);
                    webContent.loadUrl("javascript:returnResult(" + fingerprint.getLocation_x()+","+fingerprint.getLocation_y()+ ")");
//                    webContent.loadUrl("javascript:drawText(" +"\""+fingerprint.getLocation()+":"+DateUtils.DateToString(new Date(),"YYYY-MM-DD HH:MM:SS.SSS")+"\""+","+fingerprint.getLocation_x()+","+fingerprint.getLocation_y()+ ")");
                    webContent.loadUrl("javascript:drawText(" +"\""+DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"\""+","+fingerprint.getLocation_x()+","+fingerprint.getLocation_y()+ ")");
                    System.out.println("接下来的定位点定位完毕："+"x:"+fingerprint.getLocation_x()
                            +"y:"+fingerprint.getLocation_y());

                    Trajectory trajectory=new Trajectory("631507020316",
                            DeviceUtils.getUniqueId(getActivity()), DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"),fingerprint.getFingerprint(),
                            fingerprint.getLocation(),fingerprint.getLocation_x(),
                            fingerprint.getLocation_y());
                    TrajectoryLab.get(getActivity()).addTrajectory(trajectory);
                }
            });
        }
        @JavascriptInterface
        public void getRecord(final String date,final String where){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String start;
                    String end;
                    try{
                        start=date;
                        end=DateUtils.addOneDay(date);
                    }catch (Exception e){
                        return;
                    }
                    String[] args={where,start,end};
                    final List<Trajectory> trajectories=TrajectoryLab.get(getContext()).getTrajectories(args);
                    webContent.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            for(int i=0;i<trajectories.size();i++){
                                if (i == 0) {
                                    webContent.loadUrl("javascript:oldbeginDraw(" + trajectories.get(0).getLocation_x()+","+trajectories.get(0).getLocation_y()+ ")");
                                    webContent.loadUrl("javascript:olddrawText(" +"\""+DateUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"\""+","+trajectories.get(0).getLocation_x()+","+trajectories.get(0).getLocation_y()+ ")");
                                }else {
                                    webContent.loadUrl("javascript:oldreturnResult(" + trajectories.get(i).getLocation_x() + "," + trajectories.get(i).getLocation_y() + ")");
                                    webContent.loadUrl("javascript:olddrawText(" + "\"" + DateUtils.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "\"" + "," + trajectories.get(i).getLocation_x() + "," + trajectories.get(i).getLocation_y() + ")");
                                }
                            }
                        }
                    });

                }
            });
        }
    }



}