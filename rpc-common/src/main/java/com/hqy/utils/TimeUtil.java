package com.hqy.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formatted = sdf.format(new Date());
//        Instant instant = new Instan
        return Instant.now().toEpochMilli() + "";
    }
}
