package com.app.badoli.model;

import java.util.List;

public class BannerModel {
    private boolean error;

    private List<LoginBanner> loginBanner;

    private List<OfferBanner> offerBanner;

    private MannagerBanner mannagerBanner;

    private StaffBanner staffBanner;

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setLoginBanner(List<LoginBanner> loginBanner){
        this.loginBanner = loginBanner;
    }
    public List<LoginBanner> getLoginBanner(){
        return this.loginBanner;
    }
    public void setOfferBanner(List<OfferBanner> offerBanner){
        this.offerBanner = offerBanner;
    }
    public List<OfferBanner> getOfferBanner(){
        return this.offerBanner;
    }
    public void setMannagerBanner(MannagerBanner mannagerBanner){
        this.mannagerBanner = mannagerBanner;
    }
    public MannagerBanner getMannagerBanner(){
        return this.mannagerBanner;
    }
    public void setStaffBanner(StaffBanner staffBanner){
        this.staffBanner = staffBanner;
    }
    public StaffBanner getStaffBanner(){
        return this.staffBanner;
    }

    public class StaffBanner
    {
        private int id;

        private String name;

        private int place_id;

        private int type;

        private String content;

        private String image;

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
        public void setPlace_id(int place_id){
            this.place_id = place_id;
        }
        public int getPlace_id(){
            return this.place_id;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
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
    public class OfferBanner
    {
        private int id;

        private String name;

        private int place_id;

        private int type;

        private String content;

        private String image;

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
        public void setPlace_id(int place_id){
            this.place_id = place_id;
        }
        public int getPlace_id(){
            return this.place_id;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
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


    public class MannagerBanner
    {
        private int id;

        private String name;

        private int place_id;

        private int type;

        private String content;

        private String image;

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
        public void setPlace_id(int place_id){
            this.place_id = place_id;
        }
        public int getPlace_id(){
            return this.place_id;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
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
    public class LoginBanner
    {
        private int id;

        private String name;

        private int place_id;

        private int type;

        private String content;

        private String image;

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
        public void setPlace_id(int place_id){
            this.place_id = place_id;
        }
        public int getPlace_id(){
            return this.place_id;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
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
