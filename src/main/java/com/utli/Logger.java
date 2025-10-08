package com.utli;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 簡單的 Logger，輸出訊息到 console，包含台北時間及毫秒。
 */
public class Logger {
    // 包含毫秒（SSS），範例輸出：2025年10月08 14:05:07.123
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd HH:mm:ss.SSS");
    private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");

    public void log(String msg) {
        String nowTime = ZonedDateTime.now(ZONE_TAIPEI).format(DT_FMT);
        System.out.println(nowTime + ": " + msg);
    }
}
