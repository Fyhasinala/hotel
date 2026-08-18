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
        URL fxmlUrl = getClass().getResource("mutatingButton.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try
        {
            fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to compile Button layout components", ex);
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
    protected void handleCardClick()
    {

    }
}