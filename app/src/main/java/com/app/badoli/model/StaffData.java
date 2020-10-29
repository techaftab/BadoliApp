package com.app.badoli.model;

public class StaffData {

    String id,staffName,agentCode,walletBalance,agentPin,userType;

    public StaffData(String id, String staffName, String agentCode, String walletBalance, String agentPin, String userType) {
        this.id = id;
        this.staffName = staffName;
        this.agentCode = agentCode;
        this.walletBalance = walletBalance;
        this.agentPin = agentPin;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getAgentPin() {
        return agentPin;
    }

    public void setAgentPin(String agentPin) {
        this.agentPin = agentPin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
