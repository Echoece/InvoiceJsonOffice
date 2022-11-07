package com.example.invoice.util.pdfUtil;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheetPdf {
    PDPageContentStream contentStream;
    public void create() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        contentStream = new PDPageContentStream(document,page);
        contentStream.setNonStrokingColor(Color.BLACK);

        PDType1Font font = PDType1Font.HELVETICA;
        PDType1Font font_bold = PDType1Font.HELVETICA_BOLD;
        float baseYPosition = page.getCropBox().getHeight()-54;
        System.out.println(page.getCropBox().getHeight());
        int baseLineGap= 11;
        float startPosition= 0;

        // header rectangle and title
        contentStream.addRect(36, baseYPosition - baseLineGap, 540, 50);
        contentStream.setNonStrokingColor(23 / 255f, 159 / 255f, 149 / 255f);
        contentStream.fill();
        writeText("Balance Sheet", font, Color.white, 18, 250, baseYPosition - baseLineGap + 18);

        // left bar and totals
        writeText("Rafiqul Echo", font_bold, Color.black,14, 70, baseYPosition - ( 4 * baseLineGap));
        writeText("BALANCE SHEET", font, Color.black,9, 70, baseYPosition - ( 5.5f * baseLineGap));
        writeText("Aug 30, 2022", font, Color.black,9, 70, baseYPosition - ( 7 * baseLineGap));

        writeText("ASSETS", font, 9, 70, baseYPosition - ( 11 * baseLineGap) );
        writeText("23000.00", font, Color.black, 9, 500, baseYPosition - ( 11 * baseLineGap) );
        contentStream.moveTo(60,  baseYPosition - (11.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (11.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("LIABILITY", font, Color.black, 9, 70, baseYPosition - (13 * baseLineGap));
        writeText("23000.00", font, Color.black, 9, 500, baseYPosition - (13 * baseLineGap));
        contentStream.moveTo(60,  baseYPosition - (13.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (13.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("EQUITY", font, Color.black, 9, 70, baseYPosition - (15 * baseLineGap));
        writeText("23000.00", font, Color.black, 9, 500, baseYPosition - (15 * baseLineGap));

        // assets
        contentStream.addRect(44, baseYPosition - (22 * baseLineGap), 520, 30);
        contentStream.setNonStrokingColor(242 / 255f, 242 / 255f, 242 / 255f);
        contentStream.fill();
        writeText("ASSETS", font, Color.black ,11, 70, baseYPosition - (22 * baseLineGap) + 10);

        writeText("Fixed Asset", font, 10, 70, baseYPosition - ( 24 * baseLineGap) );
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - ( 24 * baseLineGap) );
        contentStream.moveTo(60,  baseYPosition - (24.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (24.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("HST Receivable", font, Color.black, 10, 70, baseYPosition - (26 * baseLineGap));
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - (26 * baseLineGap));
        contentStream.moveTo(60,  baseYPosition - (26.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (26.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("Bank Account", font, Color.black, 10, 70, baseYPosition - (28 * baseLineGap));
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - (28 * baseLineGap));

        writeText("Total Assets", font_bold, Color.black, 10, 70, baseYPosition - (31 * baseLineGap));
        writeText("23000.00", font_bold, Color.black, 10, 500, baseYPosition - (31 * baseLineGap));


        // liabilities
        contentStream.addRect(44, baseYPosition - (38 * baseLineGap), 520, 30);
        contentStream.setNonStrokingColor(242 / 255f, 242 / 255f, 242 / 255f);
        contentStream.fill();
        writeText("LIABILITES", font, Color.black ,11, 70, baseYPosition - (38 * baseLineGap) + 10);

        writeText("Fixed Asset", font, 10, 70, baseYPosition - ( 41 * baseLineGap) );
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - ( 41 * baseLineGap) );
        contentStream.moveTo(60,  baseYPosition - (41.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (41.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("HST Receivable", font, Color.black, 10, 70, baseYPosition - (43 * baseLineGap));
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - (43 * baseLineGap));
        contentStream.moveTo(60,  baseYPosition - (43.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (43.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("Bank Account", font, Color.black, 10, 70, baseYPosition - (45 * baseLineGap));
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - (45 * baseLineGap));

        writeText("Total Liabilites", font_bold, Color.black, 10, 70, baseYPosition - (48 * baseLineGap));
        writeText("23000.00", font_bold, Color.black, 10, 500, baseYPosition - (48 * baseLineGap));


        // Equity
        contentStream.addRect(44, baseYPosition - (55 * baseLineGap), 520, 30);
        contentStream.setNonStrokingColor(242 / 255f, 242 / 255f, 242 / 255f);
        contentStream.fill();
        writeText("EQUITY", font, Color.black ,11, 70, baseYPosition - (55 * baseLineGap) + 10);

        writeText("Fixed Asset", font, 10, 70, baseYPosition - ( 58 * baseLineGap) );
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - ( 58 * baseLineGap) );
        contentStream.moveTo(60,  baseYPosition - (58.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (58.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("HST Receivable", font, Color.black, 10, 70, baseYPosition - (60 * baseLineGap));
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - (60 * baseLineGap));
        contentStream.moveTo(60,  baseYPosition - (60.5f * baseLineGap));
        contentStream.lineTo(580,  baseYPosition- (60.5f * baseLineGap));
        contentStream.setStrokingColor(206 / 255f, 206 / 255f, 206 / 255f);
        contentStream.stroke();
        writeText("Bank Account", font, Color.black, 10, 70, baseYPosition - (62 * baseLineGap));
        writeText("23000.00", font, Color.black, 10, 500, baseYPosition - (62 * baseLineGap));

        writeText("Total Equity", font_bold, Color.black, 10, 70, baseYPosition - (65 * baseLineGap));
        writeText("23000.00", font_bold, Color.black, 10, 500, baseYPosition - (65 * baseLineGap));

        contentStream.close();
        document.save("BalanceSheet.pdf");
        document.close();

    }



    private  void writeText( String text, PDFont font,
                                  int size, float xPos, float yPos) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        contentStream.showText(text);
        contentStream.endText();
    }
    private  void writeText( String text, PDFont font, Color color,
                                  int size, float xPos, float yPos) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        contentStream.setNonStrokingColor(color);
        contentStream.showText(text);
        contentStream.endText();
    }

    // utility method to get formatted address
    private  java.util.List<String> formattedAddress(String addressToFormat, int addressWidth){
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
    private  float getXOffsetForRightAlignedText(PDType1Font font, int fontSize, String text, float endX ) throws IOException {
        float width = getTextWidth(font,fontSize, text);
        return (endX- width);
    }
    private  float getTextWidth(PDType1Font font, int fontSize, String text) throws IOException {
        return (font.getStringWidth(text) / 1000.0f) * fontSize;
    }
}
