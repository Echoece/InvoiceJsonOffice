package com.example.invoice.util.pdfUtil;

import com.example.invoice.util.jsonUtil.InvoiceFromJson;
import com.example.invoice.util.jsonUtil.model.Invoice;
import com.example.invoice.util.jsonUtil.model.InvoiceItems;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Component
public class CoreJavaImpl {
    private InvoiceFromJson invoiceFromJson;
    public void create() throws IOException {
        // initial setup,
        Invoice invoice = invoiceFromJson.getInvoice(new File("src/main/resources/json/Invoice.json"));
        PDDocument document = new PDDocument();

        PDType1Font roboto = PDType1Font.HELVETICA;
        PDType1Font roboto_BOLD = PDType1Font.HELVETICA_BOLD;

        // penny perfect uses roboto fonts
        //PDType0Font roboto = PDType0Font.load(document,new File("src/main/resources/fonts/roboto/Roboto-Light.ttf"));
        //PDType0Font roboto_BOLD = PDType0Font.load(document,new File("src/main/resources/fonts/roboto/Roboto-Medium.ttf"));

        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document,page);
        contentStream.setNonStrokingColor(Color.BLACK);

        float baseYPosition = page.getCropBox().getHeight()-54;
        int baseLineGap= 11;

        /*----------------------------------------------------------------------------------------------------------------*/
        // customer details (top-left side)
        // TODO: need to clarify BDX and Tax ID field, where it is coming from?
        //  : the address seems to be stored in the address table
        //  : tax code is confusing, since there can be many
        writeText(contentStream,"BDX", roboto_BOLD,12, 55,baseYPosition );
        String companyId = "";
        if(invoice.getCustomer()!=null && invoice.getCustomer().getCompanyId()!=null)
            companyId = invoice.getCustomer().getCompanyId().toString();
        writeText(contentStream, "Company ID : "+ companyId,roboto,9, 55, baseYPosition-baseLineGap);
        writeText(contentStream, "Tax ID : 986432",roboto,9, 55, baseYPosition- (2*baseLineGap));

        List<String> addressLines = formattedAddress(invoice.getShippingAddress(),35);
        int tempMultiplier =3;
        for (String line : addressLines){
            writeText(contentStream, line, roboto, 9,55, baseYPosition-(tempMultiplier*baseLineGap));
            tempMultiplier++;
        }

        // Bill to (below the customer details on the top-left side)
        writeText(contentStream, "Bill To",roboto,11, 55,baseYPosition - (9*baseLineGap));
        writeText(contentStream, "John Doe",roboto_BOLD,9, 55,baseYPosition - (10*baseLineGap));

        addressLines = formattedAddress(invoice.getBillingAddress(), 35);
        tempMultiplier =11;
        for (String line : addressLines){
            writeText(contentStream, line, roboto, 9,55, baseYPosition - (tempMultiplier*baseLineGap));
            tempMultiplier++;
        }

        /*----------------------------------------------------------------------------------------------------------------*/
        // Invoice details (top-right side)
        // invoice dates and terms (below invoice details on the top-right side)
        int invoiceDetailsEndXOffSet = 430;
        int invoiceDetailsValueEndXOffset = 570;

        writeText(contentStream,"INVOICE",roboto_BOLD, 24,getXOffsetForRightAlignedText(roboto_BOLD,24,"INVOICE", invoiceDetailsValueEndXOffset), baseYPosition);
        writeText(contentStream, "# INV-" + invoice.getInvoiceNo(), roboto_BOLD, 10,getXOffsetForRightAlignedText(roboto_BOLD,10,"# INV-"+ invoice.getInvoiceNo(), invoiceDetailsValueEndXOffset), baseYPosition-(2*baseLineGap));
        writeText(contentStream, "Balance Due", roboto_BOLD, 8,getXOffsetForRightAlignedText(roboto_BOLD,8,"Balance Due", invoiceDetailsValueEndXOffset), baseYPosition-(5*baseLineGap));
        writeText(contentStream, "BDT "+ invoice.getTotal(), roboto_BOLD, 12,getXOffsetForRightAlignedText(roboto_BOLD,12,"BDT "+ invoice.getTotal(), invoiceDetailsValueEndXOffset), baseYPosition-(6*baseLineGap));


