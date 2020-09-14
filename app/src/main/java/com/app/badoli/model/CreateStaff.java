package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateStaff {
    private boolean error;

    private String message;

    private Result result;

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
        private String merchant_id;

        private String agent_code;

        private String agent_pin;

        private String staff_name;

        private int status;

        private String updated_at;

        private String created_at;

        private int id;

        public void setMerchant_id(String merchant_id){
            this.merchant_id = merchant_id;
        }
        public String getMerchant_id(){
            return this.merchant_id;
        }
        public void setAgent_code(String agent_code){
            this.agent_code = agent_code;
        }
        public String getAgent_code(){
            return this.agent_code;
        }
        public void setAgent_pin(String agent_pin){
            this.agent_pin = agent_pin;
        }
        public String getAgent_pin(){
            return this.agent_pin;
        }
        public void setStaff_name(String staff_name){
            this.staff_name = staff_name;
        }
        public String getStaff_name(){
            return this.staff_name;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }
        public void setUpdated_at(String updated_at){
            this.updated_at = updated_at;
        }
        public String getUpdated_at(){
            return this.updated_at;
        }
        public void setCreated_at(String created_at){
            this.created_at = created_at;
        }
        public String getCreated_at(){
            return this.created_at;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
    }
}
