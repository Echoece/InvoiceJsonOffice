package com.example.invoice.util.pdfUtil;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class Test {

    public void test() throws IOException {
        PDDocument pdDocument = new PDDocument();
        PDPage page = new PDPage();

        pdDocument.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(pdDocument,page);
        contentStream.beginText();
        contentStream.newLineAtOffset(100, 200);
        contentStream.setFont(PDType1Font.TIMES_BOLD, 14);
        contentStream.showText("hello world");
        contentStream.endText();

        contentStream.close();
        pdDocument.save("Testing.pdf");
        pdDocument.close();
    }
}
