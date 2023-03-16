package com.tecmis.mis;

import java.util.Set;

public final class UserSession {

    private static UserSession instance;

    private static String userName;
    private static String userTgNum;
    private Set<String> privileges;
    private static String depId;

    private UserSession( String userTgNum,Set<String> privileges,String depId) {
        this.userTgNum = userTgNum;
        this.privileges = privileges;
        this.depId=depId;
    }

    public static UserSession getInstance(String userTgNum, Set<String> privileges,String depId) {
        if(instance == null) {
            instance = new UserSession( userTgNum, privileges,depId);
        }
        return instance;
    }


    public static String getUserTgNum() {
        return userTgNum;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public static String getUserDepId(){
        return depId;
    }

    public static void cleanUserSession() {
        userName = null;
        userTgNum = null;
        instance = null;
        depId = null;

    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
