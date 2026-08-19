package com.hotel.controllers;


import com.hotel.databases.Vatis;
import com.hotel.databases.VatisGateway;
import javafx.fxml.FXML;
import com.hotel.models.Chambremo;
import com.hotel.models.Sejourner;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.*;
import java.util.Locale;


public class UpSej {
    @FXML
    private ToggleGroup groupe;
    @FXML
    private RadioButton reservRadio;
    @FXML
    private RadioButton sejourRadio;
    @FXML
    private DatePicker dateEntreePicker;
    @FXML
    private Spinner<Integer> dureeSpinner;
    @FXML
    private ComboBox<String> desCombo;
    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private Label finLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label origineLabel;

    @FXML
    private TableView<Chambremo> chamTab;

    @FXML
    private TableColumn<Chambremo, String> numCol;
    @FXML
    private TableColumn<Chambremo, String> desCol;
    @FXML
    private TableColumn<Chambremo, String> typeCol;
    @FXML
    private TableColumn<Chambremo, Integer> prixCol;

    @FXML
    private HBox zoneChambre;
    @FXML
    private VBox chambres;
    @FXML
    private TextField champChoisie;
    @FXML
    private VBox clientBloc;
    @FXML 
    private TextField nomChamp;
    @FXML
    private TextField telChamp;
    /*@FXML
    private TextField mailChamp;*/

    private Chambremo choisie = null;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd / MM / yyyy");

    private Sejourner ligneRes;

    private Runnable onActionSuccess;

    public void initData(Sejourner res, String origine, Runnable onSuccess)
    {
        this.ligneRes = res;

    this.onActionSuccess = onSuccess; 
    
       
        dureeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 90, 1));

        LocalDate entree = LocalDate.parse(res.getDateEntree());
        dateEntreePicker.setValue(entree);
        dureeSpinner.getValueFactory().setValue(res.getJours());
        nomChamp.setText(res.getNom());
        telChamp.setText(res.getTel());
        //mailChamp.setText(res.getMail());
        champChoisie.setText(res.getNumChambr());
        idLabel.setText(String.valueOf(res.getId()));
        origineLabel.setText("(" + origine + ")");
       // desCombo.setValue(res.get)
       desCombo.getItems().addAll("STANDARD", "CONFORT", "DELUXE", "FAMILIAL", "LUXE", "SUITE");
        desCombo.setValue("CONFORT");
        typeCombo.getItems().addAll("SIMPLE", "DOUBLE", "TWIN", "TRIPLE", "QUADRUPLE");
        typeCombo.setValue("SIMPLE");

         numCol.setCellValueFactory(d -> d.getValue().numP());
        desCol.setCellValueFactory(d -> d.getValue().desP());
        typeCol.setCellValueFactory(d -> d.getValue().typeP());
        prixCol.setCellValueFactory(d -> d.getValue().prixP().asObject());
        
        

        dateEntreePicker.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );
        dureeSpinner.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );

        desCombo.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );
        // 1. Filtrer le Nom : Autoriser lettres, espaces, tirets et apostrophes (Max 50 caractères)
nomChamp.textProperty().addListener((observable, oldValue, newValue) -> {
    if (!newValue.matches("[a-zA-ZÀ-ÿ\\s'-]*")) {
        nomChamp.setText(oldValue);
    }
    if (newValue.length() > 50) {
        nomChamp.setText(oldValue);
    }
});

