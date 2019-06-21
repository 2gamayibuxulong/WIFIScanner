package com.example.npl.wifi_scanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.npl.wifi_scanner.db.BaseHelper;
import com.example.npl.wifi_scanner.db.FingerprintCursorWrapper;
import com.example.npl.wifi_scanner.db.DbSchema;

import java.util.ArrayList;
import java.util.List;

public class FingerprintLab {
    private static FingerprintLab fingerprintLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;





    private FingerprintLab(Context context) {
        mContext=context.getApplicationContext();
        mDatabase=new BaseHelper(mContext)
                .getWritableDatabase();
    }

    public static synchronized FingerprintLab get(Context context){
        if(fingerprintLab ==null){
            fingerprintLab =new FingerprintLab(context);
        }
        return fingerprintLab;
    }

    public List<Fingerprint> getFingerprints(String[] args){
        List<Fingerprint> fingerprints=new ArrayList<>();
        FingerprintCursorWrapper cursor=queryFingerprints("location=?",args);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                fingerprints.add(cursor.getFingerprint());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return fingerprints;
    }

    public List<Fingerprint> getFingerprints(){
        List<Fingerprint> fingerprints=new ArrayList<>();
        FingerprintCursorWrapper cursor=queryFingerprints(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                fingerprints.add(cursor.getFingerprint());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return fingerprints;
    }












    private static ContentValues getContentValues(Fingerprint fingerprint){
        ContentValues values=new ContentValues();
        values.put(DbSchema.FingerprintTable.Cols.FINGERPRINT,fingerprint.getFingerprint());
        values.put(DbSchema.FingerprintTable.Cols.LOCATION,fingerprint.getLocation());
        values.put(DbSchema.FingerprintTable.Cols.LOCATION_X,fingerprint.getLocation_x());
        values.put(DbSchema.FingerprintTable.Cols.LOCATION_Y,fingerprint.getLocation_y());
        return values;
    }


    public void addFingerprint(Fingerprint fingerprint){
        ContentValues values=getContentValues(fingerprint);
        mDatabase.insert(DbSchema.FingerprintTable.NAME,null,values);
    }





    private FingerprintCursorWrapper queryFingerprints(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(
                DbSchema.FingerprintTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new FingerprintCursorWrapper(cursor);
    }
}
