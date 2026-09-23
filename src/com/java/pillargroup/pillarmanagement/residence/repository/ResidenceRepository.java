package com.java.pillargroup.pillarmanagement.residence.repository;

import com.java.pillargroup.pillarmanagement.config.DataBaseConnection;
import com.java.pillargroup.pillarmanagement.exception.RepositoryException;
import com.java.pillargroup.pillarmanagement.residence.model.Residence;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;

public class ResidenceRepository {

        public Optional<Residence> findById(String Id){

            String sql = "select * from residences where residence_id = ?";
            
            try(PreparedStatement pstm = DataBaseConnection.getConnection().prepareStatement(sql);){
                
                pstm.setString(1, Id);
                
                ResultSet rs = pstm.executeQuery();
                
                if(rs.next()){
                    Residence residence = new Residence(
                            rs.getString("residence_id"),
                            rs.getInt("category_id"),
                            rs.getInt("status_id"),
                            rs.getString("url_image"),
                            rs.getString("residence_name"),
                            rs.getString("deciption"),
                            rs.getDouble("lump_sum"),
                            rs.getDouble("monthly_payment"),
                            rs.getString("address_id"),
                            rs.getString("user_id")
                    );
                    
                    return Optional.of(residence);
                }
                
                return Optional.empty();    
            }catch (SQLException e){
                throw new RepositoryException("Error al intentar buscar la residencia.");
            }

        }
    
    public ObservableList<Residence> findAll(){
        
        String sql = "select * from residences";
        
        try(PreparedStatement pstm = DataBaseConnection.getConnection().prepareStatement(sql); ResultSet rs = pstm.executeQuery();){
            
            
            ObservableList<Residence> list = FXCollections.observableArrayList();
            
            while(rs.next()){
                
                list.add(new Residence(
                        rs.getString("residence_id"),
                        rs.getInt("category_id"),
                        rs.getInt("status_id"),
                        rs.getString("url_image"),
                        rs.getString("residence_name"),
                        rs.getString("deciption"),
                        rs.getDouble("lump_sum"),
                        rs.getDouble("monthly_payment"),
                        rs.getString("address_id"),
                        rs.getString("user_id")
                ));
                
            }
            
            return list;
        }catch (SQLException e){
            throw new RepositoryException("Error en la consulta");
            
        }
        
    }
    
    public boolean save(Residence residence){
        
        String sql = "insert into residences (residence_id, category_id, status_id, url_image, residence_name, deciption, lump_sum, monthly_payment, address_id, user_id) values(UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try(PreparedStatement pstm = DataBaseConnection.getConnection().prepareStatement(sql);){
            
            pstm.setInt(1, residence. getCategoryId());
            pstm.setInt(2, residence.getStatusId());
            pstm.setString(3, residence.getUrlImage());
            pstm.setString(4, residence.getResidenceName());
            pstm.setString(5, residence.getDepiction());
            pstm.setDouble(6, residence.getLumpSum());
            pstm.setDouble(7, residence.getMonthlyPayment());
            pstm.setString(8, residence.getAddressId());
            pstm.setString(9, residence.getUserId());
            
            return pstm.executeUpdate() > 0;
        }catch(SQLException e){
            throw new RepositoryException("Error en la consulta al crear una residencia");
        }
        
    }
    
    public Residence update(Residence residence) {
        String sql = "update residences set category_id = ?, status_id = ?, url_image = ?, "
                   + "residence_name = ?, deciption = ?, lump_sum = ?, monthly_payment = ?, "
                   + "address_id = ?, user_id = ? where residence_id = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, residence.getCategoryId());
            pstm.setInt(2, residence.getStatusId());
            pstm.setString(3, residence.getUrlImage());
            pstm.setString(4, residence.getResidenceName());
            pstm.setString(5, residence.getDepiction());
            pstm.setDouble(6, residence.getLumpSum());
            pstm.setDouble(7, residence.getMonthlyPayment());
            pstm.setString(8, residence.getAddressId());
            pstm.setString(9, residence.getUserId());
            pstm.setString(10, residence.getResidenceId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("No se pudo actualizar, la residencia no existe.");
            }

            return residence;
        } catch (SQLException e) {
            throw new RepositoryException("Error en la consulta al actualizar la residencia");
        }
    }

    public boolean deleteById(String residenceId) {
        String sql = "delete from residences where residence_id = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pstm.setString(1, residenceId);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Error en la consulta al eliminar la residencia");
        }
    }
    
    public boolean existsById(String residenceId) {
        String sql = "select 1 from residences where residence_id = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnection().prepareStatement(sql)) {
            pstm.setString(1, residenceId);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next(); // Retorna true si encontró al menos una coincidencia
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error al verificar existencia de la residencia");
        }
    }
    
}