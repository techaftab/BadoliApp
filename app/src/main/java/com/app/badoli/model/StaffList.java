package com.app.badoli.model;

import java.util.List;

public class StaffList {

    private boolean error;

    private String message;

    private List<Result> result;

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
    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
        return this.result;
    }

    public class Result
    {
        private int id;

        private int merchant_id;

        private String staff_name;

        private String agent_code;

        private int agent_pin;

        private String wallet_balance;

        private int status;

        private int agent_deleted;

        private String created_at;

        private String updated_at;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setMerchant_id(int merchant_id){
            this.merchant_id = merchant_id;
        }
        public int getMerchant_id(){
            return this.merchant_id;
        }
        public void setStaff_name(String staff_name){
            this.staff_name = staff_name;
        }
        public String getStaff_name(){
            return this.staff_name;
        }
        public void setAgent_code(String agent_code){
            this.agent_code = agent_code;
        }
        public String getAgent_code(){
            return this.agent_code;
        }
        public void setAgent_pin(int agent_pin){
            this.agent_pin = agent_pin;
        }
        public int getAgent_pin(){
            return this.agent_pin;
        }
        public void setWallet_balance(String wallet_balance){
            this.wallet_balance = wallet_balance;
        }
        public String getWallet_balance(){
            return this.wallet_balance;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }
        public void setAgent_deleted(int agent_deleted){
            this.agent_deleted = agent_deleted;
        }
        public int getAgent_deleted(){
            return this.agent_deleted;
        }
        public void setCreated_at(String created_at){
            this.created_at = created_at;
        }
        public String getCreated_at(){
            return this.created_at;
        }
        public void setUpdated_at(String updated_at){
            this.updated_at = updated_at;
        }
        public String getUpdated_at(){
            return this.updated_at;
        }
    }
}