// 2. Filtrer le Téléphone : Autoriser uniquement les chiffres et le caractère + (Max 15 caractères)
telChamp.textProperty().addListener((observable, oldValue, newValue) -> {
    if (!newValue.matches("[0-9+]*")) {
        telChamp.setText(oldValue);
    }
    if (newValue.length() > 15) {
        telChamp.setText(oldValue);
    }
});


        typeCombo.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );

        chamTab.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if(newSel != null){
                verrou(newSel);
                finetprix();
        chambreDispo();

            }
        });

        finetprix();
        chambreDispo();

    }

        
    private void finetprix(){
        LocalDate dateEntree = dateEntreePicker.getValue();
        int duree = dureeSpinner.getValue();

        if(dateEntree != null)
        {
            LocalDate dateFin = dateEntree.plusDays(duree);
            finLabel.setText(dateFin.format(formatter));

            if(choisie != null){
                int cout = duree * (int) choisie.getPrix();
                int prix = (int) choisie.getPrix();
                prixLabel.setText(String.format(Locale.FRANCE, "%,d Ar (%d nuits x %,d Ar)", cout, duree, prix));

            } else {
                prixLabel.setText(" 0 Ar");
            }
        }
    }

    
    private void chambreDispo()
    {
        chamTab.getItems().clear();

        /*chamTab.getItems().add(new Chambremo("C101", "Standard", "Twin", 150000));
        chamTab.getItems().add(new Chambremo("C105", "Luxe", "Simple", 200000));*/

        LocalDate debut = dateEntreePicker.getValue();
        int nbr = dureeSpinner.getValue();
        LocalDate fin = debut.plusDays(nbr);

        String desFil = desCombo.getValue();
        String typeFil = typeCombo.getValue();
        System.out.println("desFIL ;" + desFil);

        String requete = "SELECT numChambr, Design, Type, prixNuite FROM chambre c " +
                "WHERE c.Design = ? AND c.Type = ? " +
                "AND NOT EXISTS (" +
                "   SELECT 1 FROM reserver r WHERE r.numChambr = c.numChambr AND r.dateEntree < ? AND (r.dateEntree + r.nbrJour) > ? " +
                "   UNION " +
                "   SELECT 1 FROM sejourner s WHERE s.numChambr = c.numChambr AND s.dateEntreeSejour < ? AND (s.dateEntreeSejour + s.nbrJour) > ? " +
                "   ) ORDER BY numChambr";

        VatisGateway v = Vatis.prepare();

        try(Connection co = v.ready();
        PreparedStatement prep = co.prepareStatement(requete))
        {
            java.sql.Date sqlDebut = java.sql.Date.valueOf(debut);
             java.sql.Date sqlFin = java.sql.Date.valueOf(fin);

             prep.setString(1, desFil);
             prep.setString(2, typeFil);

             prep.setDate(3, sqlFin);
             prep.setDate(4, sqlDebut);

             prep.setDate(5, sqlFin);
             prep.setDate(6, sqlDebut);


             ResultSet res = prep.executeQuery();
             System.out.println("BASE TROOUVEEEE " + res);

             while(res.next()){
                System.out.println("CHAMBRE TROUVEE");
                String numero = res.getString("numChambr");
                String design = res.getString("Design");
                String typec = res.getString("Type");
                int prixc = res.getInt("prixNuite");

                Chambremo chamb = new Chambremo(numero, design, typec, prixc);
                System.out.println("CHAM" + chamb);
                chamTab.getItems().add(chamb);
             }
             if(!res.next()){
                System.err.println("AUCUNE LIGNE TROUVEE");
             }
             
             
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("ERREUR LORS DE LA RECHERCHE");
        }

                

    }

    private void verrou(Chambremo ch)
    {
        this.choisie = ch;
        champChoisie.setText("N°" + ch.getNum() + "-" + ch.getDes() + "-" + ch.getType());

        finetprix();

        chamTab.setVisible(false);
        chamTab.setManaged(false);
        chambres.setVisible(false);
        chambres.setManaged(false);
        zoneChambre.setVisible(true);
        zoneChambre.setManaged(true);
        clientBloc.setVisible(true);
        clientBloc.setManaged(true);

    }

    @FXML
    private void modifierChoix()
    {
        this.choisie = null;
        chamTab.getSelectionModel().clearSelection();
        finetprix();

        /*clientBloc.setVisible(false);
        clientBloc.setManaged(false);*/
        zoneChambre.setVisible(false);
        zoneChambre.setManaged(false);
        chamTab.setVisible(true);
        chamTab.setManaged(true);
        chambres.setVisible(true);
        chambres.setManaged(true);

             finetprix();
        chambreDispo();
       
    }


    @FXML
    private void valider(){
         String nom = nomChamp.getText().trim();
    String tel = telChamp.getText().trim();
   // String mail = mailChamp.getText().trim();

    // 1. Validation de l'intégrité du NOM
    if (nom.isEmpty()) {
        new Alert(Alert.AlertType.WARNING, "Veuillez saisir le nom du client.").showAndWait();
        return;
    }
    if (nom.length() < 2) {
        new Alert(Alert.AlertType.WARNING, "Le nom du client doit contenir au moins 2 caractères.").showAndWait();
        return;
    }

    // 2. Validation de l'intégrité du TÉLÉPHONE
    if (tel.isEmpty()) {
        new Alert(Alert.AlertType.WARNING, "Veuillez saisir le numéro de téléphone.").showAndWait();
        return;
    }
    // Format attendu : Optionnel (+), suivi de 8 à 14 chiffres (ex: 0341234567 ou +261341234567)
    if (!tel.matches("^\\+?[0-9]{8,14}$")) {
        new Alert(Alert.AlertType.WARNING, "Le numéro de téléphone est invalide.\nFormat attendu : Uniquement des chiffres (entre 8 et 14).").showAndWait();
        return;
    }

    // 3. Validation de l'intégrité du MAIL
    
    // Norme RFC 5322 simplifiée pour valider la structure d'une adresse emai

    

    // 4. Sécurité Chambre
    if (choisie == null) {
        new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une chambre.").showAndWait();
        return;
    }
        /*if(mailChamp.getText().trim().isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Veuillez saisir le nom du client").showAndWait();
            return;
        }*/
        //String opChoix = ((RadioButton) groupe.getSelectedToggle()).getText();
        //String table = opChoix.equals("Reservation") ? "reserver" : "sejourner";
        String table = (origineLabel.getText()).equals("(Reservation)") ? "reserver" : "sejourner";
        java.sql.Date debut = java.sql.Date.valueOf(dateEntreePicker.getValue());
        int nbr = dureeSpinner.getValue();
        /*String nom = nomChamp.getText();
        String tel = telChamp.getText();*/
        //String mail = mailChamp.getText();
        //String chambre = champChoisie.getText();
        String chambre = choisie.getNum();

        int id = Integer.parseInt(idLabel.getText());
        System.out.println("ID" + id);
        System.out.println("TABLE : " + table);
        

        VatisGateway v = Vatis.prepare();

        PreparedStatement prep = null;
        try{
            Connection co = v.ready();
                if(table.equals("reserver")){
               String req = "UPDATE reserver SET dateEntree = ?, nbrJour = ?, nomClient = ?, numClient = ?, numChambr = ? " +
                " WHERE idreserv = (SELECT idreserv FROM occuper WHERE idOccup = ? ) ";
                //String req = "SELECT idreserv FROM occuper WHERE idOccup = ?";

               prep = co.prepareStatement(req);
                prep.setDate(1, debut);
                prep.setInt(2, nbr);
                prep.setString(3, nom);
                prep.setString(4, tel);
                //prep.setString(5, mail);
                prep.setString(5, chambre);
                prep.setInt(6, id);
           
            } else {
                String req = "UPDATE sejourner SET nbrJour = ?, nomClient = ?, telephone = ?, numChambr = ? " +
                " WHERE idsejour = ? ";

                prep = co.prepareStatement(req);
                
                prep.setInt(1, nbr);
                prep.setString(2, nom);
                prep.setString(3, tel);
                prep.setString(4, chambre);
                prep.setInt(5, id);


            }

            prep.executeUpdate();
            System.out.println("Enregistrement reussi EHEHHHHEHHEHEHEHHEHE   ");
             new Alert(Alert.AlertType.INFORMATION, "ENREGISTEMENT EFFECTUE").showAndWait();

              //Event.fireEvent(idLabel.getScene().getWindow(), new Event(Event.ANY));
              if (onActionSuccess != null) {
            onActionSuccess.run();
        }
             
             idLabel.getScene().getWindow().hide();
            
             
            
 
            
              /* String req = "UPDATE sejourner SET nbrJour = ?, nomClient = ?, telephone = ?, numChambr = ? " +
                "WHERE idsejour = ?";

                prep = co.prepareStatement(req);
                
                prep.setInt(1, nbr);
                prep.setString(2, nom);
                prep.setString(3, tel);
                prep.setString(4, chambre);
                prep.setInt(4, id);

            

            prep.executeUpdate();
            System.out.println("Enregistrement reussi");
             new Alert(Alert.AlertType.INFORMATION, "ENREGISTEMENT EFFECTUE").showAndWait();*/
            

            chambreDispo();
            nomChamp.clear();
            telChamp.clear();
            //mailChamp.clear();


        }catch(SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "ERREUR D'ENREGISTREMENT POSTGRE").showAndWait();
            
        }/*finally {
            try {if (prep !=null) prep.close();} catch (SQLException er) {}
            
        }*/
    }

}
