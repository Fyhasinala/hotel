package com.hotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Room extends Button
{
    @FXML private Label room_number;
    @FXML private Label status;

    public Room(String room_number, String status)
    {
        URL fxmlUrl = getClass().getResource("room.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try
        {
            fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to compile room components", ex);
        }

        if ( (room_number != null && !room_number.trim().isEmpty()) && (status != null && !status.trim().isEmpty()) )
        {
            this.room_number.setText(room_number);
            this.status.setText(status);
        }
        else
        {
            this.room_number.setText(" ");
            this.status.setText(" ");
        }
    }
}