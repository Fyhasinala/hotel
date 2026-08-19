module com.hotel {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires java.desktop;

    opens com.hotel to javafx.fxml;
    exports com.hotel;
    opens com.hotel.controllers to javafx.fxml;
    opens com.hotel.utilities to javafx.fxml;
}