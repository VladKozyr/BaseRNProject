package com.takeuseatcalls.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CallContact {
    @NonNull
    private String phone;
    @Nullable
    private String description;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Nullable
    private String photoUrl;
    @NonNull
    private String birthday;
    @Nullable
    private List<String> phoneNumbers;

    public CallContact() {
        this.phone = "";
        this.description = "";
        this.firstName = "";
        this.lastName = "";
        this.photoUrl = "";
        this.birthday = "";
    }

    public CallContact(@NonNull String phone, @Nullable String description, @NonNull String firstName, @NonNull String lastName, @Nullable String photoUrl, @NonNull String birthday) {
        this.phone = phone;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.birthday = birthday;
    }

    @Nullable
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(@Nullable List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public void setPhotoUrl(@Nullable String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setBirthday(@NonNull String birthday) {
        this.birthday = birthday;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    @NonNull
    public String getBirthday() {
        return birthday;
    }
}
