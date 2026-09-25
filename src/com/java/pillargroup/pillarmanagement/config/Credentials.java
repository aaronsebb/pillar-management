package com.java.pillargroup.pillarmanagement.config;

public class Credentials {
    
    //Cambiarlas a sus respectivas variables de entorno, reemplazar donde dice VARIABLE_DE_ENTONRO sin quitarle las comillas y depende la variable de entorno, quitar
    //el / de pillar_group
    
    public static final String URL_DATA_BASE = System.getenv("URL_MYSQL_DB") + "/pillar_group_in4bv";
    public static final String USER_DATA_BASE = System.getenv("USER_MYSQL_DB");
    public static final String PASS_DATA_BASE = System.getenv("PASS_MYSQL_DB");
    
    private Credentials(){
        
    }
    
}
