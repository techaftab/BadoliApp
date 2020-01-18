package com.webmobril.badoli.retrofit;

import com.webmobril.badoli.model.CountryResponse;
import com.webmobril.badoli.model.LoginResponse;
import com.webmobril.badoli.model.ProfileImageResponse;
import com.webmobril.badoli.model.QRResponse;
import com.webmobril.badoli.model.ResendOtpResponse;
import com.webmobril.badoli.model.SignupResponse;
import com.webmobril.badoli.model.VerifyOtpResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SignupResponse> getSignup(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("mobile") String mobile,
                                   @Field("password") String password,
                                   @Field("device_type") int device_type,
                                   @Field("device_token") String device_token,
                                   @Field("confirm_password") String confirm_password,
                                   @Field("country_id") int country_id,
                                   @Field("agree_terms") int agree_terms);


    @FormUrlEncoded
    @POST("verifyMobileOtp")
    Call<VerifyOtpResponse> verifyOtp(@Field("userid") int userid,
                                      @Field("otp") String otp,
                                      @Field("access_token") String access_token);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> getlogin(@Field("username") String username,
                                 @Field("password") String password,
                                 @Field("device_type") int device_type,
                                 @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("resendMobileOtp")
    Call<ResendOtpResponse> resendMobileOtp(@Field("userid") int userid,
                                            @Field("access_token") String access_token);

    @GET("countryList")
    Call<CountryResponse> getCountryList();

    @GET("currentWalletBalence")
    void getCurrentBalance(@Path("userid") String userid);

    @Multipart
    @POST("saveQrcode")
    Call<QRResponse> sendQrCode(@Part MultipartBody.Part file, @Part("userid") int id);

    @Multipart
    @POST("updateProfileImage")
    Call<ProfileImageResponse> saveProfileImage(@Part MultipartBody.Part file,@Part("userid") int id);
}
