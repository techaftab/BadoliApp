package com.app.badoli.repositories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistory {
    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("wallethistory")
    @Expose
    public List<WalletHistory> wallethistory;


    public class WalletHistory {

        @SerializedName("transction_date")
        @Expose
        public String transction_date;

        @SerializedName("wallethistory")
        @Expose
        public List<WalletHistoryInner> wallethistory;


        public class WalletHistoryInner {

            @SerializedName("id")
            @Expose
            public String id;

            @SerializedName("wallet_head_id")
            @Expose
            public String wallet_head_id;

            @SerializedName("userid")
            @Expose
            public String userid;

            @SerializedName("amount")
            @Expose
            public String amount;

            @SerializedName("type")
            @Expose
            public String type;

            @SerializedName("message")
            @Expose
            public String message;

            @SerializedName("created_at")
            @Expose
            public String created_at;

            @SerializedName("updated_at")
            @Expose
            public String updated_at;

        }
    }
}
