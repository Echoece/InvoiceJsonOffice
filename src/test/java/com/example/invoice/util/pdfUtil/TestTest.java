package com.example.invoice.util.pdfUtil;

import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestTest {
    private Test test = new Test();

    @org.junit.jupiter.api.Test
    public void testing() throws IOException {
        test.test();
    }

}