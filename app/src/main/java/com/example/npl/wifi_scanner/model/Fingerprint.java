package com.example.npl.wifi_scanner.model;

public class Fingerprint {
    private String fingerprint;
    private String location;
    private String location_x;
    private String location_y;

    public Fingerprint(){
        fingerprint="未知";
        location="未知";
        location_x="0";
        location_y="0";
    }

    public Fingerprint(String print){
        fingerprint=print;
        location="未知";
        location_x="0";
        location_y="0";
    }
    public Fingerprint(String fingerprint1,String location1,String location_x1,String location_y1){
        fingerprint=fingerprint1;
        location=location1;
        location_x=location_x1;
        location_y=location_y1;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation_x() {
        return location_x;
    }

    public void setLocation_x(String location_x) {
        this.location_x = location_x;
    }

    public String getLocation_y() {
        return location_y;
    }


    public void setLocation_y(String location_y) {
        this.location_y = location_y;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
