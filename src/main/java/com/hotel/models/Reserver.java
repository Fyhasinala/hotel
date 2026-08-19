package com.hotel.models;

import javafx.beans.property.*;

public class Reserver {
    private final IntegerProperty idreserv;
	private final StringProperty dateReserv;
	private final StringProperty dateEntree;
	private final IntegerProperty nbrJour;
    private final StringProperty nomClient;
	private final StringProperty numClient;
	private final StringProperty mail;
	private final StringProperty numChambr; //cle etrangere


    public Reserver(int id, String dateRes, String dateEn, int jours, String nom, String num, String email, String chamb)
	{
		idreserv = new SimpleIntegerProperty(id);
		dateReserv = new SimpleStringProperty(dateRes);
		dateEntree = new SimpleStringProperty(dateEn);
		nbrJour = new SimpleIntegerProperty(jours);
        nomClient = new SimpleStringProperty(nom);
		numClient = new SimpleStringProperty(num);
		mail = new SimpleStringProperty (email);
		numChambr = new SimpleStringProperty(chamb);
		
	}

    public int getId() {return idreserv.get();}
    public IntegerProperty idresP() {return idreserv;}
    
    public String getDateRes() {return dateReserv.get();}
    public StringProperty dateReservP() {return dateReserv;}

    public String getDateEntree() {return dateEntree.get();}
    public void setDateEntree(String ndate) {dateEntree.set(ndate);}
    public StringProperty dateEntreeP() {return dateEntree;}

    public int getNbr() {return nbrJour.get();}
    public void setNbr(int jours) {nbrJour.set(jours);}
    public IntegerProperty nbrJourP() {return nbrJour;}

    public String getNomClient() {return nomClient.get();}
    public void setNomClient(String nom) {nomClient.set(nom);}
    public StringProperty nomClientP() {return nomClient;}

    public String getNumClient() {return numClient.get();}
    public void setNumClient(String numero) {numClient.set(numero);}
    public StringProperty numClientP() {return numClient;}



    public String getMail() {return mail.get();}
    public void setMail(String email) {mail.set(email);}
    public StringProperty mailP() {return mail;}

    public String getNumChambr() {return numChambr.get();}
    public void setNumChambr(String num) {numChambr.set(num);}
    public StringProperty numChambrP() {return numChambr;}
}
