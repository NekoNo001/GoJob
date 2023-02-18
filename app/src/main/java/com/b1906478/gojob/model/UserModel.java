package com.b1906478.gojob.model;

import java.util.Date;

public class UserModel {
    public String password, introduce, name, position, university, address, email, phone, websity, skill, cer, interest, experience, ImageUrl;
    public int gpa;
    public Date DayOfBirth;

    public UserModel(String password, String introduce, String name, String position, String university, String address, String email, String phone, String websity, String skill, String cer, String interest, String experience, String imageUrl, int gpa, Date dayOfBirth) {
        this.password = password;
        this.introduce = introduce;
        this.name = name;
        this.position = position;
        this.university = university;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.websity = websity;
        this.skill = skill;
        this.cer = cer;
        this.interest = interest;
        this.experience = experience;
        ImageUrl = imageUrl;
        this.gpa = gpa;
        DayOfBirth = dayOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsity() {
        return websity;
    }

    public void setWebsity(String websity) {
        this.websity = websity;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getCer() {
        return cer;
    }

    public void setCer(String cer) {
        this.cer = cer;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getGpa() {
        return gpa;
    }

    public void setGpa(int gpa) {
        this.gpa = gpa;
    }

    public Date getDayOfBirth() {
        return DayOfBirth;
    }

    public void setDayOfBirth(Date dayOfBirth) {
        DayOfBirth = dayOfBirth;
    }

    public UserModel() {
    }
}