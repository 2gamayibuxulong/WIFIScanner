package com.example.npl.wifi_scanner.db;

import java.util.Date;

public class DbSchema {
    public static final class FingerprintTable{
        public static final String NAME="fingerprints";

        public static final class Cols{
            public static final String LOCATION  = "location";
            public static final String LOCATION_X  = "location_x";
            public static final String LOCATION_Y  = "location_y";
            public static final String FINGERPRINT = "fingerprint";

        }
    }

    public static final class TrajectoryTable{
        public static final String NAME="trajectories";

        public static final class Cols{
            public static final String STU_ID  = "stu_id";
            public static final String DEVICE_ID  = "device_id";
//            public static final String DATE  = "measure_date";
            public static final String DATE  = "date";
            public static final String FINGERPRINT = "fingerprint";
            public static final String LOCATION = "location";
            public static final String LOCATION_X  = "location_x";
            public static final String LOCATION_Y  = "location_y";


        }
    }
}
