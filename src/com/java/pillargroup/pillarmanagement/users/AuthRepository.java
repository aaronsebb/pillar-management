package com.java.pillargroup.pillarmanagement.users;


import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.config.DataBaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthRepository{
    
    
    
    public void saveUserAddress(Address address,String userId) throws SQLException{
        
    String sql1 = "insert into addresses values(?,?,?,?,?,?,?)";
    String sql2 = "update users set address = ? where user_id = ?"; 
    
    try(Connection conn = DataBaseConnection.getConnection()){
        
        try(PreparedStatement prst = conn.prepareStatement(sql1);
              PreparedStatement prstAsignation = conn.prepareStatement(sql2)  
                ){
            
            conn.setAutoCommit(false);
            
        prst.setString(1, address.getAddressId());
        prst.setString(2, address.getCity());
        prst.setString(3, address.getDistrict()); 
        prst.setString(4, address.getAvenue());
        prst.setString(5, address.getStreet());
        prst.setString(6, address.getHouse());
        
        prst.executeUpdate();
        
        prstAsignation.setString(1, address.getAddressId());
        prstAsignation.setString(2, userId);
        
        prstAsignation.executeUpdate();
        
        conn.commit();<
        
        }catch(SQLException e){
        
        conn.rollback();
        
        }finally{
        
        conn.setAutoCommit(true);
        
        }
    }
        }

        
        
    
    
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
