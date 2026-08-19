package com.hotel.controllers;

import java.io.IOException;

import com.hotel.databases.Vatis;
import com.hotel.databases.VatisGateway;
import javafx.fxml.FXML;
import com.hotel.models.Reserver;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.scene.control.cell.DatePickerTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.event.ActionEvent;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Reservations {
    
    @FXML
    private TableView<Reserver> resTable;

    @FXML
    private TableColumn<Reserver, Integer> id;

    @FXML
    private TableColumn<Reserver, String> cham;

    @FXML 
    private TableColumn<Reserver, String> dateres;

    @FXML
    private TableColumn<Reserver, String> entree;

     @FXML
    private TableColumn<Reserver, String> nom;

    @FXML
    private TableColumn<Reserver, String> cl;

    @FXML
    private TableColumn<Reserver, String> email;

    @FXML 
    private TableColumn<Reserver, Integer> dure;


    @FXML
    private void initialize()
    {
        Vatis.prepare();
        id.setCellValueFactory(c -> c.getValue().idresP().asObject());
        cham.setCellValueFactory(c -> c.getValue().numChambrP());
        dateres.setCellValueFactory(c -> c.getValue().dateReservP());
        entree.setCellValueFactory(c -> c.getValue().dateEntreeP());
         nom.setCellValueFactory(c -> c.getValue().nomClientP());
        cl.setCellValueFactory(c -> c.getValue().numClientP());
        email.setCellValueFactory(c -> c.getValue().mailP());
        dure.setCellValueFactory(c -> c.getValue().nbrJourP().asObject());

        resTable.setEditable(true);
        
        entree.setCellFactory(TextFieldTableCell.forTableColumn());
        //nom.setCellFactory(TextFieldTableCell.forTableColumn());
        cl.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        dure.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        entree.setOnEditCommit(e -> {
            Reserver r = e.getRowValue();
            r.setDateEntree(e.getNewValue());
            maj(r);
        });
        /*nom.setOnEditCommit(e -> {
            Reserver r = e.getRowValue();
            r.setNomClient(e.getNewValue());
            maj(r);
        });*/
        cl.setOnEditCommit(e -> {
            Reserver r = e.getRowValue();
            r.setNumClient(e.getNewValue());
            maj(r);
        });
        email.setOnEditCommit(e -> {
            Reserver r = e.getRowValue();
            r.setMail(e.getNewValue());
            maj(r);
        });
        dure.setOnEditCommit(e -> {
            Reserver r = e.getRowValue();
            r.setNbr(e.getNewValue());
            maj(r);
        });

        resTable.setItems(donnees());
    }

    private ObservableList<Reserver> donnees()
    {
        ObservableList<Reserver> liste = FXCollections.observableArrayList();

        String requete = "SELECT r.idreserv, r.dateReserv, r.dateEntree, r.nbrJour, r.nomClient, r.numClient, r.mail, r.numChambr FROM reserver r" +
        " WHERE NOT EXISTS ( SELECT 1 FROM occuper o WHERE o.idreserv = r.idreserv) ORDER BY idreserv";

       VatisGateway connex = Vatis.prepare();
        try(Connection co = connex.ready();
            Statement st = co.createStatement();
            ResultSet r = st.executeQuery(requete))
            {
                while(r.next()){
                    liste.add(new Reserver(
                        r.getInt("idreserv"),
                        r.getString("dateReserv"),
                        r.getString("dateEntree"),
                        r.getInt("nbrJour"),
                        r.getString("nomClient"),
                        r.getString("numClient"),
                        r.getString("mail"),
                         r.getString("numChambr")
                    ));
                }
            }catch(SQLException er){
                er.printStackTrace();
            }
            return liste;
    }
    private void maj(Reserver a)
    {
        String req = "UPDATE reserver SET dateEntree = ?, nbrJour = ?, numClient = ?, mail = ? WHERE idreserv = ?";

        VatisGateway v = Vatis.prepare();
        try(Connection co = v.ready();
        PreparedStatement prep = co.prepareStatement(req))
        {
            prep.setString(1, a.getDateEntree());
            prep.setInt(2, a.getNbr());
            prep.setString(3, a.getNumClient());
            prep.setString(4, a.getMail());
            prep.setInt(5, a.getId());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void popup() throws IOException {
        Reserver selected = resTable.getSelectionModel().getSelectedItem();
         if(selected !=null)
        {
            try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/modifRes.fxml"));
            Parent root = loader.load();

                
            Stage pop = new Stage();
            pop.initModality(Modality.APPLICATION_MODAL);
            pop.initOwner(resTable.getScene().getWindow());
            pop.setTitle("Modifier reservation");
            pop.setScene(new Scene(root));

            pop.showAndWait();

            resTable.refresh();
            }catch(IOException e){
                System.err.println("ERREUR DE CHARGEMENT 167 RESERVATIONS");
                e.printStackTrace();
            }
            


        } else {
             Alert alerte = new Alert(AlertType.WARNING);
            alerte.setTitle("Selection manquante");
            alerte.setHeaderText(null);
            alerte.setContentText("Veuillez selectionner une ligne");
            alerte.showAndWait();

        }
    }
    @FXML
    private void occup(ActionEvent a)
    {
         Reserver selected = resTable.getSelectionModel().getSelectedItem();
         if(selected !=null)
        {
            Alert al = new Alert(AlertType.CONFIRMATION);
            al.setTitle("Commencer");
            al.setHeaderText(null);
            int numero = selected.getId();
            al.setContentText("Commencer les sejour correspondant à la réservation " + numero + " ?");

            Optional<ButtonType> reponse = al.showAndWait();

            if(reponse.isPresent() && reponse.get() == ButtonType.OK)
            {
                try{
                  occuper(selected.getId(), selected.getNumChambr(), selected.getNbr());

                  resTable.getItems().remove(selected);
                }catch(SQLException e)
                {
                    System.err.println("ERREUR DE SELECTION");
                    e.printStackTrace();
                }
            }
        } else {
             Alert alerte = new Alert(AlertType.WARNING);
            alerte.setTitle("Selection manquante");
            alerte.setHeaderText(null);
            alerte.setContentText("Veuillez selectionner une ligne");
            alerte.showAndWait();

        }
    }

    private void occuper(int id, String cham, int jours) throws SQLException
    {
        String requete = "INSERT INTO occuper(idreserv) VALUES(?)";
        int prix = 0;
        int total = 0;
        VatisGateway v = Vatis.prepare();
        String trouver = "SELECT prixNuite FROM chambre WHERE numChambr = ? ";

        try(Connection co = v.ready();
    PreparedStatement p = co.prepareStatement(trouver)){

        p.setString(1, cham);
        try(ResultSet r = p.executeQuery()){
            if(r.next()){
                prix = r.getInt("prixNuite");
            }
        }
        total = prix * jours;
    }catch(SQLException e)
        {
            System.err.println("Erreur lors de l'occupation");
            e.printStackTrace();
        }

        String ajout = "INSERT INTO SOLDE(id, soldeActuel) VALUES (0, ?) ";
        System.out.println("TOTAL CALCULE = " + total);
         try(Connection conn = v.ready();
        PreparedStatement prep = conn.prepareStatement(ajout))
        {
            prep.setInt(1, total);
            prep.executeUpdate();
        }catch(SQLException e)
        {
            System.err.println("Erreur lors de l'occupation");
            e.printStackTrace();
        }


        try(Connection conn = v.ready();
        PreparedStatement prep = conn.prepareStatement(requete))
        {
            prep.setInt(1, id);
            prep.executeUpdate();
        }catch(SQLException e)
        {
            System.err.println("Erreur lors de l'occupation");
            e.printStackTrace();
        }

    }

    @FXML
    private void suppr(ActionEvent a)
    {
        Reserver selected = resTable.getSelectionModel().getSelectedItem();

        if(selected !=null)
        {
            Alert al = new Alert(AlertType.CONFIRMATION);
            al.setTitle("Annuler");
            al.setHeaderText(null);
            int numero = selected.getId();
            al.setContentText("Annuler la réservation " + numero + " ?");

            Optional<ButtonType> reponse = al.showAndWait();

            if(reponse.isPresent() && reponse.get() == ButtonType.OK)
            {
                try{
                  annuler(selected.getId());

                  resTable.getItems().remove(selected);
                }catch(SQLException e)
                {
                    System.err.println("ERREUR DE SELECTION");
                    e.printStackTrace();
                }
            }
        } else {
             Alert alerte = new Alert(AlertType.WARNING);
            alerte.setTitle("Selection manquante");
            alerte.setHeaderText(null);
            alerte.setContentText("Veuillez selectionner une ligne avant de supprimer");
            alerte.showAndWait();

        }
    }

    private void annuler(int id) throws SQLException
    {
        String requete = "DELETE FROM reserver WHERE idreserv = ?";

        VatisGateway v = Vatis.prepare();

        try(Connection conn = v.ready();
        PreparedStatement prep = conn.prepareStatement(requete))
        {
            prep.setInt(1, id);
            prep.executeUpdate();
        }catch(SQLException e)
        {
            System.err.println("Erreur lors de la suppression");
            e.printStackTrace();
        }
    }
}
