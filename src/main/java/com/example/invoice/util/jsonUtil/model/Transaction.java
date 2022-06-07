package com.example.invoice.util.jsonUtil.model;

import org.json.simple.JSONObject;

import java.time.Instant;
import java.time.LocalDate;

public class Transaction {
    private Long id;
    private Integer companyId;
    private Long fromAccount;
    private Long toAccount;
    private LocalDate transactionDate;
    private String transactionDescription;
    private String transactionType;
    private Double amount;
    private String taxes;
    private Integer journalCount;
    private Integer statementId;
    private String transactionComments;
    private Integer ownerId;
    private Instant created;
    private Instant updated;
    private String notes;
    private String chequeNumber;

    public Transaction(JSONObject doc){

    }
}
