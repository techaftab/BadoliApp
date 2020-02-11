package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AirtelResponse {

    /*1000 SUCCESS  Transaction initiated successfully*/
    /*1101 ERROR Failed to initialize the transaction*/
    /*1102 ERROR Invalid scale*/
    /*1103 ERROR Walking to a task in progress*/
    /*1104 ERROR Unknown merchant
    1105 ERROR -
    1106 ERROR Invalid Token
    1107 ERROR Invalid settings*/

    @SerializedName("transaction_reference")
    @Expose
    public String transaction_reference;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("response_code")
    @Expose
    public int response_code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("client_number")
    @Expose
    public String client_number;

    @SerializedName("merchant_number")
    @Expose
    public String merchant_number;

    public String getTransaction_reference() {
        return transaction_reference;
    }

    public void setTransaction_reference(String transaction_reference) {
        this.transaction_reference = transaction_reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClient_number() {
        return client_number;
    }

    public void setClient_number(String client_number) {
        this.client_number = client_number;
    }

    public String getMerchant_number() {
        return merchant_number;
    }

    public void setMerchant_number(String merchant_number) {
        this.merchant_number = merchant_number;
    }
}
