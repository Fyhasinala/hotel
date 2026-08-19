package com.hotel.databases;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratePdf {

    public static void generateRoomReport(String filename, String idReserv, Date bookTime, Date bookEnd, String nomClient, String roomNumber)
    {
        File directory = new File("Receipts");
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                System.out.println("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        File pdfFile = new File(directory, filename + ".pdf");
        Document document = new Document();

        try (FileOutputStream fos = new FileOutputStream(pdfFile))
        {
            PdfWriter.getInstance(document, fos);
            document.open();

            SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy");
            String start  = formatter.format(bookTime);
            String end = formatter.format(bookEnd);

            document.add(new Paragraph(String.format("Séjour N° : %s", idReserv)));
            document.add(new Paragraph(String.format("Client : %s", nomClient)));
            document.add(new Paragraph(String.format("Chambre N° : %d", roomNumber)));
            document.add(new Paragraph(String.format("Date d’entrée : %s", start)));
            document.add(new Paragraph(String.format("Date de sortie : %s", end)));

            System.out.println("PDF securely exported directly to: " + pdfFile.getAbsolutePath());

        } catch (DocumentException | IOException e) {
            System.err.println("Failed to compile layout components: " + e.getMessage());
            e.printStackTrace();
        } finally
        {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}
