package com.example.invoice;

import com.example.invoice.bristy.QCardInfo;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import ezvcard.Ezvcard;
import ezvcard.VCard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

@SpringBootApplication
@EnableScheduling
public class InvoiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {

			BufferedImage readerImage = ImageIO.read(new FileInputStream("C:\\Echo\\Polish_PDF\\InvoiceJson\\office.jpeg"));
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(readerImage)));
			Result resultObj = new MultiFormatReader().decode(binaryBitmap);

			//System.out.println(resultObj.getText());

			VCard vcard = Ezvcard.parse(resultObj.getText()).first();

			//vcard.getEmails().forEach(element -> System.out.println(element.getValue()));


			QCardInfo qCardInfo = QCardInfo.builder()
					.email(vcard.getEmails().stream().findFirst().toString())
					.name(vcard.getFormattedName().getValue())
					.telephone(vcard.getTelephoneNumbers().stream().findFirst().toString())
					.url(vcard.getUrls().toString())
					.build();
			
			System.out.println(qCardInfo);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
