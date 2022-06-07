package com.example.invoice.util.jsonUtil.model;

import lombok.Builder;
import lombok.Data;
import org.json.simple.JSONObject;

import java.time.Instant;
@Data
public class Customer {
    private Long id;
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private Long province;
    private Long country;
    private String phoneHome;
    private String phoneOffice;
    private Integer ext;
    private String email;
    private String web;
    private Long ownerId;
    private Long companyId;
    private Instant created;
    private Instant updated;

    public Customer(JSONObject obj){
        if(obj.containsKey("id"))
            this.setId((Long) obj.get("id"));

        if(obj.containsKey("name"))
            this.setName((String) obj.get("name"));

        if(obj.containsKey("address"))
            this.setAddress((String) obj.get("address"));

        if(obj.containsKey("postal_code"))
            this.setPostalCode((String) obj.get("postal_code"));

        if(obj.containsKey("city"))
            this.setCity((String) obj.get("city"));

        if(obj.containsKey("province"))
            this.setProvince((Long) obj.get("province"));

        if(obj.containsKey("country"))
            this.setCountry((Long) obj.get("country"));

        if(obj.containsKey("phone_home"))
            this.setPhoneHome((String) obj.get("phone_home"));

        if(obj.containsKey("phone_office"))
            this.setPhoneOffice((String) obj.get("phone_office"));

        if(obj.containsKey("ext"))
            this.setExt((Integer) obj.get("ext"));

        if(obj.containsKey("email"))
            this.setEmail((String) obj.get("email"));

        if(obj.containsKey("web"))
            this.setWeb((String) obj.get("web"));

        if(obj.containsKey("owner_id"))
            this.setOwnerId((Long) obj.get("owner_id"));

        if(obj.containsKey("company_id"))
            this.setCompanyId((Long) obj.get("company_id"));

        if(obj.containsKey("created"))
            this.setCreated(Instant.parse((String) obj.get("created")));

        if(obj.containsKey("created"))
            this.setUpdated(Instant.parse((String) obj.get("created")));

    }
}
