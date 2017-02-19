package com.rejimalson.finddonors.config;

public class AppConfig {
    //Splash Screen wait time in milli seconds
    public static final int SPLASH_SCREEN_WAIT_TIME = 3000;
    //Gender List
    public static final String GENDER_LIST[] = {"Select gender", "Male", "Female", "Transgender"};
    //Blood Groups List
    public static String BLOOD_GROUP_LIST[] = {"Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "A1+",
            "A1-", "A2+", "A2-", "A1B+", "A1B-", "A2B+", "A2B-", "Bombay Blood Group"};

    //Email id validation function
    public static boolean validateEmail(String mail) {
        String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+";
        return mail.matches(emailPattern);
    }
    //Password validation function
    public static boolean validatePassword(String pd) {
        return pd.length() >= 6;
    }
}
