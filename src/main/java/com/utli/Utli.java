package com.utli;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utli {    
    // 包含毫秒（SSS），範例輸出：2025年10月08 14:05:07.123
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");
    private static final String TOKEN_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final java.util.Random TOKEN_RANDOM = new java.util.Random();

    public void log(String msg) {
        String nowTime = ZonedDateTime.now(ZONE_TAIPEI).format(DT_FMT);
        System.out.println(nowTime + ": " + msg);
    }

    // 實作 MD5 工具方法
    public String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException | java.lang.NullPointerException e) {
            return null;
        }
    }

    public String getToken() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int idx = TOKEN_RANDOM.nextInt(TOKEN_CHARS.length());
            sb.append(TOKEN_CHARS.charAt(idx));
        }
        return sb.toString();
    }
}
