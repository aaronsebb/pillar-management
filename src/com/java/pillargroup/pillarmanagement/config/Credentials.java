package com.java.pillargroup.pillarmanagement.config;

public class Credentials {
    
    //Cambiarlas a sus respectivas variables de entorno, reemplazar donde dice VARIABLE_DE_ENTONRO sin quitarle las comillas y depende la variable de entorno, quitar
    //el / de pillar_group
    
    public static final String URL_DATA_BASE = "jdbc:mysql://localhost:3306/pillar_group_in4bv";
    public static final String USER_DATA_BASE = "IN4BV";
    public static final String PASS_DATA_BASE = "%IndiVA4";
    
    private Credentials(){
        
    }
    
}
