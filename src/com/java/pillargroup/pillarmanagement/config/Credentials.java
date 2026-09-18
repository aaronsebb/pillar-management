package com.java.pillargroup.pillarmanagement.config;

public class Credentials {
    
    //Cambiarlas a sus respectivas variables de entorno, reemplazar donde dice VARIABLE_DE_ENTONRO sin quitarle las comillas y depende la variable de entorno, quitar
    //el / de pillar_group
    
    public static final String URL_DATA_BASE = System.getenv("VARIABLE_DE_ENTORNO") + "/pillar_group_in4bv";
    public static final String USER_DATA_BASE = System.getenv("VARIABLE_DE_ENTORNO");
    public static final String PASS_DATA_BASE = System.getenv("VARIABLE_DE_ENTORNO");
    
    private Credentials(){
        
    }
    
}
