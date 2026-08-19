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

public class NewRoom extends VBox
{
    @FXML private TextField roomNumber;
    @FXML private ComboBox<String> design;
    @FXML private HBox popupFooter;
    @FXML private TextField price;
    @FXML private ComboBox<String> type;

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
    public String getType() { return price != null ? type.getValue() : null; }

    @FXML
    public void initialize()
    {
        design.setOnAction(event -> showPrice());
        type.setOnAction(event -> showPrice());
    }

    public void showPrice() {
        String selectedDesign = design.getValue();
        String selectedType = type.getValue(); // assuming your second ComboBox is called type

        // 1. Guard against unselected inputs
        if (selectedDesign == null || selectedType == null) {
            price.setText("0");
            return;
        }

        // 2. Fetch Base Design Pricing smoothly
        int basePrice = switch (selectedDesign) {
            case "STANDARD" -> 10000;
            case "CONFORT"  -> 12000;
            case "DELUXE"   -> 15000;
            case "FAMILIAL" -> 17000;
            case "LUXE"     -> 20000;
            case "SUITE"     -> 25000;
            default         -> 0;
        };

        // 3. Fetch Type Premium Addon cleanly
        int typeAddon = switch (selectedType) {
            case "SIMPLE"    -> 0;
            case "DOUBLE"    -> 5000;
            case "TWIN"      -> 25000;
            case "TRIPLE"    -> 10000;
            case "QUADRUPLE" -> 12000;
            default          -> -1; // Flag invalid input values
        };

        // 4. Render calculations to UI interface layers safely
        if (basePrice == 0 || typeAddon == -1) {
            price.setText("0");
        } else {
            int finalPrice = basePrice + typeAddon;
            price.setText(String.valueOf(finalPrice));
        }
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
                String typeValue = getType();
                int priceValue = 0;

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
                CardRoom room = new CardRoom(roomNumValue, designValue, priceValue, typeValue);
                Result result = room.buildRoom();
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
