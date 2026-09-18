package com.java.pillargroup.pillarmanagement.users;


import com.java.pillargroup.pillarmanagement.config.DataBaseConnection;
import com.java.pillargroup.pillarmanagement.users.User;
import com.java.pillargroup.pillarmanagement.users.UserDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthRepository{
    
    
    
    
    public boolean save(User user) throws SQLException{
    
    String sql = "Insert into users (first_name,last_name,email,password_hash,role_id) Values(?,?,?,?,?);";
    
      try(Connection conn = DataBaseConnection.getConnection();PreparedStatement prst = conn.prepareStatement(sql)){
        
    prst.setString(1,user.getFirstName());
    prst.setString(2, user.getLastName());
    prst.setString(3, user.getEmai());
    prst.setString(4, user.getEmai());
    prst.setString(5, user.getEmai());    
    
        return prst.execute();
    
    }
    }
    
    
    
    public UserDto findUserByEmail(String email) throws SQLException{
  
        String sql = "Select u.first_name,u.last_name,u.password_hash,r.role_name from users as u inner join roles as r on u.role_id = r.role_id where email = ?";
        
    try(Connection conn = DataBaseConnection.getConnection();PreparedStatement prst = conn.prepareStatement(sql)){
        
    prst.setString(1, email);
    
    try(ResultSet rs = prst.executeQuery()){
        
    if(rs.next()){
        
    return new UserDto(rs.getString("first_name"),rs.getString("last_name"),rs.getString("password_hash"),rs.getString("role_name"));
    
    }else{
        
    return null;
    }
    
        }
    }
    }
    
    public String findUserPasswordHashByEmail(String email) throws SQLException{
    
    String sql = "select password_hash from users where email = ?";
    
     try(Connection conn = DataBaseConnection.getConnection();PreparedStatement prst = conn.prepareStatement(sql)){
        
        prst.setString(1, email);
    
    try(ResultSet rs = prst.executeQuery()){
    
    if(rs.next()){
        
        return rs.getString("password_hash");
    
    }else{
        
        return null;
    
    }
    }
     }
    }
    
    
    
    
}
