/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.categories.repository;

import com.java.pillargroup.pillarmanagement.categories.model.Category;
import com.java.pillargroup.pillarmanagement.config.DataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository {

    public Category save(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_name, depiction) VALUES (?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDepiction());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setCategoryId(String.valueOf(generatedKeys.getInt(1)));
                }
            }

            return category;
        }
    }

    public Optional<Category> findById(String categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(categoryId));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCategory(rs));
            }
            return Optional.empty();
        }
    }

    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM categories";
        List<Category> list = new ArrayList<>();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }
        }
        return list;
    }

    public Category update(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_name = ?, depiction = ? WHERE category_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDepiction());
            ps.setInt(3, Integer.parseInt(category.getCategoryId()));

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró la categoría con ID: " + category.getCategoryId());
            }
            return category;
        }
    }

    public boolean deleteById(String categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(categoryId));
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsById(String categoryId) throws SQLException {
        String sql = "SELECT 1 FROM categories WHERE category_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(categoryId));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        return new Category(
                String.valueOf(rs.getInt("category_id")),
                rs.getString("category_name"),
                rs.getString("depiction")
        );
    }
}