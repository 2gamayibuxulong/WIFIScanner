package com.example.npl.wifi_scanner.model;

import android.content.Context;

import java.util.List;

public class FingerprintHttpLab {

    private static List<Fingerprint> fingerprintList;

    public static List<Fingerprint> getFingerprintList() {
        return fingerprintList;
    }

    public static void setFingerprintList(List<Fingerprint> fingerprintList1) {
        fingerprintList = fingerprintList1;
    }
}
