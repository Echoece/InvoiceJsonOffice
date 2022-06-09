package com.example.invoice.util.jsonUtil.model;

import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
public class Invoice {
    private Long id;
    private String invoiceTitle;
    private String invoiceNo;
    private LocalDate invoiceDate ;
    private LocalDate invoiceDueDate;
    private String chequeNumber;
    private String notes;
    private Double taxAmount;
    private Long discountPercent;
    private Long discount;
    private Long totalItemDiscountAmount;
    private Long subtotal;
    private Double total;
    private Double dues;
    private String billingAddress;
    private String shippingAddress;
    private Long shippingProviderId;
    private String status;
    private Long companyId;
    private Long ownerId;
    private Long createdBy;
    private Instant created;
    private Instant updated;
    private Set<Transaction> transactions = new HashSet<>();
    private Set<InvoiceItems> invoiceItems = new HashSet<>();
    private Set<InvoiceNote> invoiceNotes = new HashSet<>();
    private Customer customer;
    private String job;
    private String metaInformation;

}
