package com.example.invoice.util.jsonUtil.model;

import java.time.Instant;

public class InvoiceNote {
    private Long id;
    private String note;
    private Boolean isPrivate;
    private Instant created;
    private Instant updated;
    private Invoice invoice;
    private Long invoiceId;
}
