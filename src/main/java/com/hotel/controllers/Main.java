package com.hotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.IOException;


public class Main
{
    @FXML
    public HBox manageRoom;
    @FXML
    private VBox navBar;
    @FXML
    private BorderPane content;

    @FXML
    public void initialize()
    {
        FontLoader.loadAll();

        MyButton home = new MyButton("Table de bord", "/com/hotel/assets/sakura.png");
        MyButton room = new MyButton("Chambre", "/com/hotel/assets/bed.png");

        home.setOnAction(event -> loadHome());
        room.setOnAction(event -> loadRoom());

        navBar.getChildren().addAll(home, room);

        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/room.fxml"));
          Parent roomView = loader.load();

          content.setCenter(roomView);
        } catch (IOException e)
        {
        System.err.println("Failed to load room.fxml view." + e.getMessage());
        }
    }

    private void loadHome()
    {
        VBox homeview = new VBox (new Label("Bienvenue sur le Tableau de Bord"));
        content.setCenter(homeview);
    }

    private void loadRoom()
    {
        try
        {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/com/hotel/controllers/room.fxml"));
            Parent roomView = load.load();
            content.setCenter(roomView);
        }
        catch (IOException e)
        {
            System.err.println("failure: "  + e.getMessage());
        }
    }
}
