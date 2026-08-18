package com.hotel.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PopupMessage
{
    public static void showInfo(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setHeight(200);
        alert.setWidth(500);
        alert.setResizable(false);

        alert.showAndWait();
    }

    public static void showError(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setHeight(200);
        alert.setWidth(500);
        alert.showAndWait();
        alert.setResizable(false);
    }
}
