/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.invoices.service;

import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.invoices.model.Invoice;
import com.java.pillargroup.pillarmanagement.invoices.repository.InvoiceRepository;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService() {
        this.invoiceRepository = new InvoiceRepository();
    }

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice registrarPago(String userId, String residenceId, double payment,
                                  double interests, String paymentType) throws ServiceException {

        if (userId == null || userId.isBlank() || residenceId == null || residenceId.isBlank()) {
            throw new ServiceException("Falta el usuario o la residencia asociada al pago.");
        }
        if (payment <= 0) {
            throw new ServiceException("El monto del pago debe ser mayor a cero.");
        }
        if (paymentType == null || paymentType.isBlank()) {
            throw new ServiceException("Debes indicar el tipo de pago.");
        }

        Invoice invoice = new Invoice(null, new Date(), userId, interests, residenceId, payment, paymentType);

        try {
            return invoiceRepository.save(invoice);
        } catch (SQLException e) {
            throw new ServiceException("No se pudo registrar la factura.");
        }
    }

    public List<Invoice> obtenerFacturasDeUsuario(String userId) throws ServiceException {
        try {
            return invoiceRepository.findAll().stream()
                    .filter(invoice -> userId.equals(invoice.getUserId()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new ServiceException("No se pudieron obtener las facturas del usuario.");
        }
    }

    public Invoice obtenerPorId(String invoiceId) throws ServiceException {
        try {
            return invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new ServiceException("No existe una factura con ese ID."));
        } catch (SQLException e) {
            throw new ServiceException("No se pudo buscar la factura.");
        }
    }
}