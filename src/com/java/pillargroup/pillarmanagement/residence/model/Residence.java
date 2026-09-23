package com.java.pillargroup.pillarmanagement.residence.model;

/**
 * @author informatica
 */
public class Residence {

    private String residenceId;
    private int categoryId;       
    private int statusId;         
    private String urlImage;
    private String residenceName;
    private String depiction;
    private double lumpSum;
    private double monthlyPayment;
    private String addressName;
    private String userName;


    public Residence(String residenceId, int categoryId, int statusId, String urlImage, 
                     String residenceName, String depiction, double lumpSum, double monthlyPayment, 
                     String addressName, String userName) {
        this.residenceId = residenceId;
        this.categoryId = categoryId;
        this.statusId = statusId;
        this.urlImage = urlImage;
        this.residenceName = residenceName;
        this.depiction = depiction;
        this.lumpSum = lumpSum;
        this.monthlyPayment = monthlyPayment;
        this.addressName = addressName;
        this.userName = userName;
    }

    public String getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(String residenceId) {
        this.residenceId = residenceId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getResidenceName() {
        return residenceName;
    }

    public void setResidenceName(String residenceName) {
        this.residenceName = residenceName;
    }

    public String getDepiction() {
        return depiction;
    }

    public void setDepiction(String depiction) {
        this.depiction = depiction;
    }

    public double getLumpSum() {
        return lumpSum;
    }

    public void setLumpSum(double lumpSum) {
        this.lumpSum = lumpSum;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public String getAddressId() {
        return addressName;
    }

    public void setAddressId(String addressName) {
        this.addressName = addressName;
    }

    public String getUserId() {
        return userName;
    }

    public void setUserId(String userName) {
        this.userName = userName;
    }
}