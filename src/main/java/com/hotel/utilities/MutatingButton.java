package com.hotel.utilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MutatingButton extends ClickAnimation
{
    @FXML private ImageView image;
    @FXML private Label label;

    public MutatingButton(String buttonImage)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/utilities/mutatingButton.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (buttonImage != null && !buttonImage.trim().isEmpty())
        {
            try
            {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(buttonImage)));
                this.image.setImage(image);
            } catch (Exception ex)
            {
                System.err.println("Resource stream failed for asset: " + buttonImage);
            }
        }
    }

    @Override
    protected void handleButtonClick() { }
}