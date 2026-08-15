module com.hotel {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.hotel to javafx.fxml;
    exports com.hotel;
}
