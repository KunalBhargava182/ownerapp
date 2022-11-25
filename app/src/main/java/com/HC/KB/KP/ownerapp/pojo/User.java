package com.HC.KB.KP.ownerapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("users_id")
    @Expose
    private String usersId;
    @SerializedName("users_name")
    @Expose
    private String usersName;
    @SerializedName("users_email")
    @Expose
    private String usersEmail;
    @SerializedName("users_mobile")
    @Expose
    private String usersMobile;
    @SerializedName("users_password")
    @Expose
    private String usersPassword;
    @SerializedName("users_dor")
    @Expose
    private String usersDor;

    @SerializedName("status")
    @Expose
    private String status;

     @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getUsersEmail() {
        return usersEmail;
    }

    public void setUsersEmail(String usersEmail) {
        this.usersEmail = usersEmail;
    }

    public String getUsersMobile() {
        return usersMobile;
    }

    public void setUsersMobile(String usersMobile) {
        this.usersMobile = usersMobile;
    }

    public String getUsersPassword() {
        return usersPassword;
    }

    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    public String getUsersDor() {
        return usersDor;
    }

    public void setUsersDor(String usersDor) {
        this.usersDor = usersDor;
    }

}