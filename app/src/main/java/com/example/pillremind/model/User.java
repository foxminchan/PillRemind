package com.example.pillremind.model;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class User {
    private String email;
    private String name;
    private String phone;
    @Nullable
    private String password;
    private String birthday;
    @Nullable
    private String googleIdToken;
    @Nullable
    private String facebookIdToken;
    @Nullable
    private String appleIdToken;

    private boolean isTowFactorAuth = false;

    public User(String name, String phone, String birthday, String email, @Nullable String password, @Nullable String googleIdToken, @Nullable String facebookIdToken, @Nullable String appleIdToken, boolean isTowFactorAuth) {
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.googleIdToken = googleIdToken;
        this.facebookIdToken = facebookIdToken;
        this.appleIdToken = appleIdToken;
        this.isTowFactorAuth = isTowFactorAuth;
    }

    public User(String email, @Nullable String password) {
        this.email = email;
        this.password = password;
    }

    public User(){}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Nullable
    public String getGoogleIdToken() {
        return googleIdToken;
    }

    public void setGoogleIdToken(@Nullable String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }

    @Nullable
    public String getFacebookIdToken() {
        return facebookIdToken;
    }

    public void setFacebookIdToken(@Nullable String facebookIdToken) {
        this.facebookIdToken = facebookIdToken;
    }

    @Nullable
    public String getAppleIdToken() {
        return appleIdToken;
    }

    public void setAppleIdToken(@Nullable String appleIdToken) {
        this.appleIdToken = appleIdToken;
    }

    public boolean isTowFactorAuth() {
        return isTowFactorAuth;
    }

    public void setTwoFactorAuth(boolean isTowFactorAuth) {
        this.isTowFactorAuth = isTowFactorAuth;
    }

    public boolean isValidEmailSyntax(String email) {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPasswordSyntax(String password) {
        return !Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$").matcher(password).matches();
    }

    public boolean isValidPhoneSyntax(String phone) {
        return Pattern.compile("^\\d{10}$").matcher(phone).matches();
    }

}
