package com.hotel.models;

import javafx.beans.property.*;
/*numChambr VARCHAR(3) PRIMARY KEY,
    Design VARCHAR(10) NOT NULL CHECK (Design IN ('STANDARD', 'CONFORT', 'DELUXE', 'FAMILIAL', 'LUXE', 'SUITE')),
    Type VARCHAR(10) NOT NULL CHECK (Type IN ('SIMPLE', 'DOUBLE', 'TWIN', 'TRIPLE', 'QUADRUPLE')),
    prixNuite INT NOT NULL*/


public class Chambremo {
    
    private final StringProperty numChambr;
    private final StringProperty Design;
    private final StringProperty Type;
    private final IntegerProperty prixNuite;

    public Chambremo(String num, String des, String ty, int prix)
    {
        numChambr = new SimpleStringProperty(num);
        Design = new SimpleStringProperty(des);
        Type = new SimpleStringProperty(ty);
        prixNuite = new SimpleIntegerProperty(prix);
    }

    public String getNum() {return numChambr.get();}
    public StringProperty numP() {return numChambr;}

    public String getDes() {return Design.get();}
    public void setDes(String d) {Design.set(d);} 
    public StringProperty desP() {return Design;}

    public String getType() {return Type.get();}
    public void setType(String d) {Type.set(d);} 
    public StringProperty typeP() {return Type;}

    public int getPrix() {return prixNuite.get();}
    public void setPrix(int p) {prixNuite.set(p);} 
    public IntegerProperty prixP() {return prixNuite;}
}
