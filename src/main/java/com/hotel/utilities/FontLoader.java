package com.hotel.utilities;

import javafx.scene.text.Font;

public class FontLoader
{
   public static void loadAll()
   {
      String[] fonts =
         {
            "/com/hotel/fonts/Mending.otf"
         };
      for (String font : fonts)
      {
         try
         {
            java.io.InputStream load = FontLoader.class.getResourceAsStream(font);
            if (load != null)
            {
                Font registeredFont = Font.loadFont(load, 16);
            }
            else
            {
               System.err.println("Could not load font " + font);
            }
         }
         catch (Exception ex)
         {
            System.err.println("Unexpected error: " + ex.getMessage());
         }
      }
   }
}
