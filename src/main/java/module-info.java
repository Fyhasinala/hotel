module com.hotel {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires java.sql;

    opens com.hotel to javafx.fxml;
    exports com.hotel;
    opens com.hotel.controllers to javafx.fxml;
}