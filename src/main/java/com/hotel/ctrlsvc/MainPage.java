package com.hotel.ctrlsvc;


import java.io.IOException;

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

   /*@FXML
   static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }*/
   
}
