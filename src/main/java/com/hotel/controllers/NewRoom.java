package com.hotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class NewRoom extends VBox
{
    @FXML private TextField roomNumber;
    @FXML private ComboBox<String> design;
    @FXML private TextField price;

    private final ListRoom parent;

    public NewRoom(ListRoom parent)
    {
        this.parent = parent;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/newRoom.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        } catch (IOException ex)
        {
            throw new RuntimeException("Failed to load newRoom layout template", ex);
        }
    }

    public String getRoomNumber()
    {
        return roomNumber != null ? roomNumber.getText().trim() : "";
    }

    public String getDesign()
    {
        return design != null ? design.getValue() : null;
    }

    public String getPrice()
    {
        return price != null ? price.getText().trim() : "0";
    }

    @FXML
    public void initialize()
    {
        design.setOnAction(event -> showPrice());
    }

    public void showPrice()
    {
        String selectedDesign = design.getValue();
        if (selectedDesign == null) {
            price.setText("0");
            return;
        }

        int calculatedPrice = switch (selectedDesign)
        {
            case "STANDARD" -> 10000;
            case "LUXE" -> 20000;
            case "SUITE" -> 25000;
            default -> 0;
        };
        price.setText(String.valueOf(calculatedPrice));
    }
}
