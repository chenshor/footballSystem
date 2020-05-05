package com.football_system.football_system.logicTest;

import java.util.List;

public class SecurityObject {

    private String UserID;
    private String reqID;
    private String functionName ;
    private List<Object> object;


    public SecurityObject(String userID, String reqID, String functionName, List<Object> object) {
        UserID = userID;
        this.reqID = reqID;
        this.functionName = functionName;
        this.object = object;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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
