package com.app.badoli.retrofit;

import com.app.badoli.model.AirtelResponse;
import com.app.badoli.model.BalanceResponse;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.LoginResponse;
import com.app.badoli.model.ProfileImageResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ReferenceResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.SignupResponse;
import com.app.badoli.model.VerifyOtpResponse;
import com.app.badoli.model.WalletTransfer;
import com.app.badoli.model.ChangePasswordModel;
import com.webmobril.badoli.model.BussinessList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
                                   @Field("agree_terms") int agree_terms,
                                   @Field("roles_id") String roleId);

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
    Call<WalletTransfer> transferMobile(@Field("amount") String amount,@Field("receiver_id") String receiver_id,
                                        @Field("sender_id") String senderId);


    @FormUrlEncoded
    @POST("api_forgot_password")
    Call<ChangePasswordModel> changePassword(@Field("mobile") String mobile,@Field("new_password") String newPassword,
                                             @Field("confirm_password") String confirmPassword,@Field("roles_id") String roleId);

    @GET("activityList")
    Call<BussinessList> getBussinessList();
}
