package com.app.badoli.model;

public class UserProfile {
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

        private String name;

        private String email;

        private String mobile;

        private int roles_id;

        private String airtel_merchant_id;

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        private String activity_name;

        private String company_name;

        private String company_address;

        private int merchant_activity_id;

        private int country_code;

        private String wallet_balance;

        private String user_image;

        private String qrcode_image;

        private int device_type;

        private String device_token;

        private int status;

        private String last_login;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setEmail(String email){
            this.email = email;
        }
        public String getEmail(){
            return this.email;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public String getMobile(){
            return this.mobile;
        }
        public void setRoles_id(int roles_id){
            this.roles_id = roles_id;
        }
        public int getRoles_id(){
            return this.roles_id;
        }
        public void setAirtel_merchant_id(String airtel_merchant_id){
            this.airtel_merchant_id = airtel_merchant_id;
        }
        public String getAirtel_merchant_id(){
            return this.airtel_merchant_id;
        }
        public void setCompany_name(String company_name){
            this.company_name = company_name;
        }
        public String getCompany_name(){
            return this.company_name;
        }
        public void setCompany_address(String company_address){
            this.company_address = company_address;
        }
        public String getCompany_address(){
            return this.company_address;
        }
        public void setMerchant_activity_id(int merchant_activity_id){
            this.merchant_activity_id = merchant_activity_id;
        }
        public int getMerchant_activity_id(){
            return this.merchant_activity_id;
        }
        public void setCountry_code(int country_code){
            this.country_code = country_code;
        }
        public int getCountry_code(){
            return this.country_code;
        }
        public void setWallet_balance(String wallet_balance){
            this.wallet_balance = wallet_balance;
        }
        public String getWallet_balance(){
            return this.wallet_balance;
        }
        public void setUser_image(String user_image){
            this.user_image = user_image;
        }
        public String getUser_image(){
            return this.user_image;
        }
        public void setQrcode_image(String qrcode_image){
            this.qrcode_image = qrcode_image;
        }
        public String getQrcode_image(){
            return this.qrcode_image;
        }
        public void setDevice_type(int device_type){
            this.device_type = device_type;
        }
        public int getDevice_type(){
            return this.device_type;
        }
        public void setDevice_token(String device_token){
            this.device_token = device_token;
        }
        public String getDevice_token(){
            return this.device_token;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }
        public void setLast_login(String last_login){
            this.last_login = last_login;
        }
        public String getLast_login(){
            return this.last_login;
        }
    }


}
