package com.b1906478.gojob.model;

import java.util.Date;

public class UserModel {
     String userIntroduce, username, userPosition, userUniversity, userAddress, userEmail, userPhone, userSkill, userCer, userInterest, userExperience, userAvatar,userWeb,userId;
     Number userGPA,Priority;
     Date userDOB;

    public Number getPriority() {
        return Priority;
    }

    public void setPriority(Number priority) {
        Priority = priority;
    }

    public UserModel() {
    }

    public String getUserWeb() {
        return userWeb;
    }

    public void setUserWeb(String userWeb) {
        this.userWeb = userWeb;
    }


    public void setUserGPA(Long userGPA) {
        this.userGPA = userGPA;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Number getUserGPA() {
        return userGPA;
    }

    public void setUserGPA(Number userGPA) {
        this.userGPA = userGPA;
    }

    public String getUserIntroduce() {
        return userIntroduce;
    }

    public void setUserIntroduce(String userIntroduce) {
        this.userIntroduce = userIntroduce;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserUniversity() {
        return userUniversity;
    }

    public void setUserUniversity(String userUniversity) {
        this.userUniversity = userUniversity;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(String userSkill) {
        this.userSkill = userSkill;
    }

    public String getUserCer() {
        return userCer;
    }

    public void setUserCer(String userCer) {
        this.userCer = userCer;
    }

    public String getUserInterest() {
        return userInterest;
    }

    public void setUserInterest(String userInterest) {
        this.userInterest = userInterest;
    }

    public String getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(String userExperience) {
        this.userExperience = userExperience;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Number getUsergpa() {
        return userGPA;
    }


    public Date getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(Date userDOB) {
        this.userDOB = userDOB;
    }
}