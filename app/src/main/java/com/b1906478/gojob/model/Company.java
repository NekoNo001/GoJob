package com.b1906478.gojob.model;

import android.net.Uri;

import java.util.List;

public class Company {
    String nameCompany, jobPosition, adress, salary, typeOfWork,gender, Location, jobDescription, candidateRequirements, benefit, Level;
    Number numberOfRecruits, workExperienceNeed;
    Uri ImageUrl;

    public Company() {
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCandidateRequirements() {
        return candidateRequirements;
    }

    public void setCandidateRequirements(String candidateRequirements) {
        this.candidateRequirements = candidateRequirements;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public Number getNumberOfRecruits() {
        return numberOfRecruits;
    }

    public void setNumberOfRecruits(Number numberOfRecruits) {
        this.numberOfRecruits = numberOfRecruits;
    }

    public Number getWorkExperienceNeed() {
        return workExperienceNeed;
    }

    public void setWorkExperienceNeed(Number workExperienceNeed) {
        this.workExperienceNeed = workExperienceNeed;
    }

    public Uri getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public Company(String nameCompany, String jobPosition, String adress, String salary, String typeOfWork, String gender, String location, String jobDescription, String candidateRequirements, String benefit, Number numberOfRecruits, Number workExperienceNeed, Uri imageUrl, String Level) {
        this.nameCompany = nameCompany;
        this.jobPosition = jobPosition;
        this.adress = adress;
        this.salary = salary;
        this.typeOfWork = typeOfWork;
        this.gender = gender;
        Location = location;
        this.jobDescription = jobDescription;
        this.candidateRequirements = candidateRequirements;
        this.benefit = benefit;
        this.numberOfRecruits = numberOfRecruits;
        this.workExperienceNeed = workExperienceNeed;
        ImageUrl = imageUrl;
        this.Level = Level;
    }
}
