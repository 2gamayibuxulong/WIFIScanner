package com.example.npl.wifi_scanner.model;

import java.util.Date;

public class TrajectoryHttp {
    private String stu_id  ;
    private String device_id  ;
    private Date measure_date  ;
    private String fingerprint ;
    private String location ;
    private String location_x  ;
    private String location_y  ;

    public TrajectoryHttp(String stu_id,String device_id,Date date,String fingerprint,String location, String location_x,String location_y ){
        this.stu_id=stu_id;
        this.device_id=device_id;
        this.measure_date=date;
        this.fingerprint=fingerprint;
        this.location=location;
        this.location_x=location_x;
        this.location_y=location_y;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Date getDate() {
        return measure_date;
    }

    public void setDate(Date date) {
        this.measure_date = date;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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
}
