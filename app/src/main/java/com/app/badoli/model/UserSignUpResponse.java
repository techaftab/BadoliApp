package com.app.badoli.model;

public class UserSignUpResponse {
    private String notification;

    private boolean error;

    private String message;

    private Result result;

    public void setNotification(String notification){
        this.notification = notification;
    }
    public String getNotification(){
        return this.notification;
    }
    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
    }
    public class Result
    {
        private int id;

        private String otp;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setOtp(String otp){
            this.otp = otp;
        }
        public String getOtp(){
            return this.otp;
        }
    }
}
