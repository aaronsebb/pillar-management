/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.residence;

import com.java.pillargroup.pillarmanagement.users.UserDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label bienvenidaLabel;

    private UserDto usuarioActual;

    public void setUsuarioActual(UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;
        if (bienvenidaLabel != null) {
            bienvenidaLabel.setText("Bienvenido, " + usuarioActual.getNombre());
        }
    }
}