        writeText(contentStream, "Invoice Date :" ,roboto,11,getXOffsetForRightAlignedText(roboto, 11,"Invoice Date :",invoiceDetailsEndXOffSet),baseYPosition - (11*baseLineGap));
        writeText(contentStream, String.valueOf(invoice.getInvoiceDate()),roboto,11,getXOffsetForRightAlignedText(roboto, 11, String.valueOf(invoice.getInvoiceDate()),invoiceDetailsValueEndXOffset),baseYPosition - (11*baseLineGap));

        writeText(contentStream, "Terms :",roboto,11,getXOffsetForRightAlignedText(roboto, 11,"Terms :",invoiceDetailsEndXOffSet),baseYPosition - (13*baseLineGap));
        writeText(contentStream, "Due on Receipt",roboto,11,getXOffsetForRightAlignedText(roboto, 11, "Due on Receipt",invoiceDetailsValueEndXOffset),baseYPosition - (13*baseLineGap));

        writeText(contentStream, "Due Date :" ,roboto,11,getXOffsetForRightAlignedText(roboto, 11,"Due Date :",invoiceDetailsEndXOffSet),baseYPosition - (15*baseLineGap));
        writeText(contentStream, String.valueOf(invoice.getInvoiceDueDate()),roboto,11,getXOffsetForRightAlignedText(roboto, 11, String.valueOf(invoice.getInvoiceDueDate()),invoiceDetailsValueEndXOffset),baseYPosition - (15*baseLineGap));

        /*----------------------------------------------------------------------------------------------------------------*/
        //subject
        writeText(contentStream, "Subject :",roboto,9,55,baseYPosition - (18*baseLineGap));
        // TODO: need to clarify about the requirements of the subject in case multiple items
        String firstProductName =invoice.getInvoiceItems().stream().findFirst().orElse(new InvoiceItems()).getProductName();
        writeText(contentStream, "Invoice of purchase " + firstProductName,roboto,9,55,baseYPosition - (19*baseLineGap));

        // invoice item menu header
        contentStream.addRect(55,baseYPosition-(22*baseLineGap),540,25);
        contentStream.setNonStrokingColor(60/255f,60/255f,60/255f);
        contentStream.fill();
        writeText(contentStream, "#",roboto, Color.white,10,60,baseYPosition - (22*baseLineGap)+9);
        writeText(contentStream, "Item & Description",roboto, Color.white,10,100,baseYPosition - (22*baseLineGap)+9);
        writeText(contentStream, "Qty",roboto, Color.white,10,400,baseYPosition - (22*baseLineGap)+9);
        writeText(contentStream, "Rate",roboto, Color.white,10,470,baseYPosition - (22*baseLineGap)+9);
        writeText(contentStream, "Amount",roboto, Color.white,10,540,baseYPosition - (22*baseLineGap)+9);

        // invoice items iteration,
        // TODO: need to check for null pointer exception for invoiceItems
        int i= 1;
        int itemlistBaseY = 23;
        Iterator<InvoiceItems> itr =  invoice.getInvoiceItems().iterator();
        
        int rateEndXOffset = 490;
        int amountEndXOffset = 574;
        int quantityEndXOffset = 415;

