package com.example.invoice.util.pdfUtil;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class BalanaceSheetPdfTest {
    private BalanceSheetPdf pdf = new BalanceSheetPdf();

    @Test
    public void testing() throws IOException {
        pdf.create();
    }
}