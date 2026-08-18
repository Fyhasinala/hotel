package com.hotel.controllers;

import com.hotel.utilities.MutatingButton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class ListRoom
{
    @FXML
    public HBox manageRoom;
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
        MutatingButton add = new MutatingButton("/com/hotel/assets/add.png");
        MutatingButton modify = new MutatingButton("/com/hotel/assets/modify.png");
        MutatingButton delete = new MutatingButton("/com/hotel/assets/remove.png");

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
