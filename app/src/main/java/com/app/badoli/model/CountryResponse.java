package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {

    public boolean error;

    public String message;

    public List<CountryResult> result;

    public class CountryResult {

        public int id;

        public String sortname;

        public String name;

        public int phonecode;

        public int status;

        public String created_at;

        public String updated_at;

    }

}
