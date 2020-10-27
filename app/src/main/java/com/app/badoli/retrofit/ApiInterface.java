package com.app.badoli.retrofit;

import com.app.badoli.model.AirtelResponse;
import com.app.badoli.model.BalanceResponse;
import com.app.badoli.model.BannerModel;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.CreateStaff;
import com.app.badoli.model.CustomerUserProfile;
import com.app.badoli.model.LoginResponse;
import com.app.badoli.model.ProfileImageResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ReferenceResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.ResetPassword;
import com.app.badoli.model.SignupResponse;
import com.app.badoli.model.StaffList;
import com.app.badoli.model.UserProfile;
import com.app.badoli.model.UserSignUpResponse;
import com.app.badoli.model.VerifyOtpResponse;
import com.app.badoli.model.WalletTransfer;
import com.app.badoli.model.ChangePasswordModel;
import com.app.badoli.model.BussinessList;
import com.app.badoli.repositories.TransactionHistory;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    Call<UserSignUpResponse> getSignup(@Field("name") String name,
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
    @POST("signup")
    Call<SignupResponse> signUpProfessional(@Field("device_type") int deviceType, @Field("device_token") String device_token,
                                            @Field("company_name") String companyName, @Field("company_address") String companyAddress,
                                            @Field("merchant_activity_id") String businessId, @Field("country_id") String countryId,
                                            @Field("mobile") String phone, @Field("email") String email, @Field("password") String password,
                                            @Field("confirm_password") String confirm_password, @Field("name") String nameDirector,
                                            @Field("agree_terms") int agreeTerms,@Field("roles_id") String roleId);

    @FormUrlEncoded
    @POST("verifyMobileOtp")
    Call<VerifyOtpResponse> verifyOtp(@Field("userid") String userid,
                                      @Field("otp") String otp);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> getlogin(@Field("username") String username,
                                 @Field("password") String password,
                                 @Field("device_type") int device_type,
                                 @Field("device_token") String device_token,
                                 @Field("roles_id") String roleId);

    @FormUrlEncoded
    @POST("resendMobileOtp")
    Call<ResendOtpResponse> resendMobileOtp(@Field("userid") String userid);

    @GET("countryList")
    Call<CountryResponse> getCountryList();

    @GET("currentWalletBalence")
    Call<BalanceResponse> getCurrentBalance(@Query("userid") String userid);

    @Multipart
    @POST("saveQrcode")
    Call<QRResponse> sendQrCode(@Part MultipartBody.Part file, @Part("userid") RequestBody id);

    @Multipart
    @POST("updateProfileImage")
    Call<ProfileImageResponse> saveProfileImage(@Part MultipartBody.Part file,@Part("userid") int id);

    @FormUrlEncoded
    @POST("getPaymentRef")
    Call<ReferenceResponse> getReference(@Field("user_id") String id, @Field("tel_client") String mobile,
                                         @Field("amount") String amount, @Field("access_token") String auth_token,
                                         @Field("request_for") String request_for);

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

    @FormUrlEncoded
    @POST("api_change_password")
    Call<ResetPassword> resetPassword(@Field("user_id") String userId,@Field("old_password") String oldPwd,
                                      @Field("new_password") String newPassword,@Field("confirm_password") String confirmPwd);

    @GET("transactionHistory")
    Call<TransactionHistory> getHistory(@Query("userid") String id,@Query("type") String type);

    @GET("getBanner")
    Call<BannerModel> getBanner();

    @FormUrlEncoded
    @POST("staffList")
    Call<StaffList> getStaffList(@Field("user_id") String id);

    @FormUrlEncoded
    @POST("createStaff")
    Call<CreateStaff> createStaff(@Field("user_id") String id,@Field("staff_name") String name);

    @GET("getProfile")
    Call<UserProfile> getProfile(@Query("user_id") String id,@Query("roles_id") String userType);

    @GET("getProfile")
    Call<CustomerUserProfile> getUserProfile(@Query("user_id") String id,@Query("roles_id") String userType);

    @FormUrlEncoded
    @POST("contact-us")
    Call<ResetPassword> contactUs(@Field("userid") String id,@Field("subject") String subject,@Field("message") String message);

    @FormUrlEncoded
    @POST("updateProfile")
    Call<ResetPassword> updateProfile(@Field("userid") String id,@Field("roles_id") String userType,
                                      @Field("name") String name,@Field("email") String email,
                                      @Field("dob") String dob,@Field("gender") String gender);

    @FormUrlEncoded
    @POST("deleteStaff")
    Call<CreateStaff> deleteStaffCode(@Field("staff_id") String staffId);

    @FormUrlEncoded
    @POST("updateProfile")
    Call<ResetPassword> updateMerchantProfile(@Field("userid") String id,@Field("roles_id")  String userType,
                                              @Field("company_name") String name,@Field("company_address") String address);

    @FormUrlEncoded
    @POST("reGenerateAgentCode")
    Call<CreateStaff> renewCode(@Field("user_id") String id,@Field("staff_id") String staffId);
}
