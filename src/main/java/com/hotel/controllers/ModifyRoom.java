package com.hotel.controllers;

import com.hotel.databases.CardRoom;
import com.hotel.databases.Result;
import com.hotel.utilities.MutatingButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class ModifyRoom extends VBox
{
    @FXML private TextField roomNumber;
    @FXML private ComboBox<String> design;
    @FXML private HBox popupFooter;
    @FXML private TextField price;
    @FXML private ComboBox<String> type;

    private final ListRoom parent;

    public ModifyRoom(ListRoom parent, String number)
    {
        this.parent = parent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/controllers/modifyRoom.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        } catch (IOException ex)
        {
            throw new RuntimeException("Failed to load newRoom layout template", ex);
        }
        roomNumber.setText(number);
        setupActionButtons();
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
    public String getType() { return type != null ? type.getValue() : null; }

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

    private void setupActionButtons()
    {
        MutatingButton confirm = new MutatingButton("/com/hotel/assets/confirm.png")
        {
            @Override
            protected void handleButtonClick()
            {
                String roomNumValue = getRoomNumber();
                String designValue = getDesign();
                String rawValue = getPrice();
                int priceValue = 0;
                String roomType = getType();

                if (roomNumValue.isEmpty() || designValue == null || rawValue.isEmpty())
                {
                    PopupMessage.showError("Warning",  "Veuillez remplir tous les champs");
                    return;
                }
                try
                {
                    priceValue = Integer.parseInt(rawValue);
                }
                catch (NumberFormatException ex)
                {
                    System.err.println("Price string parsing failed: " + ex.getMessage());
                }
                CardRoom room = new CardRoom(roomNumValue, designValue, priceValue, roomType);
                Result result = room.upgradeRoom();
                if (result.status())
                {
                    PopupMessage.showInfo("Success", result.message());
                    parent.showRoom();
                } else
                {
                    PopupMessage.showError("Warning", result.message());
                }
            }
        };

        MutatingButton cancel = new MutatingButton("/com/hotel/assets/cancel.png") {
            @Override
            protected void handleButtonClick()
            {
                parent.showRoom();
            }
        };
        popupFooter.setSpacing(100);
        popupFooter.getChildren().addAll(confirm, cancel);


    }
}
