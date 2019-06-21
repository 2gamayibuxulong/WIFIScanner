package com.example.npl.wifi_scanner.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.npl.wifi_scanner.model.Trajectory;

import java.util.Date;

public class TrajectoryCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TrajectoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Trajectory getTrajectory(){
        String STU_ID=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.STU_ID));
        String DEVICE_ID=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.DEVICE_ID));
        String DATE=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.DATE));
        String FINGERPRINT=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.FINGERPRINT));
        String LOCATION=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.LOCATION));
        String LOCATION_X=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.LOCATION_X));
        String LOCATION_Y=getString(getColumnIndex(DbSchema.TrajectoryTable.Cols.LOCATION_Y));

        Trajectory trajectory=new Trajectory(STU_ID,DEVICE_ID,DATE,FINGERPRINT,LOCATION,LOCATION_X,LOCATION_Y);

        return trajectory;
    }
}
