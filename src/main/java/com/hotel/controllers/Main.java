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

        MyButton home = new MyButton("Table de bord", "/com/hotel/assets/sakura.png");
        MyButton room = new MyButton("Chambre", "/com/hotel/assets/bed.png");
        MyButton operation = new MyButton("Operation", "/com/hotel/assets/bed.png");
        MyButton reservations = new MyButton("Reservations", "/com/hotel/assets/bed.png");
        MyButton sejours = new MyButton("Sejours", "/com/hotel/assets/bed.png");
        

        home.setOnAction(event -> loadHome());
        room.setOnAction(event -> loadRoom());
        operation.setOnAction(event -> loadOp());
        reservations.setOnAction(event -> loadRe());

        sejours.setOnAction(event -> loadSe());


        navBar.getChildren().addAll(home, room, operation, reservations, sejours);

        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/listroom.fxml"));
          Parent homeView = loader.load();

          content.setCenter(homeView);
        } catch (IOException ex)
        {
        System.err.println("Failed to load listRoom.fxml view." + ex.getMessage());
        ex.printStackTrace();
        }
    }

    private void loadHome()
    {
        try
        {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/com/hotel/controllers/home.fxml"));
            Parent homeView = load.load();
            content.setCenter(homeView);
        }
        catch (IOException ex)
        {
            System.err.println("failure: " + ex.getMessage());
        }
    }

    private void loadRoom()
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
    private void loadOp()
    {
        try
        {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/com/hotel/controllers/formulaire.fxml"));
            Parent roomView = load.load();
            content.setCenter(roomView);
        }
        catch (IOException ex)
        {
            System.err.println("failure: "  + ex.getMessage());
        }
    }
    private void loadRe()
    {
        try
        {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/com/hotel/controllers/reservations.fxml"));
            Parent roomView = load.load();
            content.setCenter(roomView);
        }
        catch (IOException ex)
        {
            System.err.println("failure: "  + ex.getMessage());
            ex.printStackTrace();
        }
    }
    private void loadSe()
    {
        try
        {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/com/hotel/controllers/sejours.fxml"));
            Parent roomView = load.load();
            content.setCenter(roomView);
        }
        catch (IOException ex)
        {
            System.err.println("failure: "  + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
