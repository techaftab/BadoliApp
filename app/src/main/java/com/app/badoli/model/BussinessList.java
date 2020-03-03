package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BussinessList {

    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("result")
    @Expose
    private List<Result> result;

    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
        return this.result;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class Result
    {
        private int id;

        private String name;

        private int status;

        private String created_at;

        private String updated_at;

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
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
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