        while (itr.hasNext() && i<8){
            InvoiceItems item = itr.next();
            try {
                Float yPos =baseYPosition - (itemlistBaseY * baseLineGap) - 10;

                writeText(contentStream, Integer.toString(i),roboto, Color.black,9,60, yPos);
                writeText(contentStream, item.getProductName(),roboto_BOLD, Color.black,9,100, yPos+5);
                // TODO: make a check for product description for being too long, i can probably trim it or just add more space.
                //  It can overflow for too long text
                writeText(contentStream, item.getProductDescription(),roboto, Color.black,9,100, yPos- 5);
                writeText(contentStream, Long.toString(item.getQuantity()),roboto, Color.black,9,getXOffsetForRightAlignedText(roboto,9,Long.toString(item.getQuantity()), quantityEndXOffset), yPos+5);
                writeText(contentStream, "pcs",roboto, Color.black,9,400, yPos -5);
                writeText(contentStream, Long.toString(item.getUnitPrice()),roboto, Color.black,9,getXOffsetForRightAlignedText(roboto,9,Long.toString(item.getUnitPrice()),rateEndXOffset), yPos);
                writeText(contentStream, Long.toString(item.getItemSubtotal()),roboto, Color.black,9,getXOffsetForRightAlignedText(roboto,9,Long.toString(item.getItemSubtotal()),amountEndXOffset), yPos);

                contentStream.moveTo(60, yPos-15);
                contentStream.lineTo(580, yPos -15);
                contentStream.setStrokingColor(150/255f,150/255f,150/255f);
                contentStream.stroke();

                i++;
                itemlistBaseY+=4;

                if(( baseYPosition - (itemlistBaseY*baseLineGap) - 10 ) < 50 ){
                    page = new PDPage();
                    document.addPage(page);
                    contentStream.close();
                    contentStream = new PDPageContentStream(document,page);
                    baseYPosition = page.getCropBox().getHeight() -54;
                    itemlistBaseY = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*----------------------------------------------------------------------------------------------------------------*/
        // invoice total
        float startPosition = baseYPosition - (itemlistBaseY * baseLineGap)- 5;
        int invoiceTotalEndXOffset = 470;
        int invoiceTotalValueEndXOffset = amountEndXOffset;

        if(startPosition < 100 ){
            page= new PDPage();
            document.addPage(page);
            contentStream.close();
            contentStream = new PDPageContentStream(document,page);
            startPosition = page.getCropBox().getHeight()-54;
        }

        writeText(contentStream, "Sub Total",roboto,Color.BLACK,9,getXOffsetForRightAlignedText(roboto,9,"Sub Total",invoiceTotalEndXOffset),startPosition);
        writeText(contentStream, String.valueOf(invoice.getSubtotal()),roboto,9,getXOffsetForRightAlignedText(roboto,9,String.valueOf(invoice.getSubtotal()),invoiceTotalValueEndXOffset),startPosition);

        writeText(contentStream, "Shipping Charge" ,roboto,9,getXOffsetForRightAlignedText(roboto,9,"Shipping Charge",invoiceTotalEndXOffset),startPosition - (2*baseLineGap));
        writeText(contentStream, "100.00",roboto,9,getXOffsetForRightAlignedText(roboto,9,"100.00",invoiceTotalValueEndXOffset),startPosition - (2*baseLineGap));

        writeText(contentStream, "Tax" ,roboto,9,getXOffsetForRightAlignedText(roboto,9,"Tax",invoiceTotalEndXOffset),startPosition - (4*baseLineGap));
        writeText(contentStream, String.valueOf(invoice.getTaxAmount()),roboto,9,getXOffsetForRightAlignedText(roboto,9,String.valueOf(invoice.getTaxAmount()),invoiceTotalValueEndXOffset),startPosition - (4*baseLineGap));

        writeText(contentStream, "Discount",roboto, 9, getXOffsetForRightAlignedText(roboto,9,"Discount",invoiceTotalEndXOffset),startPosition-(6*baseLineGap));
        writeText(contentStream, String.valueOf(invoice.getDiscount()),roboto, 9, getXOffsetForRightAlignedText(roboto,9,String.valueOf(invoice.getDiscount()),invoiceTotalValueEndXOffset),startPosition-(6*baseLineGap));

        writeText(contentStream, "Total",roboto_BOLD, 9, getXOffsetForRightAlignedText(roboto_BOLD,9,"Total",invoiceTotalEndXOffset),startPosition-(8*baseLineGap));
        writeText(contentStream, String.format("%,.2f",invoice.getTotal()),roboto_BOLD, 9, getXOffsetForRightAlignedText(roboto_BOLD,9,String.format("%,.2f",invoice.getTotal()),invoiceTotalValueEndXOffset),startPosition-(8*baseLineGap));


        contentStream.addRect(295,startPosition- (11*baseLineGap),300,25);
        contentStream.setNonStrokingColor(230/255f,230/255f,230/255f);
        contentStream.fill();
        writeText(contentStream,"Balance Due",roboto_BOLD,Color.BLACK,9,getXOffsetForRightAlignedText(roboto_BOLD,9,"Balance Due",invoiceTotalEndXOffset),startPosition - (11*baseLineGap)+9);
        writeText(contentStream,String.format("%,.2f",invoice.getDues()),roboto_BOLD,Color.BLACK,9,getXOffsetForRightAlignedText(roboto_BOLD,9,String.format("%,.2f",invoice.getDues()),invoiceTotalValueEndXOffset),startPosition - (11*baseLineGap)+9);

        /*----------------------------------------------------------------------------------------------------------------*/

        // final note
        if(startPosition - (10*baseLineGap) < 120 ){
            page = new PDPage();
            document.addPage(page);
            contentStream.close();
            contentStream = new PDPageContentStream(document,page);
            startPosition = page.getCropBox().getHeight()-530;
        } else
            startPosition = page.getCropBox().getHeight() - 530;

        writeText(contentStream,"Notes", roboto,10,60,startPosition - (14*baseLineGap));
        writeText(contentStream,"Thanks for your business", roboto,8,60,startPosition - (15*baseLineGap));
        writeText(contentStream,"Terms & Conditions", roboto,10,60,startPosition - (17*baseLineGap));
        writeText(contentStream,"Company is not responsible if the product is damaged during transportation", roboto,8,60,startPosition - (18*baseLineGap));

        // final line at the end of page
        contentStream.moveTo(60, 30);
        contentStream.lineTo(560,  30);
        contentStream.stroke();

        /*----------------------------------------------------------------------------------------------------------------*/
        System.out.println(page.getCropBox());
        contentStream.close();
        document.save("test.pdf");
        document.close();

        /*
                // for penny perfect app it returns byte array. we can use the other overload of the save() method which takes an outputStream argument
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                document.save(byteArrayOutputStream);
                document.close();
                return byteArrayOutputStream.toByteArray();
        */
    }

    private static void writeText(PDPageContentStream contentStream, String text, PDFont font,
                          int size, float xPos, float yPos) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        contentStream.showText(text);
        contentStream.endText();
    }
    private static void writeText(PDPageContentStream contentStream, String text, PDFont font, Color color,
                          int size, float xPos, float yPos) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        contentStream.setNonStrokingColor(color);
        contentStream.showText(text);
        contentStream.endText();
    }

    // utility method to get formatted address
    private static List<String> formattedAddress(String addressToFormat,int addressWidth){
        // addressToFormat the address into words -> add them back into a List of strings, each string in the list will represent
        // a line having width of less than the specified addressWidth.
        List<String> lines = new ArrayList<>();

        String[] address = addressToFormat.split(" ");
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

    // utility methods for getting the starting X offset for right aligned items.
    private static float getXOffsetForRightAlignedText(PDType1Font font, int fontSize, String text, float endX ) throws IOException {
        float width = getTextWidth(font,fontSize, text);
        return (endX- width);
    }
    private static float getTextWidth(PDType1Font font, int fontSize, String text) throws IOException {
        return (font.getStringWidth(text) / 1000.0f) * fontSize;
    }
}


