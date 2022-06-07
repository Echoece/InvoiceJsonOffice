package com.example.invoice.util.pdfUtil;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoreJavaImplTest {


    @Test
    public void testingImp(){
        List<String> lines = new ArrayList<>();

        String[] address = "19/\"19/2A Debi Das Ghat Lane, Chawk Bazar Dhaka Dhaka 1211 Bangladesh\"2A Gulshan-2 Dhaka 1211 Dhaka bibhag Bangladesh".split(" ");
        int addressWidth = 40; // to limit the address per line
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
        lines.forEach(element-> System.out.println(element));
    }
}