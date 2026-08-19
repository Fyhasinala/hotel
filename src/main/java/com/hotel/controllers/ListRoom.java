package com.hotel.controllers;

import com.hotel.databases.CardRoom;
import com.hotel.databases.Result;
import com.hotel.utilities.MutatingButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import javax.swing.plaf.PopupMenuUI;
import java.util.List;
import java.util.Optional;

public class ListRoom
{
    @FXML
    public HBox footer;
    @FXML
    public BorderPane centerPane;
    @FXML
    private ScrollPane panelList;
    @FXML
    private FlowPane room;
    @FXML
    private ComboBox<String> sort;

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
                sort.setOnMouseClicked(event -> selectedCard = null);

                sort.setVisible(false);
                sort.setManaged(false);
                centerPane.setCenter(null);
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
                if (selectedCard == null)
                {
                    PopupMessage.showInfo("Reminder", "Veuillez d'abord sélectionner une chambre à supprimer.");
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning");
                alert.setContentText("Confirm");

                Optional<ButtonType> result = alert.showAndWait();
                CardRoom room = new CardRoom(selectedCard.getNumber());

                boolean isTrue = result.isPresent() && result.get() == ButtonType.OK;
                if (selectedCard != null)
                {
                    if (isTrue)
                    {
                        Result status = room.destroyRoom();
                        if (status.status())
                        {
                            PopupMessage.showInfo("Success", status.message());
                            fillRoom("Tous");
                        } else
                        {
                            PopupMessage.showError("Warnin", status.message());
                        }
                    }
                } else
                {
                    PopupMessage.showError("Error", "La chambre a detruire reste introuvable");
                }
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

        selectedCard = null;

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
                    if (selectedCard != null && selectedCard != card)
                    {
                        selectedCard.getStyleClass().remove("selected-card");
                    }
                    selectedCard = card;

                });
                room.getChildren().add(card);
            }
        }
    }
}