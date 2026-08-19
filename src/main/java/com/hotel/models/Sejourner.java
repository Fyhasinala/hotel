package com.hotel.models;

import javafx.beans.property.*;

public class Sejourner {
    
    private final IntegerProperty idsejour;
    private final StringProperty dateEntreeSejour;
    private final IntegerProperty nbrJour;
    private final StringProperty nomClient;
    private final StringProperty telephone;
    private final StringProperty numChambr;
    private final StringProperty origine;

    public Sejourner(int id, String entr, int jours, String cli, String tel, String chambre, String or)
    {
        idsejour = new SimpleIntegerProperty(id);
        dateEntreeSejour = new SimpleStringProperty(entr);
        nbrJour = new SimpleIntegerProperty(jours);
        nomClient = new SimpleStringProperty(cli);
        telephone = new SimpleStringProperty(tel);
        numChambr = new SimpleStringProperty(chambre);
        origine = new SimpleStringProperty(or);

    }

    public int getId() {return idsejour.get();}
    public IntegerProperty idP() {return idsejour;}
    

    public String getDateEntree() {return dateEntreeSejour.get();}
    public StringProperty entreeP() {return dateEntreeSejour;}
    
    public int getJours() {return nbrJour.get();}
    public void setJours(int nbr) {nbrJour.set(nbr);}
    public IntegerProperty nbrJourP() {return nbrJour;}

    public String getNom() {return nomClient.get();}
    public void setNom(String nom) {nomClient.set(nom);}
    public StringProperty nomP() {return nomClient;}

    public String getTel() {return telephone.get();}
    public void setTel(String num) {telephone.set(num);}
    public StringProperty telP() {return telephone;}

    public String getNumChambr() {return numChambr.get();}
    public void setNumChambr(String num) {numChambr.set(num);}
    public StringProperty numChambrP() {return numChambr;}

    public String getOrigine() {return origine.get();}
    public StringProperty origP() {return origine;}


}
