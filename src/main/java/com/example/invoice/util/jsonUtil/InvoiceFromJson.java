package com.example.invoice.util.jsonUtil;

import com.example.invoice.util.jsonUtil.model.Customer;
import com.example.invoice.util.jsonUtil.model.Invoice;
import com.example.invoice.util.jsonUtil.model.InvoiceItems;
import com.example.invoice.util.jsonUtil.model.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class InvoiceFromJson {
    private Invoice invoice ;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");

    public Invoice getInvoice(File file) throws FileNotFoundException{
        JSONObject jsonDocument = (JSONObject) JSONValue.parse(new FileReader(file));
        invoice =  new Invoice();
        setInvoiceFromJson(jsonDocument);

        invoice.getInvoiceItems().forEach(System.out::println);
        System.out.println(invoice.getInvoiceItems().size());
        return this.invoice;
    }

    private void setInvoiceFromJson(JSONObject doc) {
        if(doc.containsKey("id"))
            this.invoice.setId((Long) doc.get("id"));

        if(doc.containsKey("invoice_title"))
            this.invoice.setInvoiceTitle((String) doc.get("invoice_title"));

        if(doc.containsKey("invoice_no"))
            this.invoice.setInvoiceNo((String) doc.get("invoice_no"));

        if(doc.containsKey("invoice_date"))
            this.invoice.setInvoiceDate( LocalDate.parse( (String)doc.get("invoice_date")));

        if(doc.containsKey("invoice_due_date"))
            this.invoice.setInvoiceDueDate( LocalDate.parse( (String)doc.get("invoice_due_date")));

        if(doc.containsKey("cheque_number"))
            this.invoice.setChequeNumber((String) doc.get("cheque_number"));

        if(doc.containsKey("notes"))
            this.invoice.setNotes((String) doc.get("notes"));

        if(doc.containsKey("tax_amount"))
            this.invoice.setTaxAmount((Double) doc.get("tax_amount"));

        if(doc.containsKey("discount_percent"))
            this.invoice.setDiscountPercent((Long) doc.get("discount_percent"));

        if(doc.containsKey("discount"))
            this.invoice.setDiscount((Long) doc.get("discount"));

        if(doc.containsKey("total_item_discount_amount"))
            this.invoice.setTotalItemDiscountAmount((Long) doc.get("total_item_discount_amount"));

        if(doc.containsKey("subtotal"))
            this.invoice.setSubtotal((Long) doc.get("subtotal"));

        if(doc.containsKey("total"))
            this.invoice.setTotal((Double) doc.get("total"));

        if(doc.containsKey("dues"))
            this.invoice.setDues((Double) doc.get("dues"));

        if(doc.containsKey("billing_address"))
            this.invoice.setBillingAddress((String) doc.get("billing_address"));

        if(doc.containsKey("shipping_address"))
            this.invoice.setShippingAddress((String) doc.get("shipping_address"));

        if(doc.containsKey("shipping_provider_id"))
            this.invoice.setShippingProviderId((Long) doc.get("shipping_provider_id"));

        if(doc.containsKey("status"))
            this.invoice.setStatus((String) doc.get("status"));

        if(doc.containsKey("company_id"))
            this.invoice.setCompanyId((Long) doc.get("company_id"));

        if(doc.containsKey("owner_id"))
            this.invoice.setOwnerId((Long) doc.get("owner_id"));

        if(doc.containsKey("created_by"))
            this.invoice.setCreatedBy((Long) doc.get("created_by"));

        if(doc.containsKey("created"))
            this.invoice.setCreated(Instant.parse((String) doc.get("created")));

        if(doc.containsKey("updated"))
            this.invoice.setUpdated(Instant.parse((String) doc.get("updated")));

        this.invoice.setTransactions(
                Set.of(new Transaction(getJsonObjectFromDocument(doc,"transactions")))
        );

        this.invoice.setInvoiceItems(getInvoiceItems(doc));
        this.invoice.setCustomer(new Customer(getJsonObjectFromDocument(doc,"customer")));

    }

    // getting the set of InvoiceItems from json, TODO: make generic to get JSONArray
    private Set<InvoiceItems> getInvoiceItems(JSONObject doc){
        Set<InvoiceItems> items = new HashSet<>();
        JSONArray invoiceItemArray =(JSONArray) doc.get("invoice_items");
        Iterator itr = invoiceItemArray.iterator();

        while (itr.hasNext()){
            JSONObject simpleObject = (JSONObject)itr.next();
            items.add(new InvoiceItems(simpleObject));
        }
        return items;
    }

    // getting an object from the json
    private JSONObject getJsonObjectFromDocument(JSONObject doc, String key) {
        if(doc.containsKey(key)) {
            Object simpleObject = doc.get(key);
            if(simpleObject instanceof JSONObject) {
                return (JSONObject)simpleObject;
            }
        }
        return null;
    }

}
