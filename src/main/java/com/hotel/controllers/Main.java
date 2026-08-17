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
    private VBox navBar;
    @FXML
    private BorderPane content;

    @FXML
    public void initialize()
    {
        FontLoader.loadAll();

        MyButton room = new MyButton("Chambre", "/com/hotel/assets/bed.png");

        room.setOnAction(event -> loadRoom());

        navBar.getChildren().addAll(room);

        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/listRoom.fxml"));
          Parent roomView = loader.load();

          content.setCenter(roomView);
        }
        catch (IOException ex)
        {
        System.err.println("Failed to load listRoom.fxml view." + ex.getMessage());
        }
    }

    public void loadRoom()
    {
        try
        {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/com/hotel/controllers/listRoom.fxml"));
            Parent roomView = load.load();
            content.setCenter(roomView);
        }
        catch (IOException ex)
        {
            System.err.println("failure: "  + ex.getMessage());
        }
    }
}
