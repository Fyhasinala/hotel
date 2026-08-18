package com.hotel.controllers;

import com.hotel.utilities.MutatingButton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ListRoom
{
    @FXML
    public HBox manageRoom;
    @FXML
    public BorderPane centerPane;
    @FXML
    private ScrollPane panelList;
    @FXML
    private FlowPane room;
    @FXML
    private ComboBox<String> sort;

    public void initialize()
    {
        fillBottom();

        fillRoom("Tous");
        sort.setOnAction(event-> {String filter = sort.getValue(); fillRoom(filter);});
    }

    public void fillBottom ()
    {
        MutatingButton add = new MutatingButton("/com/hotel/assets/add.png")
        {
            @Override
            protected void handleButtonClick()
            {
                NewRoom entry = new NewRoom();
                centerPane.setCenter(entry);
            }
        };

        MutatingButton modify = new MutatingButton("/com/hotel/assets/modify.png");
        MutatingButton delete = new MutatingButton("/com/hotel/assets/remove.png");

        MutatingButton back = new MutatingButton("/com/hotel/assets/remove.png")
        {
            @Override
            protected void handleButtonClick()
            {
                centerPane.setCenter(panelList);
                fillRoom(sort.getValue() != null ? sort.getValue() : "Tous");
            }
        };

        manageRoom.getChildren().addAll(add, modify, delete);

    }

    public void fillRoom (String filter)
    {
        room.getChildren().clear();
        room.setHgap(10);
        room.setVgap(10);

//      for (String r : num)
//      {
//            if (filter.equals("Tous")) {
//                room.getChildren().add(new Rooms(num, status, design));
//            }
//            else if (filter.equals("Libre") && status.equals("L")) {
//                room.getChildren().add(new Rooms(num, status, design));
//            }
//            else if (filter.equals("Occuper") && status.equals("O")) {
//                room.getChildren().add(new Rooms(num, status, design));
//            }
//      }
    }
}
