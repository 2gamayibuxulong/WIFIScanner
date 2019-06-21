package com.example.npl.wifi_scanner.tool;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.npl.wifi_scanner.tool.Constant.serviceAddress;

public class HttpUtils {


    public static String getResultForHttpGet(String path, Map<String,String> map) throws ClientProtocolException, IOException, URISyntaxException {
        //服务器  ：服务器项目  ：servlet名称
        String strUrl=path;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            strUrl+="?"+entry.getKey()+"="+entry.getValue();
        }

// 用HttpClient发送请求，分为五步
        try{

            //第一步：创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            //第二步：创建代表请求的对象,参数是访问的服务器地址
            URL url = new URL(strUrl);
            URI uri=new URI(url.getProtocol(),null,url.getHost(),url.getPort(),url.getPath(),url.getQuery(),null);
            HttpGet httpGet = new HttpGet(uri);
            //第三步：执行请求，获取服务器发还的相应对象
            HttpResponse httpResponse = httpClient.execute(httpGet);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse!=null&&httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                //在子线程中将Message对象发出去
                return response;
            }else{
                return null;
            }

        }catch (Exception e){
            return null;
        }


    }

    public static String sendResultByHttpPost(String path,String data){
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(path);
        try {
                StringEntity s = new StringEntity(data.toString());
                s.setContentEncoding("UTF-8");
                post.setEntity(s);

                HttpResponse httpResponse = client.execute(post);
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                    //在子线程中将Message对象发出去
                    return response;
                }else{
                    return  null;
                }
            } catch (Exception e) {
                return null;
            }
    }



    public static String methodPost(String uri,String data) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        // 目标地址
        HttpPost httppost = new HttpPost(uri);
        System.out.println("请求: " + httppost.getRequestLine());

        // post 参数 传递
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        nvps.add(new BasicNameValuePair("param", data)); // 参数
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); // 设置参数给Post
        // 执行
        HttpResponse httpResponse = httpclient.execute(httppost);

        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity entity = httpResponse.getEntity();
            String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
            //在子线程中将Message对象发出去
            return response;
        }else{
            return  null;
        }
    }

}
