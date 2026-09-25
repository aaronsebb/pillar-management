package com.java.pillargroup.pillarmanagement.users.respository;

import com.java.pillargroup.pillarmanagement.users.model.User;
import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.config.DataBaseConnection;
import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
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
        
        conn.commit();
        
        }catch(SQLException e){
        
        conn.rollback();
        
        }finally{
        
        conn.setAutoCommit(true);
        
        }
    }
        }
    
    public boolean saveUser(User user) throws SQLException{
    
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

        String sql = "Select u.user_id,u.first_name,u.last_name,u.email,u.password_hash,r.role_name from users as u inner join roles as r on u.role_id = r.role_id where u.email = ?";

        try(Connection conn = DataBaseConnection.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    UserDto dto = new UserDto(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("password_hash"));
                    dto.setUserId(rs.getString("user_id"));
                    dto.setRole(rs.getString("role_name"));
                    return dto;

                }else{

                    return null;
                }
            }
        }
    }
    
    public String findUserPasswordHashByEmail(String email) throws SQLException{
    
    String sql = "select password_hash from users where email = ?";
    
     try(Connection conn = DataBaseConnection.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
        
        ps.setString(1, email);
    
    try(ResultSet rs = ps.executeQuery()){
    
    if(rs.next()){
        
        return rs.getString("password_hash");
    
    }else{
        
        return null;
        
            }
        }
        }
    }
    
    public boolean save(User user) throws SQLException {

    String sql = "Insert into users (first_name,last_name,email,password_hash,role_id) Values(?,?,?,?,?);";

    try (Connection conn = DataBaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getEmai());
        ps.setString(4, user.getPassword_hash());
        ps.setInt(5, user.getRoleId());

        return ps.executeUpdate() > 0;
    }
}
    
    public boolean save(User user, String addressId) throws SQLException {

        String sql = "Insert into users (first_name,last_name,email,password_hash,address_id,role_id) "
                + "Values(?,?,?,?,?,(select role_id from roles where role_name = ?));";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmai());
            ps.setString(4, user.getPassword_hash());
            ps.setString(5, addressId);
            ps.setString(6, "Usuario");

            return ps.executeUpdate() > 0;
        }
    }
    
    public String findFullNameByUserId(String userId) throws SQLException {
        String sql = "select first_name, last_name from users where user_id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("first_name") + " " + rs.getString("last_name") : null;
            }
        }
    }

    public String findAddressIdByUserId(String userId) throws SQLException {
        String sql = "select address_id from users where user_id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("address_id") : null;
            }
        }
    }

    public boolean updateUserAddress(String userId, String addressId) throws SQLException {
        String sql = "update users set address_id = ? where user_id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressId);
            ps.setString(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

}