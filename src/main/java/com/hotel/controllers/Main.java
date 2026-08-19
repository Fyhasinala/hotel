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

        MyButton home = new MyButton("Table de bord", "/com/hotel/assets/sakura.png");
        MyButton room = new MyButton("Chambre", "/com/hotel/assets/bed.png");

        room.setOnAction(event -> loadRoom());

        navBar.getChildren().addAll(home, room);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/home.fxml"));
            Parent homeView = loader.load();

            content.setCenter(homeView);
        } catch (IOException ex)
        {
            System.err.println("Failed to load listRoom.fxml view." + ex.getMessage());
        }
    }
}