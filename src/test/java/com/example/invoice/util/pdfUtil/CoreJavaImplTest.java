package com.example.invoice.util.pdfUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CoreJavaImplTest {
    @Autowired
    private CoreJavaImpl coreJava;

    @Test
    public void addressFormatTest(){
        List<String> lines = new ArrayList<>();

        String[] address = "19/\"19/2A Debi Das Ghat Lane, Chawk Bazar Dhaka Dhaka 1211 Bangladesh\"2A Gulshan-2 Dhaka 1211 Dhaka bibhag Bangladesh".split(" ");
        int addressWidth = 40;
        int tempLength=0;
        String tempString = "";

        for(String word: address){
            tempLength = tempLength + word.length();
            if(tempLength <= addressWidth){
                tempString = tempString + word +" ";
            }
            else {
                lines.add(tempString);
                tempLength=0;
                tempString="";
            }
        }
        lines.forEach(System.out::println);
    }

    @Test
    public void createPdf() throws IOException {
        coreJava.create();
    }


}
