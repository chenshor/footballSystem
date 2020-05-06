package com.football_system.football_system.logicTest;

import com.football_system.football_system.FMserver.LogicLayer.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityObject {

    private String userID;
    private String reqID;
    private String functionName ;
    private List<Object> object;

    private static Map<String , String> SecurityAccessControl = new HashMap<>();
    private static Map<String , User> systemUsers = new HashMap<>();

    public static boolean  Verification(String UserID , String reqID){
        if(UserID == null || reqID == null) return false;
        return SecurityAccessControl.get(UserID).equals(reqID) ;
    }
    public static User Authorization(String UserID , String reqID){
        if(Verification(UserID, reqID)){
            return systemUsers.get(UserID);
        }
        return null;
    }
    public static User Authorization(SecurityObject securityObject){
        return Authorization(securityObject.getUserID() , securityObject.getReqID());
    }
    /**
     *
     * @param user
     * @return a security key
     */
    public static String  addUserToSystem(User user){
        if(systemUsers.containsKey(user.getEmail())) return null ;
        systemUsers.put(user.getEmail(), user) ;
        //Integer key= (int) (Math.random()*10000+3000);
        String key = "1000" ;
        String secKey  =  key.toString()  ;
        SecurityAccessControl.put(user.getEmail() , secKey) ;
        return secKey ;
    }
    public SecurityObject(String userID, String reqID, String functionName, List<Object> object) {
        this.userID = userID;
        this.reqID = reqID;
        this.functionName = functionName;
        this.object = object;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String UserID) {
        this.userID = UserID;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public List<Object> getObject() {
        return object;
    }

    public void setObject(List<Object> object) {
        this.object = object;
    }


}
