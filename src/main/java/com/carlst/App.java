package com.carlst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BeerTracker");
        primaryStage.setScene(new Scene(FXMLLoader.load(App.class.getResource("fxml/Ui.fxml"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
