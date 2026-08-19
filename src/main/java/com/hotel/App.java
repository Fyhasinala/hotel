package com.hotel;

import com.hotel.databases.Vatis;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException
    {
        try
        {
            Vatis.prepare();
            System.out.println("HikariCP connection pool initialized successfully!");
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to initialize database pool! Close application.");
            e.printStackTrace();
            System.exit(1);
        }
        stage.setOnCloseRequest(event -> {
            System.out.println("Window closing... shutting down database pool.");
            Vatis.closePool();
        });

       FXMLLoader main = new FXMLLoader(App.class.getResource("/com/hotel/controllers/main.fxml"));
       stage.setTitle("Agnes Sweet Home");
       stage.setScene(new Scene(main.load(), 1366, 768));
       stage.setMinWidth(1366);
       stage.setMinHeight(768);
       stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}