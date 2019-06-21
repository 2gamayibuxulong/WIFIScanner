package com.example.npl.wifi_scanner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wifiInfoBase.db";

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null , VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1="create table "+ DbSchema.FingerprintTable.NAME+"("+
                DbSchema.FingerprintTable.Cols.FINGERPRINT +" PRIMARY KEY"+","+
                DbSchema.FingerprintTable.Cols.LOCATION+","+
                DbSchema.FingerprintTable.Cols.LOCATION_X+","+
                DbSchema.FingerprintTable.Cols.LOCATION_Y+
                ")";
        String sql2="create table "+ DbSchema.TrajectoryTable.NAME+"("+
                DbSchema.TrajectoryTable.Cols.STU_ID +","+
                DbSchema.TrajectoryTable.Cols.DEVICE_ID +","+
                DbSchema.TrajectoryTable.Cols.DATE +","+
                DbSchema.TrajectoryTable.Cols.FINGERPRINT +","+
                DbSchema.TrajectoryTable.Cols.LOCATION+","+
                DbSchema.TrajectoryTable.Cols.LOCATION_X+","+
                DbSchema.TrajectoryTable.Cols.LOCATION_Y+
                ")";
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void main(String[] args) {
        String sql="create table "+ DbSchema.TrajectoryTable.NAME+"("+
                DbSchema.TrajectoryTable.Cols.STU_ID +","+
                DbSchema.TrajectoryTable.Cols.DEVICE_ID +","+
                DbSchema.TrajectoryTable.Cols.DATE +","+
                DbSchema.TrajectoryTable.Cols.FINGERPRINT +","+
                DbSchema.TrajectoryTable.Cols.LOCATION+","+
                DbSchema.TrajectoryTable.Cols.LOCATION_X+","+
                DbSchema.TrajectoryTable.Cols.LOCATION_Y+
                ")";
        System.out.println(sql);
    }
}
