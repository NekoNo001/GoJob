package com.b1906478.gojob.model;

import android.net.Uri;

import java.sql.Date;
import java.sql.Timestamp;

public class Company {
    String companyName, companyJobPosition, companyAdress, companySalary, companyTypeOfWork, companyGender, companyJobLocation, companyJobDescription, companyCandidateRequirements, companyBenefit, companyLevel, companyCity, jobId;
    Number companyNumberOfRecruits, companyWorkExperience;
    Uri companyAvatar;
    Date dateStart;

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Company() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyJobPosition() {
        return companyJobPosition;
    }

    public void setCompanyJobPosition(String companyJobPosition) {
        this.companyJobPosition = companyJobPosition;
    }

    public String getCompanyAdress() {
        return companyAdress;
    }

    public void setCompanyAdress(String companyAdress) {
        this.companyAdress = companyAdress;
    }

    public String getCompanySalary() {
        return companySalary;
    }

    public void setCompanySalary(String companySalary) {
        this.companySalary = companySalary;
    }

    public String getCompanyTypeOfWork() {
        return companyTypeOfWork;
    }

    public void setCompanyTypeOfWork(String companyTypeOfWork) {
        this.companyTypeOfWork = companyTypeOfWork;
    }

    public String getCompanyGender() {
        return companyGender;
    }

    public void setCompanyGender(String companyGender) {
        this.companyGender = companyGender;
    }

    public String getCompanyJobLocation() {
        return companyJobLocation;
    }

    public void setCompanyJobLocation(String companyJobLocation) {
        this.companyJobLocation = companyJobLocation;
    }

    public String getCompanyJobDescription() {
        return companyJobDescription;
    }

    public void setCompanyJobDescription(String companyJobDescription) {
        this.companyJobDescription = companyJobDescription;
    }

    public String getCompanyCandidateRequirements() {
        return companyCandidateRequirements;
    }

    public void setCompanyCandidateRequirements(String companyCandidateRequirements) {
        this.companyCandidateRequirements = companyCandidateRequirements;
    }

    public String getCompanyBenefit() {
        return companyBenefit;
    }

    public void setCompanyBenefit(String companyBenefit) {
        this.companyBenefit = companyBenefit;
    }

    public String getCompanyLevel() {
        return companyLevel;
    }

    public void setCompanyLevel(String companyLevel) {
        this.companyLevel = companyLevel;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Number getCompanyNumberOfRecruits() {
        return companyNumberOfRecruits;
    }

    public void setCompanyNumberOfRecruits(Number companyNumberOfRecruits) {
        this.companyNumberOfRecruits = companyNumberOfRecruits;
    }

    public Number getCompanyWorkExperience() {
        return companyWorkExperience;
    }

    public void setCompanyWorkExperience(Number companyWorkExperience) {
        this.companyWorkExperience = companyWorkExperience;
    }

    public Uri getCompanyAvatar() {
        return companyAvatar;
    }

    public void setCompanyAvatar(Uri companyAvatar) {
        this.companyAvatar = companyAvatar;
    }

    public Company(String companyName, String companyJobPosition, String companyAdress, String companySalary, String companyTypeOfWork, String companyGender, String companyJobLocation, String companyJobDescription, String companyCandidateRequirements, String companyBenefit, String companyLevel, String companyCity, String jobId, Number companyNumberOfRecruits, Number companyWorkExperience, Uri companyAvatar) {
        this.companyName = companyName;
        this.companyJobPosition = companyJobPosition;
        this.companyAdress = companyAdress;
        this.companySalary = companySalary;
        this.companyTypeOfWork = companyTypeOfWork;
        this.companyGender = companyGender;
        this.companyJobLocation = companyJobLocation;
        this.companyJobDescription = companyJobDescription;
        this.companyCandidateRequirements = companyCandidateRequirements;
        this.companyBenefit = companyBenefit;
        this.companyLevel = companyLevel;
        this.companyCity = companyCity;
        this.jobId = jobId;
        this.companyNumberOfRecruits = companyNumberOfRecruits;
        this.companyWorkExperience = companyWorkExperience;
        this.companyAvatar = companyAvatar;
    }

}
