package com.hotel.utilities;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public abstract class ClickAnimation extends Button
{
    public ClickAnimation()
    {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), this);
        ScaleTransition pressDown = new ScaleTransition(Duration.millis(50), this);
        pressDown.setToX(0.92);
        pressDown.setToY(0.92);

        ScaleTransition releasePop = new ScaleTransition(Duration.millis(100), this);
        releasePop.setToX(1.05);
        releasePop.setToY(1.05);

        pressDown.setOnFinished(finishEvent -> releasePop.playFromStart());

        this.setOnMouseEntered
        (event ->
            {
                st.setToX(1.05);
                st.setToY(1.05);
                st.playFromStart();
            }
        );
        this.setOnMouseExited
        (event ->
            {
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
            }
        );
        this.setOnMouseClicked
        (event ->
            {
                pressDown.playFromStart();
            }
        );
    }

    protected abstract void handleButtonClick();
}
