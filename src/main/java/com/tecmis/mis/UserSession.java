package com.tecmis.mis;

import java.util.Set;

public final class UserSession {

    private static UserSession instance;

    private static String userName;
    private static String userTgNum;
    private Set<String> privileges;

    private UserSession(String userName, String userTgNum, Set<String> privileges) {
        this.userName = userName;
        this.userTgNum = userTgNum;
        this.privileges = privileges;
    }

    public static UserSession getInstance(String userName, String userTgNum, Set<String> privileges) {
        if(instance == null) {
            instance = new UserSession(userName, userTgNum, privileges);
        }
        return instance;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserTgNum() {
        return userTgNum;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public static void cleanUserSession() {
        userName = null;
        userTgNum = null;
        instance = null;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
