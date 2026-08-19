package com.hotel.dbsvc;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

public final class Vatis implements VatisGateway
{
   private String url;
   private String username;
   private String password;
   
   private Vatis()
   {
      //Properties data = new Properties();
       this.url = "jdbc:postgresql://localhost:5432/gestion_hotel";
         this.username = "postgres";
         this.password = "1618033988";
     
      /*try (InputStream input = Vatis.class.getClassLoader().getResourceAsStream("database.properties"))
      {
         //if (input == null || input.available() == 0)
         System.out.println("input trouve : " + input);
            if (input == null)
         {
            throw new IOException("unable to find database.properties");
            
         }
         data.load(input);
         System.out.println("Infos trouvees : " + data.keySet());
         this.url = data.getProperty("db.url", "jdbc:postgresql://localhost:5432/gestion_hotel");
         this.username = data.getProperty("db.username", "postgres");
         this.password = data.getProperty("db.password", "1618033988");
      }
      catch (IOException ex)
      {
         throw new RuntimeException("Failed to load Vatis information : ", ex);
      }*/
   }
   
   private static VatisGateway instance = null;
   
   public static synchronized VatisGateway prepare()
   {
       System.out.println("input trouve : AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );
      if (instance == null)
      {
         instance = new Vatis();
      }
      return instance;
   }
   
   @Override
   public Connection ready() throws SQLException
   {
      return java.sql.DriverManager.getConnection(url, username, password);
   }
}
