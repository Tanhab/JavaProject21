package com.example.javaproject21;

import androidx.annotation.NonNull;

public class Utils {
    public static String  CR,className,UserName;

    public Utils() {
    }

    public static String getCR() {
        return CR;
    }

    public static void setCR(String CR) {
        Utils.CR = CR;
    }

    public static String getClassName() {
        return className;
    }

    public static void setClassName(String className) {
        Utils.className = className;
    }

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }
}
