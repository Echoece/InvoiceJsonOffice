package com.example.invoice.util.pdfUtil;

import com.example.invoice.util.jsonUtil.InvoiceFromJson;
import com.example.invoice.util.jsonUtil.model.Invoice;
import com.example.invoice.util.jsonUtil.model.InvoiceItems;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Component
public class CoreJavaImpl {
    private InvoiceFromJson invoiceFromJson;

    public void create() throws IOException {
        // initial setup for pdf and getting data from json file,
        Invoice invoice = invoiceFromJson.getInvoice(new File("src/main/resources/json/Invoice.json"));
        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document,page);
        contentStream.setNonStrokingColor(Color.BLACK);

        float baseYPosition = page.getCropBox().getHeight()-54;
        int baseLineGap= 11;


        // customer details (top-left side)
        writeText(contentStream,"BDX", PDType1Font.HELVETICA_BOLD,12, 55,baseYPosition , RenderingMode.FILL);
        writeText(contentStream, "Company ID : "+ invoice.getCustomer().getCompanyId(),PDType1Font.HELVETICA,9, 55, baseYPosition-baseLineGap,RenderingMode.FILL);
        writeText(contentStream, "Tax ID : 986432",PDType1Font.HELVETICA,9, 55, baseYPosition- (2*baseLineGap),RenderingMode.FILL);

        List<String> addressLines = formattedAddress(invoice.getShippingAddress());
        int tempMultiplier =3;
        for (String line : addressLines){
            writeText(contentStream, line, PDType1Font.HELVETICA, 9,55, baseYPosition-(tempMultiplier*baseLineGap),RenderingMode.FILL);
            tempMultiplier++;
        }

        // Invoice details (top-right side)
        writeText(contentStream,"INVOICE",PDType1Font.HELVETICA, 24,450, baseYPosition, RenderingMode.FILL);
        writeText(contentStream, "# INV-" + invoice.getInvoiceNo(), PDType1Font.HELVETICA, 10,450, baseYPosition-(2*baseLineGap), RenderingMode.FILL);

        writeText(contentStream, "Balance Due", PDType1Font.HELVETICA_BOLD, 8,450, baseYPosition-(5*baseLineGap), RenderingMode.FILL);
        writeText(contentStream, "BDT "+String.format("%,.2f", invoice.getTotal()), PDType1Font.HELVETICA_BOLD, 12,450, baseYPosition-(6*baseLineGap), RenderingMode.FILL);

