package com.example.invoice.util.jsonUtil.model;

import java.time.Instant;

public class InvoiceItemTax {
    private Long id;
    private Long taxAccount;
    private Float taxRate;
    private String taxCode;
    private Double taxAmount;
    private Instant created;
    private Instant updated;
    private InvoiceItems invoiceItems;
}
