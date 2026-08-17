package com.hotel.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListRoom
{
    @FXML
    public ComboBox sort;
    @FXML
    public HBox statusBar;
    @FXML
    public HBox manageRoom;
    @FXML
    public GridPane room;
    @FXML
    private Room bed;

//    private final ObservableList<Room> list = FXCollections.observableList();

    public void initialize()
    {
        fillBottom();
    }

    public void fillBottom ()
    {
        MyButton add = new MyButton("", "/com/hotel/assets/add.png");
        MyButton modify = new MyButton("", "/com/hotel/assets/modify.png");
        MyButton delete = new MyButton("", "/com/hotel/assets/remove.png");

        manageRoom.getChildren().addAll(add, modify, delete);
    }

    public void fillRoom ()
    {
//        Room[]
    }
}