        // Bill to (below the customer details on the top-left side)
        writeText(contentStream, "Bill To",PDType1Font.HELVETICA,11, 55,baseYPosition - (9*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "John Doe",PDType1Font.HELVETICA_BOLD,9, 55,baseYPosition - (10*baseLineGap),RenderingMode.FILL);

        addressLines = formattedAddress(invoice.getBillingAddress());
        tempMultiplier =11;
        for (String line : addressLines){
            writeText(contentStream, line, PDType1Font.HELVETICA, 9,55, baseYPosition-(tempMultiplier*baseLineGap),RenderingMode.FILL);
            tempMultiplier++;
        }

        //subject
        writeText(contentStream, "Subject :",PDType1Font.HELVETICA,9,55,baseYPosition - (18*baseLineGap),RenderingMode.FILL);
        String firstProductName =invoice.getInvoiceItems().stream().findFirst().get().getProductName();
        writeText(contentStream, "Invoice of purchasing " + firstProductName,PDType1Font.HELVETICA,9,55,baseYPosition - (19*baseLineGap),RenderingMode.FILL);

        // invoice dates and terms (below invoice details on the top-right side)
        writeText(contentStream, "Invoice Date :       " + invoice.getInvoiceDate(),PDType1Font.HELVETICA,11,400,baseYPosition - (11*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Terms :                 " + "Due on Receipt",PDType1Font.HELVETICA,11,400,baseYPosition - (13*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Due Date :            " + invoice.getInvoiceDueDate(),PDType1Font.HELVETICA,11,400,baseYPosition - (15*baseLineGap),RenderingMode.FILL);

        // invoice item menu header
        contentStream.addRect(55,baseYPosition-(22*baseLineGap),530,20);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.fill();
        writeText(contentStream, "#",PDType1Font.HELVETICA, Color.white,10,60,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Item & Description",PDType1Font.HELVETICA, Color.white,10,100,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Qty",PDType1Font.HELVETICA, Color.white,10,400,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Rate",PDType1Font.HELVETICA, Color.white,10,470,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);
        writeText(contentStream, "Amount",PDType1Font.HELVETICA, Color.white,10,540,baseYPosition - (22*baseLineGap)+7,RenderingMode.FILL);

        int i= 1;
        int itemlistBaseY = 23;
        Float yPos = 0f;
        Iterator<InvoiceItems> itr =  invoice.getInvoiceItems().iterator();
        while (itr.hasNext()){
            InvoiceItems item = itr.next();
            try {
                yPos =baseYPosition - (itemlistBaseY * baseLineGap) - 10;

                writeText(contentStream, Integer.toString(i),PDType1Font.HELVETICA, Color.black,9,60, yPos,RenderingMode.FILL);
                writeText(contentStream, item.getProductName(),PDType1Font.HELVETICA_BOLD, Color.black,9,100, yPos,RenderingMode.FILL);
                writeText(contentStream, item.getProductDescription(),PDType1Font.HELVETICA, Color.black,9,100, yPos- 10,RenderingMode.FILL);
                writeText(contentStream, Long.toString(item.getQuantity()),PDType1Font.HELVETICA, Color.black,9,400, yPos,RenderingMode.FILL);
                writeText(contentStream, "pcs",PDType1Font.HELVETICA, Color.black,9,400, yPos -10,RenderingMode.FILL);
                writeText(contentStream, Long.toString(item.getUnitPrice()),PDType1Font.HELVETICA, Color.black,9,470, yPos,RenderingMode.FILL);
                writeText(contentStream, Long.toString(item.getItemSubtotal()),PDType1Font.HELVETICA, Color.black,9,540, yPos,RenderingMode.FILL);

                contentStream.moveTo(60, yPos-15);
                contentStream.lineTo(580, yPos -15);
                contentStream.stroke();

                i++;
                itemlistBaseY+=4;

                if(( baseYPosition - (itemlistBaseY*baseLineGap) - 10 ) < 50 ){
                    PDPage nextPage = new PDPage();
                    document.addPage(nextPage);
                    contentStream.close();
                    contentStream = new PDPageContentStream(document,nextPage);
                    baseYPosition = nextPage.getCropBox().getHeight() -54;
                    itemlistBaseY = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        // TODO: test logic for creating new page in case it overflows, for both the invoice details and the final notes logic is not tested
        // invoice total
        float startPosition = baseYPosition - (itemlistBaseY*baseLineGap);
        if(startPosition < 100 ){
            PDPage nextPage = new PDPage();
            document.addPage(nextPage);
            contentStream.close();
            contentStream = new PDPageContentStream(document,nextPage);
            startPosition = nextPage.getCropBox().getHeight()-54;
        }
        writeText(contentStream,"Sub Total:               "+ invoice.getSubtotal(),PDType1Font.HELVETICA,9,400,startPosition,RenderingMode.FILL);
        writeText(contentStream, "Total:                       " + invoice.getTotal(),PDType1Font.HELVETICA,9,400,startPosition - (2*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Discount:                 "+invoice.getDiscount(),PDType1Font.HELVETICA, 9, 400,startPosition-(4*baseLineGap),RenderingMode.FILL);
        writeText(contentStream, "Shipping Charge:   100.00",PDType1Font.HELVETICA, 9, 400,startPosition-(6*baseLineGap),RenderingMode.FILL);

        contentStream.addRect(300,startPosition- (9*baseLineGap),300,25);
        contentStream.setNonStrokingColor(Color.lightGray);
        contentStream.fill();
        writeText(contentStream,"Balance Due:          "+ String.format("%,.2f",430000.00f),PDType1Font.HELVETICA,Color.BLACK,9,400,startPosition - (9*baseLineGap)+9,RenderingMode.FILL);


        // final note
        if(startPosition - (10*baseLineGap) < 60 ){
            PDPage nextPage = new PDPage();
            document.addPage(nextPage);
            contentStream.close();
            contentStream = new PDPageContentStream(document,nextPage);
            startPosition = nextPage.getCropBox().getHeight()-54;
        }
        writeText(contentStream,"Notes", PDType1Font.HELVETICA,10,55,startPosition - (14*baseLineGap),RenderingMode.FILL);
        writeText(contentStream,"Thanks for your business", PDType1Font.HELVETICA,8,55,startPosition - (15*baseLineGap),RenderingMode.FILL);
        writeText(contentStream,"Terms & Conditions", PDType1Font.HELVETICA,10,55,startPosition - (17*baseLineGap),RenderingMode.FILL);
        writeText(contentStream,"Company is not responsible if the product is damaged during transportation", PDType1Font.HELVETICA,8,55,startPosition - (18*baseLineGap),RenderingMode.FILL);

        // final line at the end of page
        contentStream.moveTo(60, 30);
        contentStream.lineTo(580,  30);
        contentStream.stroke();
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

    static List<String> formattedAddress(String adressToFormat){
        // split the adress into words -> add them back into a List of strings, each string in the list will
        // have width of less than 40.
        List<String> lines = new ArrayList<>();

        String[] address = adressToFormat.split(" ");
        int addressWidth = 30; // to limit the address per line
        int tempLength=0;
        String tempString = "";

        for(String word: address){
            tempLength = tempLength + word.length();
            if(tempLength <= addressWidth){
                tempLength += word.length();
                tempString = tempString + word +" ";
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
