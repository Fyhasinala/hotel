package com.hotel.controllers;

//import java.io.IOException;
import com.hotel.databases.Vatis;
import com.hotel.databases.VatisGateway;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import com.hotel.models.Reserver;
import com.hotel.models.Sejourner;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.event.ActionEvent;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;

public class Sejours {
    
    @FXML
    private TableView<Sejourner> sejoTable;

    @FXML
    private TableColumn<Sejourner, Integer> id;

    @FXML
    private TableColumn<Sejourner, String> ori;

    @FXML
    private TableColumn<Sejourner, String> cham;

    @FXML
    private TableColumn<Sejourner, String> entree;

    @FXML
    private TableColumn<Sejourner, String> client;

    @FXML
    private TableColumn<Sejourner, String> tel;

    @FXML
    private TableColumn<Sejourner, Integer> duree;

    @FXML
    public void initialize()
    {
        Vatis.prepare();
        id.setCellValueFactory(c -> c.getValue().idP().asObject());
        ori.setCellValueFactory(c -> c.getValue().origP());
        cham.setCellValueFactory(c -> c.getValue().numChambrP());
        entree.setCellValueFactory(c -> c.getValue().entreeP());
        client.setCellValueFactory(c -> c.getValue().nomP());
        tel.setCellValueFactory(c -> c.getValue().telP());
        duree.setCellValueFactory(c -> c.getValue().nbrJourP().asObject());

        sejoTable.setEditable(true);

        
        client.setCellFactory(TextFieldTableCell.forTableColumn());
        tel.setCellFactory(TextFieldTableCell.forTableColumn());
        duree.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        client.setOnEditCommit(e -> {
            Sejourner a = e.getRowValue();
            a.setNom(e.getNewValue());
            maj(a);
        });
        tel.setOnEditCommit(e -> {
            Sejourner a = e.getRowValue();
            a.setTel(e.getNewValue());
            maj(a);
        });
        duree.setOnEditCommit(e -> {
            Sejourner a = e.getRowValue();
            a.setJours(e.getNewValue());
            maj(a);
        });

        sejoTable.setItems(donnees());


    }

    private ObservableList<Sejourner> donnees()
    {
        ObservableList<Sejourner> liste = FXCollections.observableArrayList();
        //String requete = "SELECT idsejour, dateEntreeSejour, nbrJour, nomClient, telephone, numChambr FROM sejourner ORDER BY idsejour";
        String requete =
            "SELECT r.dateEntree AS dateEntreeSejour, r.nbrJour, r.nomClient, r.numClient AS telephone, r.numChambr, 'Reservation' AS origine, o.idOccup AS idsejour " +
            "FROM occuper o " + 
            "JOIN reserver r ON o.idreserv = r.idreserv " +
            " UNION ALL " +
            "SELECT s.dateEntreeSejour, s.nbrJour, s.nomClient, s.telephone, s.numChambr, 'Direct' AS origine, s.idsejour AS idsejour " +
            "FROM sejourner s ";

        VatisGateway v = Vatis.prepare();

        try(Connection co = v.ready();
        Statement st = co.createStatement();
        ResultSet r = st.executeQuery(requete))
        {
            while(r.next())
            {
                liste.add(new Sejourner(
                    r.getInt("idsejour"),
                    r.getString("dateEntreeSejour"),
                    r.getInt("nbrJour"),
                    r.getString("nomClient"),
                    r.getString("telephone"),
                    r.getString("numChambr"),
                    r.getString("origine")
                
                ));
            }
        }catch(SQLException e)
        {
            System.err.println("ERREUR DANS 118");
            e.printStackTrace();
        }

        return liste;
    }

    private void maj(Sejourner s)
    {
        String req = "";

        if (s.getOrigine().equals("Direct")) {
            req = "UPDATE sejourner SET nbrJour = ?, nomClient = ?, telephone = ? WHERE idsejour = ?";
        } else if (s.getOrigine().equals("Reservation")){
            req = "UPDATE reserver SET nbrJour = ?, nomClient = ?, numClient = ? WHERE idreserv = ( " +
                "SELECT idreserv FROM occuper WHERE idOccup = ?)";
        }
        

        VatisGateway va = Vatis.prepare();

        try(Connection co = va.ready();
        PreparedStatement prep = co.prepareStatement(req))
        {
            prep.setInt(1, s.getJours());
            prep.setString(2, s.getNom());
            prep.setString(3, s.getTel());
            prep.setInt(4, s.getId());

        }catch(SQLException e)
        {
             System.err.println("ERREUR DANS 149");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void popup() throws IOException {
        Sejourner selected = sejoTable.getSelectionModel().getSelectedItem();
         if(selected !=null)
        {
            try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/modifSej.fxml"));
            Parent root = loader.load();


            UpSej controllerPop = loader.getController();
           controllerPop.initData(selected, ori.getCellData(selected), () -> {
                // Cette partie s'exécutera uniquement si la validation a réussi
                initialize(); // <-- Remplacez par le nom de votre méthode qui lit la base de données
                sejoTable.refresh();       // Optionnel si les items de la table changent complètement
            });

            Stage pop = new Stage();
            pop.initModality(Modality.WINDOW_MODAL);
            pop.initOwner(sejoTable.getScene().getWindow());
            pop.setTitle("Modifier reservation");
            pop.setScene(new Scene(root));
        
            pop.showAndWait();

            sejoTable.refresh();
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
    private void terminer(ActionEvent a)
    {
        Sejourner selected = sejoTable.getSelectionModel().getSelectedItem();

        if(selected != null)
        {
             Alert alerte = new Alert(AlertType.CONFIRMATION);
            alerte.setTitle("Terminer");
            alerte.setHeaderText(null);
            String chambre = selected.getNumChambr();
            int num = selected.getId();
            alerte.setContentText("Terminer le sejour " + num  + "dans la chambre " + chambre +  "?");

            Optional<ButtonType> reponse = alerte.showAndWait();

            if(reponse.isPresent() && reponse.get() == ButtonType.OK)
            {
                try{
                    finish(selected, selected.getId());
                    sejoTable.getItems().remove(selected);
                }catch(SQLException e){
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

    private void finish(Sejourner s, int id) throws SQLException
    {
        String requete = "";

         if (s.getOrigine().equals("Direct")) {
            requete = "DELETE FROM sejourner WHERE idsejour = ?";
        } else if (s.getOrigine().equals("Reservation")){
            requete = "DELETE FROM reserver WHERE idreserv = ( " +
                "SELECT idreserv FROM occuper WHERE idOccup = ?)";
        }
        
        VatisGateway v = Vatis.prepare();

        try(Connection co = v.ready();
        PreparedStatement prep = co.prepareStatement(requete))
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
