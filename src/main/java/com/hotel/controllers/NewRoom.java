package com.hotel.controllers;

import com.hotel.utilities.MutatingButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewRoom extends VBox
{
    @FXML
    private TextField roomNumber;
    @FXML
    private ComboBox<String> design;
    @FXML
    private TextField price;
    @FXML
    private HBox confirm;

    public NewRoom(ListRoom parent)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/newRoom.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Failed to load newRoom FXML layout", ex);
        }
    }

    @FXML
    public void initialize()
    {
        design.setOnAction(event -> ShowPrice());

        MutatingButton saveButton = new MutatingButton("/com/hotel/assets/check.png")
        {
            @Override
            protected void handleButtonClick()
            {
                String num = roomNumber.getText();
                String tier = design.getValue();
                String prc = price.getText();


            }
        };

        // Append only the save button into your local confirm pane container block
        confirm.getChildren().add(saveButton);
    }

    public void ShowPrice()
    {
        String selectedDesign = design.getValue();

        if (selectedDesign == null)
        {
            price.setText("0");
            return;
        }

        try
        {
            int Price = 0;

            switch (selectedDesign)
            {
                case "STANDARD":
                    Price += 10000;
                    break;
                case "LUXE":
                    Price += 20000;
                    break;
                case "SUITE":
                    Price = 25000;
                    break;
            }
            price.setText(String.format("%d", Price));

        }
        catch (NumberFormatException ex)
        {
            price.setText("0");
        }
    }
}
