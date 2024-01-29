package storlien.beertracker.core.javafx;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storlien.beertracker.core.Filehandler;

public class App extends Application {

    private AppController controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("BeerTracker");

        URL fxmlLocation = App.class.getClassLoader().getResource("ui.fxml");
        System.out.println(fxmlLocation);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ui.fxml"));

        primaryStage.setScene(new Scene(loader.load()));

        controller = loader.getController();

        primaryStage.setOnCloseRequest(event -> {
            Filehandler.saveFile();
            Filehandler.saveHashFile();
            controller.onCloseRequest();
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}