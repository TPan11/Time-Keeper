package com.btbsolutions.timekeeper.utility;

public class ConfigurationFile {

    //Text Size for Headers
    private static int HEADER_TEXT_SIZE = 16;
    public static void setHeaderTextSize(int size){
        HEADER_TEXT_SIZE = size;
    }
    public static int getHeaderTextSize(){
        return HEADER_TEXT_SIZE;
    }

    //Text Size for Sub Headings
    private static int SUB_HEADER_TEXT_SIZE = 14;
    public static void setSubHeaderTextSize(int size){
        SUB_HEADER_TEXT_SIZE = size;
    }
    public static int getSubHeaderTextSize(){
        return SUB_HEADER_TEXT_SIZE;
    }

    //Background Color for Card of Daily Activity
    private static String DAILY_ACTIVITY_BACK_COLOR = "#ffffffff";
    public static void setDailyActivityBackColor(String color){
        DAILY_ACTIVITY_BACK_COLOR = color;
    }
    public static String getDailyActivityBackColor(){
        return DAILY_ACTIVITY_BACK_COLOR;
    }
}
