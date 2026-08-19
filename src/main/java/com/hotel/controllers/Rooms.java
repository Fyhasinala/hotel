package com.hotel.controllers;

import com.hotel.utilities.CardHover;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;

public class Rooms extends CardHover
{
    private final String roomNumber;
    @FXML private Label number;
    @FXML private Label status;
    @FXML private Label design;
    @FXML private Label price;
    private Boolean isSelected = false;

    public Rooms(String number, String status, String design, int price)
    {
        this.roomNumber = number;
        FXMLLoader loadRoom = new FXMLLoader(getClass().getResource("rooms.fxml"));
        loadRoom.setRoot(this);
        loadRoom.setController(this);
        try
        {
            loadRoom.load();
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Failed to compile room components", ex);
        }

        if (number != null && !number.trim().isEmpty())
        {
            this.number.setText(number);
            this.status.setText(status);
            this.design.setText(design);
            this.price.setText(String.valueOf(price));
        }
        else
        {
            this.number.setText("noRoom");
            this.status.setText(".");
            this.design.setText("NULL");
            this.price.setText("0");
        }
        this.number.getStyleClass().setAll("label", design);
        this.status.getStyleClass().setAll("label", design);
        this.design.getStyleClass().setAll("label", design);
        this.price.getStyleClass().setAll("label", design);
        this.getStyleClass().setAll("stack-pane", "s-"+design);

        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event ->
        {
            isSelected = !isSelected;

            if (isSelected) {
                this.getStyleClass().add("selected-card");
            } else {
                this.getStyleClass().remove("selected-card");
            }
        });

        ScaleTransition st = new ScaleTransition(Duration.millis(150), this);

        this.setOnMouseEntered(e -> {
            st.setToX(1.05);
            st.setToY(1.05);
            st.playFromStart();
        });

        this.setOnMouseExited(e -> {
            st.setToX(1.0);
            st.setToY(1.0);
            st.playFromStart();
        });
    }

    public String getNumber()
    {
        return roomNumber;
    }
}
