package com.HC.KB.KP.ownerapp.api;



import com.HC.KB.KP.ownerapp.pojo.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerInterface {
    String LOGIN_URL = "http://harishdevloper.infinityfreeapp.com/app/owner/";

    @FormUrlEncoded
    @POST("simplelogin.php")
    Call<User> getUserLogin(
            @Field("users_email") String users_email,
            @Field("users_password") String users_password
    );
   @FormUrlEncoded
   @POST("simpleregister.php")
   Call<User> getUserRegister(
           @Field("users_name") String users_name,
           @Field("users_email") String users_email,
           @Field("users_mobile") String users_mobile,
           @Field("users_password") String users_password
   );

}
