package com.football_system.football_system.serverObjects;

import com.football_system.football_system.FMserver.LogicLayer.DataComp;
import com.football_system.football_system.FMserver.LogicLayer.User;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SecurityObject {

    private String userID;
    private String reqID;
    private String functionName ;
    private List<Object> object;

    private static Map<String , String> SecurityAccessControl = new HashMap<>();
   // private static Map<String , User> systemUsers = new HashMap<>();

    public static boolean  Verification(String UserID , String reqID){
        if(UserID == null || reqID == null) return false;
        return SecurityAccessControl.get(UserID).equals(reqID) ;
    }
    public static User Authorization(String UserID , String reqID){
        if(Verification(UserID, reqID)){
            return DataComp.getInstance().getUserByMail(null,UserID) ;// systemUsers.get(UserID);
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
    public static boolean  addUserToSystem(User user){
//        if(systemUsers.containsKey(user.getEmail())) return false ;
//        systemUsers.put(user.getEmail(), user) ;
        return true;
    }

    public static String  logInToSystem(User user) {
        byte[] array = new byte[32]; // length is bounded by 32
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        String secKey  =  generatedString.toString()  ;
        System.out.println(secKey);
        SecurityAccessControl.put(user.getEmail() , secKey) ;
        return secKey ;
    }
    public SecurityObject(){} ;

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
