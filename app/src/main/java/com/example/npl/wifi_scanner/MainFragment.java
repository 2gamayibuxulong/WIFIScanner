package com.example.npl.wifi_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.npl.wifi_scanner.model.Fingerprint;
import com.example.npl.wifi_scanner.model.FingerprintHttpLab;
import com.example.npl.wifi_scanner.tool.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

public class MainFragment extends Fragment implements View.OnClickListener {
    private Button look_aroundBtn;
    private Button formingFingerprintBtn;
    private Button begin_locateBtn;
    private Button otherBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendForData();
    }

    private void sendForData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //用HttpClient发送请求，分为五步
                    //第一步：创建HttpClient对象
                    HttpClient httpCient = new DefaultHttpClient();
                    //第二步：创建代表请求的对象,参数是访问的服务器地址
                    String strUrl= Constant.serviceAddress +"/querySome";
                    URL url = new URL(strUrl);
                    URI uri=new URI(url.getProtocol(),null,url.getHost(),url.getPort(),url.getPath(),url.getQuery(),null);
                    HttpGet httpGet = new HttpGet(uri);

                    //第三步：执行请求，获取服务器发还的相应对象
                    Message message = new Message();
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse!=null&&httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        //在子线程中将Message对象发出去
                        response= URLDecoder.decode(response);
                        FingerprintHttpLab.setFingerprintList(JSON.parseArray(response, Fingerprint.class));
                    }
                } catch (Exception e) {
                    System.out.println("_________________________"+e);
                }

            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        look_aroundBtn=(Button)view.findViewById(R.id.look_around);
        formingFingerprintBtn=(Button)view.findViewById(R.id.formingFingerprint);
        begin_locateBtn=(Button)view.findViewById(R.id.begin_locate);
        otherBtn=(Button)view.findViewById(R.id.show_Trajectory);

        look_aroundBtn.setOnClickListener(this);
        formingFingerprintBtn.setOnClickListener(this);
        begin_locateBtn.setOnClickListener(this);
        otherBtn.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.look_around:
                intent=MainActivity.newIntent(getContext(),"look_around");
                startActivity(intent);
                break;
            case R.id.formingFingerprint:
                intent=MainActivity.newIntent(getContext(),"formingFingerprint");
                startActivity(intent);
                break;
            case R.id.begin_locate:
                intent=MainActivity.newIntent(getContext(),"begin_locate");
                startActivity(intent);
                break;
            case R.id.show_Trajectory:
                intent=MainActivity.newIntent(getContext(),"show_Trajectory");
                startActivity(intent);
                break;
            default:
                intent=new Intent();
        }
    }
}
