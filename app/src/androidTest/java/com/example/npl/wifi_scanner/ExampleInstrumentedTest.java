package com.example.npl.wifi_scanner;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.npl.wifi_scanner.model.Fingerprint;
import com.example.npl.wifi_scanner.model.TrajectoryHttp;
import com.example.npl.wifi_scanner.tool.DeviceUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.npl.wifi_scanner", appContext.getPackageName());
    }


    @Test
    public void testJson(){
        Fingerprint f1=new Fingerprint("1","2","3","4");
        Fingerprint f2=new Fingerprint("1","2","3","4");
        List<Fingerprint> result=new ArrayList<>();
        result.add(f1);
        result.add(f2);
        System.out.println(JSON.toJSONString(result));
    }


}
