/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.invoices.model;

import java.util.Date;

/**
 *
 * @author informatica
 */
public class Invoice {
   String invoiceId;
   Date invoiceDate;
   String userId;
   double interests;
   String residenceId;
   double payment;
   String paymentType;

    public Invoice(String invoiceId, Date invoiceDate, String userId, double interests, String residenceId, double payment, String paymentType) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.userId = userId;
        this.interests = interests;
        this.residenceId = residenceId;
        this.payment = payment;
        this.paymentType = paymentType;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getInterests() {
        return interests;
    }

    public void setInterests(double interests) {
        this.interests = interests;
    }

    public String getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(String residenceId) {
        this.residenceId = residenceId;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
   
}
