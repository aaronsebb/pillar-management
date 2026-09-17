/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.com.pillargroup.pillarmanagement.residence.model;

/**
 *
 * @author informatica
 */
public class Residence {
    String residenceId;
    String categoryName;
    String statusName;
    String urlImage;
    String residenceName;
    String depiction;
    String lumpSum;
    String monthlyPayment;
    int category;
    String addressName;
    String userName;
    String residenceStatus;

    public Residence(String residenceId, String categoryName, String statusName, String urlImage, String residenceName, String depiction, String lumpSum, String monthlyPayment, int category, String addressName, String userName, String residenceStatus) {
        this.residenceId = residenceId;
        this.categoryName = categoryName;
        this.statusName = statusName;
        this.urlImage = urlImage;
        this.residenceName = residenceName;
        this.depiction = depiction;
        this.lumpSum = lumpSum;
        this.monthlyPayment = monthlyPayment;
        this.category = category;
        this.addressName = addressName;
        this.userName = userName;
        this.residenceStatus = residenceStatus;
    }

    public String getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(String residenceId) {
        this.residenceId = residenceId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getLumpSum() {
        return lumpSum;
    }

    public void setLumpSum(String lumpSum) {
        this.lumpSum = lumpSum;
    }

    public String getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(String monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getResidenceStatus() {
        return residenceStatus;
    }

    public void setResidenceStatus(String residenceStatus) {
        this.residenceStatus = residenceStatus;
    }
    
    
}
