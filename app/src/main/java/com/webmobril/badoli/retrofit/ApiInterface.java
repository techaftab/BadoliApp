package com.webmobril.badoli.retrofit;

import com.webmobril.badoli.model.AirtelResponse;
import com.webmobril.badoli.model.BalanceResponse;
import com.webmobril.badoli.model.CountryResponse;
import com.webmobril.badoli.model.LoginResponse;
import com.webmobril.badoli.model.ProfileImageResponse;
import com.webmobril.badoli.model.QRResponse;
import com.webmobril.badoli.model.ReferenceResponse;
import com.webmobril.badoli.model.ResendOtpResponse;
import com.webmobril.badoli.model.SignupResponse;
import com.webmobril.badoli.model.VerifyOtpResponse;
import com.webmobril.badoli.model.WalletTransfer;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<BalanceResponse> getCurrentBalance(@Query("userid") String userid);

    @Multipart
    @POST("saveQrcode")
    Call<QRResponse> sendQrCode(@Part MultipartBody.Part file, @Part("userid") int id);

    @Multipart
    @POST("updateProfileImage")
    Call<ProfileImageResponse> saveProfileImage(@Part MultipartBody.Part file,@Part("userid") int id);

    @FormUrlEncoded
    @POST("getPaymentRef")
    Call<ReferenceResponse> getReference(@Field("user_id") String id,@Field("tel_client") String mobile,
                                         @Field("amount") String amount,@Field("access_token") String auth_token);

    @FormUrlEncoded
    @POST("mypvitapi.kk")
    Call<AirtelResponse> goAirtel(@Field("tel_client") String mobile,@Field("montant") String amount,@Field("ref")String referenceNo,
                                  @Field("tel_marchand") String telMerchand,@Field("token") String token);

    @FormUrlEncoded
    @POST("payByWallet")
    Call<WalletTransfer> transferMobile(@Field("amount") String amount,@Field("tel_client") String receiver_id,
                                        @Field("sender_id") String senderId);
}
