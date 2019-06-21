package com.example.npl.wifi_scanner.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.npl.wifi_scanner.model.Fingerprint;

public class FingerprintCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public FingerprintCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Fingerprint getFingerprint(){
        String FINGERPRINT=getString(getColumnIndex(DbSchema.FingerprintTable.Cols.FINGERPRINT));
        String LOCATION=getString(getColumnIndex(DbSchema.FingerprintTable.Cols.LOCATION));
        String LOCATION_X=getString(getColumnIndex(DbSchema.FingerprintTable.Cols.LOCATION_X));
        String LOCATION_Y=getString(getColumnIndex(DbSchema.FingerprintTable.Cols.LOCATION_Y));

        Fingerprint fingerprint=new Fingerprint(FINGERPRINT,LOCATION,LOCATION_X,LOCATION_Y);

        return fingerprint;
    }
}
