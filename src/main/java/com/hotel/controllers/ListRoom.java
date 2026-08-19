package com.hotel.controllers;

import com.hotel.databases.CardRoom;
import com.hotel.databases.Result;
import com.hotel.utilities.MutatingButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;

public class ListRoom
{
    @FXML public HBox footer;
    @FXML public BorderPane centerPane;
    @FXML private ScrollPane panelList;
    @FXML private FlowPane room;
    @FXML private ComboBox<String> sort;

    private Rooms selectedCard = null;
    private MutatingButton add;
    private MutatingButton modify;
    private MutatingButton delete;

    public void initialize()
    {
        initializeAllButtons();
        sort.getItems().setAll("Tous", "Libre", "Occuper");
        sort.setValue("Tous");
        showRoom();

        fillRoom("Tous");
        sort.setOnAction(event ->
        {
            String filter = sort.getValue();
            fillRoom(filter);
        });
    }

    private void initializeAllButtons()
    {
        add = new MutatingButton("/com/hotel/assets/add.png")
        {
            @Override
            protected void handleButtonClick()
            {
                sort.setVisible(false);
                sort.setManaged(false);
                centerPane.setCenter(null);
                NewRoom activeFormInstance = new NewRoom(ListRoom.this);
                centerPane.setCenter(activeFormInstance);
                footer.setVisible(false);
                footer.setManaged(false);
            }
        };

        modify = new MutatingButton("/com/hotel/assets/modify.png")
        {
            @Override
            protected void handleButtonClick()
            {
                if (selectedCard == null)
                {
                    PopupMessage.showInfo("Reminder", "Veuillez d'abord sélectionner une chambre à modifier.");
                    return;
                }

                sort.setVisible(false);
                sort.setManaged(false);
                centerPane.setCenter(null);
                // Pre-populates your editing view directly via your card reference string
                ModifyRoom activeFormInstance = new ModifyRoom(ListRoom.this, selectedCard.getNumber());
                centerPane.setCenter(activeFormInstance);
                footer.setVisible(false);
                footer.setManaged(false);
            }
        };

        delete = new MutatingButton("/com/hotel/assets/delete.png")
        {
            @Override
            protected void handleButtonClick()
            {
                // 🔥 FIX 1: Explicitly HALT execution here if no selection card reference is trackable
                if (selectedCard == null)
                {
                    PopupMessage.showInfo("Reminder", "Veuillez d'abord sélectionner une chambre à supprimer.");
                    return;
                }

                // Wrap popup inside Platform.runLater to prevent animation execution timeline blocks
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Suppression de la chambre N° " + selectedCard.getNumber());
                    alert.setContentText("Êtes-vous sûr de vouloir supprimer définitivement cette chambre ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK)
                    {
                        CardRoom roomObj = new CardRoom(selectedCard.getNumber());
                        Result status = roomObj.destroyRoom();

                        if (status.status())
                        {
                            PopupMessage.showInfo("Success", status.message());
                            showRoom(); // Instantly clears selection tracking maps and pulls active database records
                        }
                        else
                        {
                            PopupMessage.showError("Warning", status.message());
                        }
                    }
                });
            }
        };
    }

    public void showRoom()
    {
        centerPane.setCenter(panelList);

        sort.setManaged(true);
        sort.setVisible(true);

        footer.setManaged(true);
        footer.setVisible(true);
        footer.getChildren().clear();
        footer.getChildren().addAll(add, modify, delete);

        selectedCard = null; // Cleanly resets single-target selections whenever view redraw swaps execute

        fillRoom(sort.getValue() != null ? sort.getValue() : "Tous");
    }

    public void fillRoom(String filter)
    {
        room.getChildren().clear();
        room.setHgap(10);
        room.setVgap(10);

        List<CardRoom> roomList = CardRoom.listAllRooms(filter);

        for (CardRoom roomItem : roomList)
        {
            String num = roomItem.getRoomNumber();
            String status = roomItem.getStatus();
            String design = roomItem.getRoomType();
            int price = roomItem.getRoomPrice();
            String type = roomItem.getRoomDesign();

            boolean matchesAll = filter.equals("Tous");
            boolean matchesLibre = filter.equals("Libre") && status.equals("L");
            boolean matchesOccuper = filter.equals("Occuper") && status.equals("O");

            if (matchesAll || matchesLibre || matchesOccuper)
            {
                Rooms card = new Rooms(num, status, design, price, type);

                card.setOnMouseClicked(event ->
                {
                    // Remove border style from previous card reference tracking instances
                    if (selectedCard != null && selectedCard != card)
                    {
                        selectedCard.getStyleClass().remove("selected-card");
                    }

                    selectedCard = card;

                    // 🔥 FIX 2: Explicitly apply the style border accent class so it lights up on screen!
                    if (!selectedCard.getStyleClass().contains("selected-card")) {
                        selectedCard.getStyleClass().add("selected-card");
                    }
                });

                room.getChildren().add(card);
            }
        }
    }
}
