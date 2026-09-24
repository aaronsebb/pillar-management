
package com.java.pillargroup.pillarmanagement.users;
import com.java.pillargroup.pillarmanagement.exception.DatabaseException;
import com.java.pillargroup.pillarmanagement.exception.EntradaVaciaException;
import java.sql.SQLException;
import main.java.dev.alpha.alphalogin.security.jbcrypt.BCrypt;

public class AuthService {
    
    
        private AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    
    public boolean saveUser(String email, String firstName, String lastName, String passwordHash, String userId, int roleId) throws DatabaseException, EntradaVaciaException{

        if(email == null || !(email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))){

            throw new EntradaVaciaException("El correo esta vacio o la variable es nula");
            
        }
        
        if (firstName == null || firstName.isBlank()) {
    
            throw new EntradaVaciaException("El primer nombre esta vacio o la variable es nula");
            
        }

        if (lastName == null || lastName.isBlank()) {
            
            throw new EntradaVaciaException("El apellido esta vacio o la variable es nula");
    
        }

        if (passwordHash == null || passwordHash.isBlank()) {
    
            throw new EntradaVaciaException("La contrasena esta vacio o la variable es nula");
            
        }

        if (userId == null || userId.isBlank()) {
            
            throw new EntradaVaciaException("El id del usuario esta vacio o la variable es nula");
    
        }
        
        if(roleId == 0){
        
            throw new EntradaVaciaException("El rol no puede estar vacio");
        
        }
        
        email = email.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();
        passwordHash = passwordHash.trim();
        userId = userId.trim();
        
        passwordHash = BCrypt.hashpw(passwordHash,BCrypt.gensalt());
        
        
        User user = new User(userId, firstName, lastName, email, passwordHash, userId, roleId);
        
        try{
            
            return authRepository.saveUser(user);
            
        }catch(SQLException e){
        
        throw new DatabaseException("Excepcion en la base de datos");
        
        }
    
    
    }
   
    
    
    
    
    
}
