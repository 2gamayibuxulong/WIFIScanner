package com.example.npl.wifi_scanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.npl.wifi_scanner.db.BaseHelper;
import com.example.npl.wifi_scanner.db.DbSchema;
import com.example.npl.wifi_scanner.db.TrajectoryCursorWrapper;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryLab {
    private static TrajectoryLab trajectoryLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private TrajectoryLab(Context context){
        mContext=context.getApplicationContext();
        mDatabase=new BaseHelper(mContext)
                .getWritableDatabase();
    }

    public static synchronized TrajectoryLab get(Context context){
        if(trajectoryLab ==null){
            trajectoryLab =new TrajectoryLab(context);
        }
        return trajectoryLab;
    }

    public List<Trajectory> getTrajectories(){
        List<Trajectory> trajectories=new ArrayList<>();
        TrajectoryCursorWrapper cursor=queryTrajectories(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                trajectories.add(cursor.getTrajectory());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return trajectories;
    }


    public List<Trajectory> getTrajectories(String[] args){
        List<Trajectory> trajectories=new ArrayList<>();
        TrajectoryCursorWrapper cursor=queryTrajectories("location=? and ?<date and date>?",args);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                trajectories.add(cursor.getTrajectory());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return trajectories;
    }



    public void addTrajectory(Trajectory trajectory){
        ContentValues values=getContentValues(trajectory);
        mDatabase.insert(DbSchema.TrajectoryTable.NAME,null,values);
    }

    public void addTrajectoryByHttp(Trajectory trajectory){


    }



    private TrajectoryCursorWrapper queryTrajectories(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(
                DbSchema.TrajectoryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TrajectoryCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Trajectory trajectory){
        ContentValues values=new ContentValues();
        values.put(DbSchema.TrajectoryTable.Cols.STU_ID,trajectory.getStu_id());
        values.put(DbSchema.TrajectoryTable.Cols.DEVICE_ID,trajectory.getDevice_id());
        values.put(DbSchema.TrajectoryTable.Cols.DATE,trajectory.getMeasure_date());
        values.put(DbSchema.TrajectoryTable.Cols.FINGERPRINT,trajectory.getFingerprint());
        values.put(DbSchema.TrajectoryTable.Cols.LOCATION,trajectory.getLocation());
        values.put(DbSchema.TrajectoryTable.Cols.LOCATION_X,trajectory.getLocation_x());
        values.put(DbSchema.TrajectoryTable.Cols.LOCATION_Y,trajectory.getLocation_y());
        return values;
    }

}
