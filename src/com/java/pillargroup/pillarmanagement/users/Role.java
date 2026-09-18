<<<<<<<< HEAD:src/java/com/pillargroup/pillarmanagement/users/model/Role.java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.com.pillargroup.pillarmanagement.users.model;
========
>>>>>>>> 50a2f0de92e44f1b08df38a3a636f288093e2239:src/com/java/pillargroup/pillarmanagement/users/Role.java

package com.java.pillargroup.pillarmanagement.users;


public class Role {
    String roleId;
    String roleName;

    public Role(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
