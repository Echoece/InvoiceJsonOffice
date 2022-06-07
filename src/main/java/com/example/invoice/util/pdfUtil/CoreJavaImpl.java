package com.example.invoice.util.pdfUtil;

import com.example.invoice.util.jsonUtil.InvoiceFromJson;
import com.example.invoice.util.jsonUtil.model.Invoice;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class CoreJavaImpl {
    private InvoiceFromJson invoiceFromJson;

    public void create() throws IOException {
        Invoice invoice = invoiceFromJson.getInvoice(new File("src/main/resources/json/Invoice.json"));
        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document,page);
        contentStream.setNonStrokingColor(Color.BLACK);

        float baseYPosition = page.getCropBox().getHeight()-54;
        int baseLineGap= 11;
        int tempMultiplier =3;
        
        // customer details (top-left side)
        writeText(contentStream,"BDX", PDType1Font.HELVETICA_BOLD,12, 55,baseYPosition , RenderingMode.FILL);
        writeText(contentStream, "Company ID : "+ invoice.getCustomer().getCompanyId(),PDType1Font.HELVETICA,9, 55, baseYPosition-baseLineGap,RenderingMode.FILL);
        writeText(contentStream, "Tax ID : 986432",PDType1Font.HELVETICA,9, 55, baseYPosition- (2*baseLineGap),RenderingMode.FILL);

        List<String> addressLines = formattedAddress(invoice);

        for (String line : addressLines){
            writeText(contentStream, line, PDType1Font.HELVETICA, 9,55, baseYPosition-(tempMultiplier*baseLineGap),RenderingMode.FILL);
            tempMultiplier++;
        }

        // Invoice details (top-right side)
        writeText(contentStream,"INVOICE",PDType1Font.HELVETICA, 24,450, baseYPosition, RenderingMode.FILL);
        String invoiceNumber = "000001"; // issue with leading zero while parsing string
        writeText(contentStream, "# INV-" + invoiceNumber, PDType1Font.HELVETICA, 10,450, baseYPosition-(2*baseLineGap), RenderingMode.FILL);

        writeText(contentStream, "Balance Due", PDType1Font.HELVETICA_BOLD, 8,450, baseYPosition-(5*baseLineGap), RenderingMode.FILL);
        String currency = "BDT ";
        Float amount =47530.00f;
        writeText(contentStream, currency+String.format("%,.2f", amount), PDType1Font.HELVETICA_BOLD, 12,450, baseYPosition-(6*baseLineGap), RenderingMode.FILL);

        // Bill to (below the customer details on the top-left side)
        writeText(contentStream, "Bill To",PDType1Font.HELVETICA,11, 55,baseYPosition - (9*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "John Doe",PDType1Font.HELVETICA_BOLD,9, 55,baseYPosition - (10*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "19/2A Gulshan-2",PDType1Font.HELVETICA,9,55,baseYPosition - (11*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Dhaka",PDType1Font.HELVETICA,9,55,baseYPosition - (12*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "1211 Dhaka bibhag",PDType1Font.HELVETICA,9,55,baseYPosition - (13*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Bangladesh",PDType1Font.HELVETICA,9,55,baseYPosition - (14*baseLineGap),RenderingMode.FILL);
        //subject
        writeText(contentStream, "Subject :",PDType1Font.HELVETICA,9,55,baseYPosition - (18*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Invoice of purchasing mobile",PDType1Font.HELVETICA,9,55,baseYPosition - (19*baseLineGap),RenderingMode.FILL);


        // invoice dates and terms (below invoice details on the top-right side)
        writeText(contentStream, "Invoice Date :       " + LocalDate.now(),PDType1Font.HELVETICA,11,400,baseYPosition - (11*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Terms :                 " + "Due on Receipt",PDType1Font.HELVETICA,11,400,baseYPosition - (13*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Due Date :            " + LocalDate.now(),PDType1Font.HELVETICA,11,400,baseYPosition - (15*baseLineGap),RenderingMode.FILL);

        // invoice item menu header
        contentStream.addRect(55,baseYPosition-(22*baseLineGap),530,20);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.fill();
        writeText(contentStream, "#",PDType1Font.HELVETICA, Color.white,10,60,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Item & Description",PDType1Font.HELVETICA, Color.white,10,100,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Qty",PDType1Font.HELVETICA, Color.white,10,400,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Rate",PDType1Font.HELVETICA, Color.white,10,470,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Amount",PDType1Font.HELVETICA, Color.white,10,540,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);

        AtomicInteger i= new AtomicInteger(1);
        AtomicInteger itemlistBaseY = new AtomicInteger(23);
        AtomicReference<Float> yPos = new AtomicReference<>((float) 0);
        invoice.getInvoiceItems().forEach(
                item -> {
                    try {
                        yPos.set(baseYPosition - (itemlistBaseY.get() * baseLineGap) - 10);

                        writeText(contentStream, Integer.toString(i.get()),PDType1Font.HELVETICA, Color.black,9,60, yPos.get(),RenderingMode.FILL);
                        writeText(contentStream, item.getProductName(),PDType1Font.HELVETICA_BOLD, Color.black,9,100, yPos.get(),RenderingMode.FILL);
                        writeText(contentStream, item.getProductDescription(),PDType1Font.HELVETICA, Color.black,9,100, yPos.get()- 10,RenderingMode.FILL);
                        writeText(contentStream, Long.toString(item.getQuantity()),PDType1Font.HELVETICA, Color.black,9,400, yPos.get(),RenderingMode.FILL);
                        writeText(contentStream, "pcs",PDType1Font.HELVETICA, Color.black,9,400, yPos.get() -10,RenderingMode.FILL);
                        writeText(contentStream, String.format("%,.2f",item.getUnitPrice()),PDType1Font.HELVETICA, Color.black,9,470, yPos.get(),RenderingMode.FILL);
                        writeText(contentStream, String.format("%,.2f",item.getItemSubtotal()),PDType1Font.HELVETICA, Color.black,9,540, yPos.get(),RenderingMode.FILL);

                        contentStream.moveTo(60, yPos.get() -15);
                        contentStream.lineTo(580, yPos.get() -15);
                        contentStream.stroke();

                        i.getAndAdd(1);
                        itemlistBaseY.getAndAdd(4);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // invoice total
        float startPosition = baseYPosition - (itemlistBaseY.get()*baseLineGap);
        writeText(contentStream, "Total:                   " + String.format("%,.2f",430000.00f),PDType1Font.HELVETICA, Color.black,11,400,startPosition,RenderingMode.FILL);

        contentStream.addRect(300,startPosition- (3*baseLineGap),300,25);
        contentStream.setNonStrokingColor(Color.lightGray);
        contentStream.fill();
        writeText(contentStream,"Balance Due:      "+ String.format("%,.2f",430000.00f),PDType1Font.HELVETICA, Color.black,11,400,startPosition - (3*baseLineGap)+7,RenderingMode.FILL);

        System.out.println(page.getCropBox());

        contentStream.close();
        document.save("test.pdf");
        document.close();
    }

    static void writeText(PDPageContentStream contentStream, String text, PDFont font,
                          int size, float xPos, float yPos, RenderingMode renderMode) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        //contentStream.setRenderingMode(renderMode);
        contentStream.showText(text);
        contentStream.endText();
    }
    static void writeText(PDPageContentStream contentStream, String text, PDFont font, Color color,
                          int size, float xPos, float yPos, RenderingMode renderMode) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        contentStream.setNonStrokingColor(color);
        //contentStream.setRenderingMode(renderMode);
        contentStream.showText(text);
        contentStream.endText();
    }

    static List<String> formattedAddress(Invoice invoice){
        // split the adress into words -> add them back into a List of strings, each string in the list will
        // have width of less than 40.
        List<String> lines = new ArrayList<>();

        String[] address = invoice.getShippingAddress().split(" ");
        int addressWidth = 40; // to limit the address per line
        int tempLength=0;
        String tempString = "";

        for(String word: address){
            tempLength = tempLength + word.length();
            if(tempLength <= addressWidth){
                tempString = tempString + word;
            }
            else {
                lines.add(tempString);
                tempLength=0;
                tempString="";
            }
        }

        return lines;
    }
    static float getTextWidth(PDType1Font font, int fontSize, String text) throws IOException {
        return (font.getStringWidth(text) / 1000.0f) * fontSize;
    }
}
