package com.example.invoice.util.jsonUtil.model;

import lombok.Data;
import org.json.simple.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class InvoiceItems {
    private Long id;
    private Long companyId;
    private Long incomeAccountId;
    private Long productId;
    private String productName;
    private String productDescription;
    private Long quantity;
    private Long unitPrice;
    private String discountType;
    private Double discountAmount;
    private Long totalDiscountAmount;
    private Long itemSubtotal;
    private Double itemTotal;
    private Double totalTaxAmount;
    private Long ownerId;
    private Long createdBy;

    private Instant created;
    private Set<InvoiceItemTax> taxedInvoiceItems = new HashSet<>();
    private Instant updated;
    private Invoice invoice;
    private List<String> taxCodes = new ArrayList<>();

    public InvoiceItems(){
        productName = "";
    }
    public InvoiceItems(JSONObject obj){
        if(obj.containsKey("id"))
            this.setId((Long) obj.get("id"));

        if(obj.containsKey("company_id"))
            this.setCompanyId((Long) obj.get("company_id"));

        if(obj.containsKey("income_account_id"))
            this.setIncomeAccountId((Long) obj.get("income_account_id"));

        if(obj.containsKey("product_id"))
            this.setProductId((Long) obj.get("product_id"));

        if(obj.containsKey("product_name"))
            this.setProductName((String) obj.get("product_name"));

        if(obj.containsKey("product_description"))
            this.setProductDescription(((String) obj.get("product_description")));

        if(obj.containsKey("quantity"))
            this.setQuantity((Long) obj.get("quantity"));

        if(obj.containsKey("unit_price"))
            this.setUnitPrice((Long) obj.get("unit_price"));

        if(obj.containsKey("discount_type"))
            this.setDiscountType((String) obj.get("discount_type"));

        if(obj.containsKey("total_discount_amount"))
            this.setTotalDiscountAmount((Long) obj.get("total_discount_amount"));

        if(obj.containsKey("item_subtotal"))
            this.setItemSubtotal((Long) obj.get("item_subtotal"));

        if(obj.containsKey("item_total"))
            this.setItemTotal((Double) obj.get("item_total"));

        if(obj.containsKey("total_tax_amount"))
            this.setTotalTaxAmount((Double) obj.get("total_tax_amount"));

        if(obj.containsKey("owner_id"))
            this.setOwnerId((Long) obj.get("owner_id"));

        if(obj.containsKey("created_by"))
            this.setCreatedBy((Long) obj.get("created_by"));

        if(obj.containsKey("created"))
            this.setCreated(Instant.parse((String) obj.get("created")));

        if(obj.containsKey("updated"))
            this.setUpdated(Instant.parse((String) obj.get("updated")));

        if(obj.containsKey("discount_amount"))
            this.setDiscountAmount((Double) obj.get("discount_amount"));

    }

}
