package com.hotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Room
{
    @FXML
    public ComboBox sort;
    @FXML
    public VBox first;
    @FXML
    public VBox second;
    @FXML
    public VBox third;
    @FXML
    public HBox roomList;
    @FXML
    public HBox statusBar;
    @FXML
    public HBox manageRoom;
    @FXML
    public BorderPane roomSection;

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
}
