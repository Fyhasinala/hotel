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
        loadAllRoom();
    }

    public void loadAllRoom()
    {
        MyButton room = new MyButton("Chambre", "/com/hotel/assets/bed.png");
        MyButton operation = new MyButton("Operation", "/com/hotel/assets/list.png");
        MyButton reservations = new MyButton("Reservations", "/com/hotel/assets/holiday.png");
        MyButton sejours = new MyButton("Sejours", "/com/hotel/assets/stay.png");
        

        room.setOnAction(event -> loadRoom());
        operation.setOnAction(event -> loadOp());
        reservations.setOnAction(event -> loadRe());
        sejours.setOnAction(event -> loadSe());

        navBar.getChildren().addAll(room, operation, reservations, sejours);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/listroom.fxml"));
            Parent homeView = loader.load();
            content.setCenter(homeView);
        }
        catch (IOException ex)
        {
            System.err.println("Failed to load listRoom.fxml view." + ex.getMessage());
            ex.printStackTrace();
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
