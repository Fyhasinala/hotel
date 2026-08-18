package com.hotel.utilities;

import javafx.animation.ScaleTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public abstract class CardHover extends StackPane {
    public CardHover()
    {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), this);

        this.setOnMouseEntered(event -> {
            st.setToX(1.05);
            st.setToY(1.05);
            st.playFromStart();
        });

        this.setOnMouseExited(event -> {
            st.setToX(1.0);
            st.setToY(1.0);
            st.playFromStart();
        });
    }
}
