/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.invoices.repository;

import com.java.pillargroup.pillarmanagement.invoices.model.Invoice;
import com.java.pillargroup.pillarmanagement.config.DataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InvoiceRepository {

    public Invoice save(Invoice invoice) throws SQLException {
        if (invoice.getInvoiceId() == null || invoice.getInvoiceId().trim().isEmpty()) {
            invoice.setInvoiceId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO invoices (invoice_id, invoice_date, user_id, interests, residence_id, payment, payment_type) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoice.getInvoiceId());
            ps.setTimestamp(2, new Timestamp(invoice.getInvoiceDate().getTime()));
            ps.setString(3, invoice.getUserId());
            ps.setDouble(4, invoice.getInterests());
            ps.setString(5, invoice.getResidenceId());
            ps.setDouble(6, invoice.getPayment());
            ps.setString(7, invoice.getPaymentType());

            ps.executeUpdate();
            return invoice;
        }
    }

    public Optional<Invoice> findById(String invoiceId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoiceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToInvoice(rs));
            }
            return Optional.empty();
        }
    }

    public List<Invoice> findAll() throws SQLException {
        String sql = "SELECT * FROM invoices";
        List<Invoice> list = new ArrayList<>();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToInvoice(rs));
            }
        }
        return list;
    }

    public Invoice update(Invoice invoice) throws SQLException {
        String sql = "UPDATE invoices SET invoice_date = ?, user_id = ?, interests = ?, "
                   + "residence_id = ?, payment = ?, payment_type = ? WHERE invoice_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(invoice.getInvoiceDate().getTime()));
            ps.setString(2, invoice.getUserId());
            ps.setDouble(3, invoice.getInterests());
            ps.setString(4, invoice.getResidenceId());
            ps.setDouble(5, invoice.getPayment());
            ps.setString(6, invoice.getPaymentType());
            ps.setString(7, invoice.getInvoiceId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró la factura con ID: " + invoice.getInvoiceId());
            }
            return invoice;
        }
    }

    public boolean deleteById(String invoiceId) throws SQLException {
        String sql = "DELETE FROM invoices WHERE invoice_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoiceId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsById(String invoiceId) throws SQLException {
        String sql = "SELECT 1 FROM invoices WHERE invoice_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // ========== MAPEO ==========
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        return new Invoice(
                rs.getString("invoice_id"),
                rs.getTimestamp("invoice_date"),          // Timestamp → Date
                rs.getString("user_id"),
                rs.getDouble("interests"),
                rs.getString("residence_id"),
                rs.getDouble("payment"),
                rs.getString("payment_type")
        );
    }
}