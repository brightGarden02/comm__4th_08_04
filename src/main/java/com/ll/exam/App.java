package com.ll.exam;

public class App {
    public static String DB_HOST = "localhost";
    public static int DB_PORT = 3306;
    public static String DB_ID = "lldjlocal";
    public static String DB_PASSWORD = "1234";
    public static String DB_NAME = "blog";
    public static boolean isProd = false;

    static {
        String profilesActive = System.getenv("SPRING_PROFILES_ACTIVE");

        if (profilesActive != null && profilesActive.equals("production")) {
            isProd = true;
        }

        if (isProd) {
            DB_HOST = "172.17.0.1";
            DB_PORT = 3306;
            DB_ID = "lldj";
            DB_PASSWORD = "lldj123414";
            DB_NAME = "site1";
        }
    }
}
