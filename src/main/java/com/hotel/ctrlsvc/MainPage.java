package com.hotel.ctrlsvc;

import javafx.fxml.FXML;
import javafx.scene.Group;

public class MainPage
{
   @FXML
   private Group sakuraGlow;
   
   @FXML
   public void initialize()
   {
      com.hotel.ctrlsvc.FontLoader.loadAll();
   }
}
