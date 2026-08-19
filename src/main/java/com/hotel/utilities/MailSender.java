package com.hotel.utilities;

import com.hotel.databases.Vatis;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

public class MailSender {

    private static  String username;
    private static  String password;
    private static final String SMTP_HOST = "://gmail.com";
    private static final String SMTP_PORT = "587";

    public static void sendConfirmation(String roomChamber, Date bookTime, int days, String customerMail)
    {
        new Thread(() ->
        {
            try(InputStream input = Vatis.class.getClassLoader().getResourceAsStream("database.properties"))
            {
                Properties properties = new Properties();
                if (input == null) {
                    System.err.println("Sorry, unable to find config.properties");
                    return;
                }
                username = properties.getProperty("email");
                password = properties.getProperty("mailPass");
                properties.load(input);
                Properties prop = new Properties();
                prop.put("mail.smtp.auth", "true");
                prop.put("mail.smtp.starttls.enable", "true");
                prop.put("mail.smtp.host", SMTP_HOST);
                prop.put("mail.smtp.port", SMTP_PORT);

                Session session = Session.getInstance(prop, new Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });

                SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy");
                String day  = formatter.format(bookTime);

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username, "Agnes Sweet Home"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customerMail));

                message.setSubject("Confirmation de votre réservation - Agnes Sweet Home");

                String emailBody = String.format
                    (
                    "Bonjour cher client,\n\n" +
                        "Merci pour votre confiance! Votre réservation a été enregistrée avec succès.\n\n" +
                        "Détails de la réservation :\n" +
                        "\t- Chambre : %s\n" +
                        "\t- Dates : %s\n" +
                        "\t- Reserver pour : %d jour(s)\n\n" +
                        "À très bientôt,\nL'équipe Agnes Sweet Home.",
                    roomChamber, day, days
                );

                message.setText(emailBody);

                Transport.send(message);
                System.out.println("Confirmation email instantly sent to: " + customerMail);

            } catch (Exception e) {
                System.err.println("Failed to send instant email notification: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
