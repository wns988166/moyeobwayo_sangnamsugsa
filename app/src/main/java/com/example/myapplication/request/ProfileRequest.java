package com.example.myapplication.request;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {
    @SerializedName("userId")
    private String userId;

    // option1 - mbti
    @SerializedName("Embti")
    private int Embti;
    @SerializedName("Smbti")
    private int Smbti;
    @SerializedName("Tmbti")
    private int Tmbti;
    @SerializedName("Jmbti")
    private int Jmbti;

    // option2
    @SerializedName("firstLesson")
    private int firstLesson;
    @SerializedName("smoke")
    private int smoke;
    @SerializedName("sleepHabit")
    private int sleepHabit;
    @SerializedName("grade")
    private int grade;
    @SerializedName("shareNeeds")
    private int shareNeeds;

    // option3
    @SerializedName("inComm")
    private int inComm;
    @SerializedName("heatSens")
    private int heatSens;
    @SerializedName("coldSens")
    private int coldSens;
    @SerializedName("drinkFreq")
    private int drinkFreq;

    // option4
    @SerializedName("cleanliness")
    private int cleanliness;
    @SerializedName("noiseSens")
    private int noiseSens;
    @SerializedName("sleepSche")
    private int sleepSche;
    @SerializedName("upSche")
    private int upSche;

    // Constructor
    public ProfileRequest(String userId, int Embti, int Smbti, int Tmbti, int Jmbti,
                          int firstLesson, int smoke, int sleepHabit, int grade, int shareNeeds,
                          int inComm, int heatSens, int coldSens, int drinkFreq,
                          int cleanliness, int noiseSens, int sleepSche, int upSche) {
        this.userId = userId;
        this.Embti = Embti;
        this.Smbti = Smbti;
        this.Tmbti = Tmbti;
        this.Jmbti = Jmbti;
        this.firstLesson = firstLesson;
        this.smoke = smoke;
        this.sleepHabit = sleepHabit;
        this.grade = grade;
        this.shareNeeds = shareNeeds;
        this.inComm = inComm;
        this.heatSens = heatSens;
        this.coldSens = coldSens;
        this.drinkFreq = drinkFreq;
        this.cleanliness = cleanliness;
        this.noiseSens = noiseSens;
        this.sleepSche = sleepSche;
        this.upSche = upSche;
    }

    // Getters and setters for all fields
    // ...
}
