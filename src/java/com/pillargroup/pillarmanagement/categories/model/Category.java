/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.com.pillargroup.pillarmanagement.categories.model;

/**
 *
 * @author informatica
 */
public class Category {
   String categoryId;
   String categoryName;
   String depiction;

    public Category(String categoryId, String categoryName, String depiction) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.depiction = depiction;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDepiction() {
        return depiction;
    }

    public void setDepiction(String depiction) {
        this.depiction = depiction;
    }
}
