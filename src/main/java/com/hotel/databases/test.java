package com.hotel.databases;

import java.util.Calendar;
import java.util.Date;

import static com.hotel.databases.GeneratePdf.generateReceipt;
import static com.hotel.utilities.MailSender.sendConfirmation;

public class test
{
    public static void main (String[] args)
    {
        Date now = new Date();
        Date end = new Date();
        sendConfirmation("102", now, 12,"ismaelsan120m@gmail.com");
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 1);
//        generateReceipt("Trying", "12", now, end, "Fauster", "102");
    }
}
