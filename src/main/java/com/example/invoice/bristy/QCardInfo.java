package com.example.invoice.bristy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QCardInfo {
    private String name;
    private String email;
    private String url;
    private String telephone;
}
