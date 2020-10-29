package com.app.badoli.model;

public class StaffLogin {
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
        private int id;

        private String staff_name;

        private String agent_code;

        private String wallet_balance;

        private int agent_pin;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
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
        public void setWallet_balance(String wallet_balance){
            this.wallet_balance = wallet_balance;
        }
        public String getWallet_balance(){
            return this.wallet_balance;
        }
        public void setAgent_pin(int agent_pin){
            this.agent_pin = agent_pin;
        }
        public int getAgent_pin(){
            return this.agent_pin;
        }
    }
}
