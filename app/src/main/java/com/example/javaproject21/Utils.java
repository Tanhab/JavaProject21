package com.example.javaproject21;

import androidx.annotation.NonNull;

public class Utils {
    public static String  CR,className,UserName,classDescription,CR2,invitationCode,imageUri,nickName,topic;

    public static String getTopic() {
        return topic;
    }

    public static void setTopic(String topic) {
        Utils.topic = topic;
    }

    public static String getImageUri() {
        return imageUri;
    }

    public static String getNickName() {
        return nickName;
    }

    public static void setNickName(String nickName) {
        Utils.nickName = nickName;
    }

    public static void setImageUri(String imageUri) {
        Utils.imageUri = imageUri;
    }

    public static String getInvitationCode() {
        return invitationCode;
    }

    public static void setInvitationCode(String invitationCode) {
        Utils.invitationCode = invitationCode;
    }

    public Utils() {
    }

    public static String getCR() {
        return CR;
    }

    public static String getClassDescription() {
        return classDescription;
    }

    public static void setClassDescription(String classDescription) {
        Utils.classDescription = classDescription;
    }

    public static String getCR2() {
        return CR2;
    }

    public static void setCR2(String CR2) {
        Utils.CR2 = CR2;
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
