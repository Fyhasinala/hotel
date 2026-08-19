package com.hotel.databases;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class Vatis implements VatisGateway
{
    private static HikariDataSource dataSource;
    private static Vatis instance = null;
   
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
          HikariConfig config = new HikariConfig();
          config.setJdbcUrl(data.getProperty("url"));
          config.setUsername(data.getProperty("username"));
          config.setPassword(data.getProperty("password"));

          config.setMaximumPoolSize(10);
          config.setMinimumIdle(2);
          config.setConnectionTimeout(30000);
          dataSource = new HikariDataSource(config);
      }
      catch (IOException ex)
      {
         throw new RuntimeException("Failed to load Vatis information : ", ex);
      }
   }
   
   public static synchronized VatisGateway prepare()
   {
      if (instance == null)
      {
         instance = new Vatis();
      }
      return instance;
   }

    public static synchronized void closePool()
    {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
   public Connection ready() throws SQLException
   {
       return dataSource.getConnection();
   }
}
