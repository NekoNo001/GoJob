package com.b1906478.gojob.model;

import java.util.Date;

public class UserModel {
     String userIntroduce, username, userPosition, userUniversity, userAddress, userEmail, userPhone, userUnivebsity, userSkill, userCer, userInterest, userExperience, userAvatar;
     int userGPA;
     Date userDOB;

    public UserModel() {
    }

    public UserModel(String userIntroduce, String username, String userPosition, String userUniversity, String userAddress, String userEmail, String userPhone, String userUnivebsity, String userSkill, String userCer, String userInterest, String userExperience, String userAvatar, int userGPA, Date userDOB) {
        this.userIntroduce = userIntroduce;
        this.username = username;
        this.userPosition = userPosition;
        this.userUniversity = userUniversity;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userUnivebsity = userUnivebsity;
        this.userSkill = userSkill;
        this.userCer = userCer;
        this.userInterest = userInterest;
        this.userExperience = userExperience;
        this.userAvatar = userAvatar;
        this.userGPA = userGPA;
        this.userDOB = userDOB;
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

    public String getUserUnivebsity() {
        return userUnivebsity;
    }

    public void setUserUnivebsity(String userUnivebsity) {
        this.userUnivebsity = userUnivebsity;
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

    public int getUsergpa() {
        return userGPA;
    }

    public void setUsergpa(int usergpa) {
        this.userGPA = usergpa;
    }

    public Date getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(Date userDOB) {
        this.userDOB = userDOB;
    }
}