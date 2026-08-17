package com.hotel.databases;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

public final class Vatis implements VatisGateway
{
   private final String url;
   private final String username;
   private final String password;
   
   private Vatis()
   {
      Properties data = new Properties();
      try (InputStream input = Vatis.class.getClassLoader().getResourceAsStream("database.properties"))
      {
         if (input == null || input.available() == 0)
         {
            throw new IOException("unable to find database.properties");
         }
         data.load(input);
         this.url = data.getProperty("url");
         this.username = data.getProperty("username");
         this.password = data.getProperty("password");
      }
      catch (IOException ex)
      {
         throw new RuntimeException("Failed to load Vatis information : ", ex);
      }
   }
   
   private static VatisGateway instance = null;
   
   public static synchronized VatisGateway prepare()
   {
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
