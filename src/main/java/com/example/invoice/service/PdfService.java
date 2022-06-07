package com.example.invoice.service;


import com.example.invoice.util.jsonUtil.InvoiceFromJson;
import com.example.invoice.util.jsonUtil.model.Invoice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
@AllArgsConstructor
public class PdfService {
    private InvoiceFromJson invoiceFromJson;

    public void printInvoice() throws FileNotFoundException {
        Invoice invoice = invoiceFromJson.getInvoice(new File("src/main/resources/json/Invoice.json"));
    }

}
