package com.webmobril.badoli.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {

    private static RetrofitConnection connect;
    private ApiInterface clientService;
    private static final String BASE_URL = "https://www.webmobril.org/dev/badolipay/api/";


    public static synchronized RetrofitConnection getInstance() {
        if (connect == null) {
            connect = new RetrofitConnection();
        }
        return connect;
    }

    // service interface instance to call api

    public ApiInterface createService() {
        if (clientService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();//    logs HTTP request and response data.
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);//  set your desired log level
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            httpClient.readTimeout(1, TimeUnit.SECONDS)
//                    .connectTimeout(1, TimeUnit.SECONDS);
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            // add your other interceptors …
            httpClient.addInterceptor(logging); //  add logging as last interceptor

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            clientService = retrofit.create(ApiInterface.class);
        }
        return clientService;
    }
}
