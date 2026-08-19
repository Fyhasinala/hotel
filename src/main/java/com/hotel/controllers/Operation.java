package com.hotel.controllers;

import com.hotel.databases.Vatis;
import java.util.Date;
import com.hotel.databases.VatisGateway;
import javafx.fxml.FXML;
import com.hotel.models.Chambremo;
import javafx.scene.control.*;
import java.sql.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.*;
import java.util.Locale;

import static com.hotel.databases.GeneratePdf.generateReceipt;
import static com.hotel.utilities.MailSender.sendConfirmation;

public class Operation {
    
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
    private TextField champChoisie;
    @FXML
    private VBox clientBloc;
    @FXML 
    private TextField nomChamp;
    @FXML
    private TextField telChamp;
    @FXML
    private TextField mailChamp;

    private Chambremo choisie = null;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd / MM / yyyy");


    //config et autres
    @FXML
    public void initialize(){
        Vatis.prepare();
        dureeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 90, 1));
        dateEntreePicker.setValue(LocalDate.now());

       /*numChambr VARCHAR(3) PRIMARY KEY,
    Design VARCHAR(10) NOT NULL CHECK (Design IN ('STANDARD', 'CONFORT', 'DELUXE', 'FAMILIAL', 'LUXE', 'SUITE')),
    Type VARCHAR(10) NOT NULL CHECK (Type IN ('SIMPLE', 'DOUBLE', 'TWIN', 'TRIPLE', 'QUADRUPLE')),
    prixNuite INT NOT NULL*/


        desCombo.getItems().addAll("STANDARD", "CONFORT", "DELUXE", "FAMILIAL", "LUXE", "SUITE");
        desCombo.setValue("CONFORT");
        typeCombo.getItems().addAll("SIMPLE", "DOUBLE", "TWIN", "TRIPLE", "QUADRUPLE");
        typeCombo.setValue("SIMPLE");

        numCol.setCellValueFactory(d -> d.getValue().numP());
        desCol.setCellValueFactory(d -> d.getValue().desP());
        typeCol.setCellValueFactory(d -> d.getValue().typeP());
        prixCol.setCellValueFactory(d -> d.getValue().prixP().asObject());

        groupe.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if(newToggle == sejourRadio) {
                dateEntreePicker.setValue(LocalDate.now());
                dateEntreePicker.setDisable(true);
            } else if (newToggle == reservRadio) {
                dateEntreePicker.setDisable(false);
            }

            finetprix();
            chambreDispo();
        });

        dateEntreePicker.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );
        dureeSpinner.valueProperty().addListener((o, ov, nv) -> {
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


        desCombo.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );

        typeCombo.valueProperty().addListener((o, ov, nv) -> {
            finetprix();
            chambreDispo();

        } );

        chamTab.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if(newSel != null){
                verrou(newSel);
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
             while(res.next()){
                String numero = res.getString("numChambr");
                String design = res.getString("Design");
                String typec = res.getString("Type");
                int prixc = res.getInt("prixNuite");

                Chambremo chamb = new Chambremo(numero, design, typec, prixc);

                chamTab.getItems().add(chamb);
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

        clientBloc.setVisible(false);
        clientBloc.setManaged(false);
        zoneChambre.setVisible(false);
        zoneChambre.setManaged(false);
        chamTab.setVisible(true);
        chamTab.setManaged(true);
    }
    @FXML
    private void valider(){
        String nom = nomChamp.getText().trim();
    String tel = telChamp.getText().trim();
    String mail = mailChamp.getText().trim();

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
    if (mail.isEmpty()) {
        new Alert(Alert.AlertType.WARNING, "Veuillez saisir l'adresse email.").showAndWait();
        return;
    }
    // Norme RFC 5322 simplifiée pour valider la structure d'une adresse emai

    String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    if (!mail.matches(regexEmail)) {
        new Alert(Alert.AlertType.WARNING, "L'adresse email saisie n'est pas valide (ex: client@domaine.com).").showAndWait();
        return;
    }

    // 4. Sécurité Chambre
    if (choisie == null) {
        new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une chambre.").showAndWait();
        return;
    }
        String opChoix = ((RadioButton) groupe.getSelectedToggle()).getText();
        String table = opChoix.equals("Reservation") ? "reserver" : "sejourner";
        java.sql.Date debut = java.sql.Date.valueOf(dateEntreePicker.getValue());
        int nbr = dureeSpinner.getValue();
       /*  String nom = nomChamp.getText();
        String tel = telChamp.getText();
        String mail = mailChamp.getText();*/
        String chambre = choisie.getNum();

        VatisGateway v = Vatis.prepare();

        PreparedStatement prep = null;
        try( Connection co = v.ready())
        {
            if(table.equals("reserver")){
                String req = "INSERT INTO reserver (dateEntree, nbrJour, nomClient, numClient, mail, numChambr) " +
                "VALUES (?, ?, ?, ?, ?, ?) ";

                prep = co.prepareStatement(req);
                prep.setDate(1, debut);
                prep.setInt(2, nbr);
                prep.setString(3, nom);
                prep.setString(4, tel);
                prep.setString(5, mail);
                prep.setString(6, chambre);
                try (Connection butler = Vatis.prepare().ready();
                     PreparedStatement ticket = butler.prepareStatement("SELECT dateEntreeSejour + (nbrjour * INTERVAL '1day') AS mock, idsejour FROM sejourner WHERE numChambr = ?"))
                {
                    ticket.setString(1, chambre);
                    ResultSet set = ticket.executeQuery();
                    if (set.next())
                    {
                        java.sql.Date date = set.getDate("mock");
                        String id = set.getString("idsejour");
                        generateReceipt("Sejour-"+id, id,new Date(), date, nom, chambre);
                    }
                }
            } else {
                String req = "INSERT INTO sejourner (nbrJour, nomClient, telephone, numChambr) " +
                "VALUES (?, ?, ?, ?) ";

                prep = co.prepareStatement(req);

                prep.setInt(1, nbr);
                prep.setString(2, nom);
                prep.setString(3, tel);
                prep.setString(4, chambre);

            }

            prep.executeUpdate();
            System.out.println("Enregistrement reussi");
             new Alert(Alert.AlertType.INFORMATION, "ENREGISTEMENT EFFECTUE").showAndWait();
             Date date = new Date();
             String id;

            chambreDispo();
            nomChamp.clear();
            telChamp.clear();
            mailChamp.clear();

            sendConfirmation(chambre, debut, nbr, mail);

        }catch(SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "ERREUR D'ENREGISTREMENT POSTGRE").showAndWait();
            
        }finally {
            try {if (prep !=null) prep.close();} catch (SQLException er) {}
            
        }
        
    }






}
