package com.hotel.controllers;

import com.hotel.utilities.MutatingButton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ListRoom
{
    @FXML public HBox footer;
    @FXML public BorderPane centerPane;
    @FXML private ScrollPane panelList;
    @FXML private FlowPane room;
    @FXML private ComboBox<String> sort;

    private MutatingButton add;
    private MutatingButton modify;
    private MutatingButton delete;
    private MutatingButton cancel;
    private MutatingButton confirm;

    // Track the active open form instance so we can pull text from it later
    private NewRoom activeFormInstance;

    public void initialize()
    {
        initializeAllButtons();
        showRoom();

        fillRoom("Tous");
        sort.setOnAction(event -> { String filter = sort.getValue(); fillRoom(filter); });
    }

    private void initializeAllButtons() {
        add = new MutatingButton("/com/hotel/assets/add.png") {
            @Override
            protected void handleButtonClick() {
                // Store the instance into our tracker variable
                activeFormInstance = new NewRoom(ListRoom.this);
                centerPane.setCenter(activeFormInstance);
                showNewRoomFooter();
            }
        };

        modify = new MutatingButton("/com/hotel/assets/modify.png");
        delete = new MutatingButton("/com/hotel/assets/delete.png");

        confirm = new MutatingButton("/com/hotel/assets/confirm.png") {
            @Override
            protected void handleButtonClick() {
                if (activeFormInstance != null) {
                    // 1. Grab the inputs using our getters!
                    String roomNumValue = activeFormInstance.getRoomNumber();
                    String designValue  = activeFormInstance.getDesign();
                    String priceValue   = activeFormInstance.getPrice();

                    // Validation check: prevent empty entries
                    if (roomNumValue.isEmpty() || designValue == null) {
                        System.err.println("Error: Fields cannot be left empty!");
                        return;
                    }

                    // 2. Establish PostgreSQL Credentials
                    String dbUrl = "jdbc:postgresql://localhost:5432/hotel_db"; // Replace with your DB name
                    String dbUser = "postgres";                                 // Your username
                    String dbPassword = "your_password";                         // Your password

                    // 3. Prepare the secure SQL query template
                    String sqlQuery = "INSERT INTO rooms (room_number, status, design, price) VALUES (?, ?, ?, ?)";

                    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

                        pstmt.setString(1, roomNumValue);
                        pstmt.setString(2, "L"); // Default new rooms to 'L' (Libre)
                        pstmt.setString(3, designValue);
                        pstmt.setInt(4, Integer.parseInt(priceValue));

                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("SUCCESS: Room added to database!");
                        }
                    } catch (SQLException ex) {
                        System.err.println("Database error: " + ex.getMessage());
                        ex.printStackTrace();
                    } catch (NumberFormatException ex) {
                        System.err.println("Price parsing error: " + ex.getMessage());
                    }
                }

                // Cleanly clear reference tracker and return to room grid view list
                activeFormInstance = null;
                showRoom();
            }
        };

        cancel = new MutatingButton("/com/hotel/assets/cancel.png") {
            @Override
            protected void handleButtonClick() {
                activeFormInstance = null; // Clear tracking reference
                showRoom();
            }
        };
    }

    public void showRoom()
    {
        centerPane.setCenter(panelList);

        footer.getChildren().clear();
        footer.getChildren().addAll(add, modify, delete);

        fillRoom(sort.getValue() != null ? sort.getValue() : "Tous");
    }

    private void showNewRoomFooter()
    {
        footer.getChildren().clear();
        footer.getChildren().addAll(confirm, cancel);
    }

    public void fillRoom(String filter)
    {
        room.getChildren().clear();
        room.setHgap(10);
        room.setVgap(10);

        // Keep using your mock data template loop below until you pull rows from DB...
        String[][] mockRooms = {
                {"101", "L", "STANDARD"}, {"102", "O", "STANDARD"}, {"103", "L", "LUXE"},
                {"104", "O", "LUXE"},     {"105", "L", "STANDARD"}, {"201", "O", "LUXE"},
                {"202", "L", "LUXE"},     {"203", "O", "SUITE"},    {"204", "L", "STANDARD"},
                {"205", "O", "LUXE"},     {"301", "L", "SUITE"},    {"302", "O", "SUITE"},
                {"303", "L", "LUXE"},     {"304", "L", "STANDARD"}, {"305", "O", "SUITE"}
        };

        for (String[] r : mockRooms) {
            String num = r[0];
            String status = r[1];
            String design = r[2];

            boolean matchesAll = filter.equals("Tous");
            boolean matchesLibre = filter.equals("Libre") && status.equals("L");
            boolean matchesOccuper = filter.equals("Occuper") && status.equals("O");

            if (matchesAll || matchesLibre || matchesOccuper) {
                Rooms card = new Rooms(num, status, design);
                room.getChildren().add(card);
            }
        }
    }
}
