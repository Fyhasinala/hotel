package com.hotel.ctrlsvc;

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
               
               if (registeredFont != null)
               {
                  System.out.println("SUCCESS: Loaded family name -> \"" + registeredFont.getFamily() + "\"");
               } else
               {
                  System.err.println("JavaFX engine failed to process the font file data for: " + font);
               }
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
