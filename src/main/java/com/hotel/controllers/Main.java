package com.hotel.controllers;

import com.hotel.utilities.FontLoader;
import com.hotel.utilities.MyButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
