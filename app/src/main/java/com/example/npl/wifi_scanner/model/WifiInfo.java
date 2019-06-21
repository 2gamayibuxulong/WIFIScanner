package com.example.npl.wifi_scanner.model;

import java.util.Date;

public class WifiInfo {
    private String Mac;
    private String Name;
    private String Rss;
    private Date measureDate;
    private boolean isTarget;

    public WifiInfo() {
        measureDate=new Date();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public String getRss() {
        return Rss;
    }

    public void setRss(String rss) {
        Rss = rss;
    }

    public Date getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(Date measureDate) {
        this.measureDate = measureDate;
    }

    public Boolean getTarget() {
        return isTarget;
    }

    public void setTarget(Boolean target) {
        isTarget = target;
    }
}
