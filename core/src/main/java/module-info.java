module beertracker.application.core {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.common;
    requires com.fasterxml.jackson.databind;
    requires unirest.java;

    exports storlien.beertracker.application.core.javafx to javafx.graphics;

    opens storlien.beertracker.application.core.javafx to javafx.fxml;
}