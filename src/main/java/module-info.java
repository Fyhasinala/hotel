module com.hotel {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires java.sql;

    opens com.hotel to javafx.fxml;
    opens com.hotel.ctrlsvc to javafx.fxml;
    exports com.hotel;
}