package org.application.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.application.controller.view.ViewSwitcher;

/**
 * This class is an entrance to the project.
 * The start method sets the SignIn scene and show it to user.
 * The main function calls launch method which is the JavaFX
 *      Application's method for creating new thread for GUI.
 * */

public class EntrancePointToApplication extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Catfish messenger");
        stage.setScene(ViewSwitcher.getScene());
        stage.show();
        Client client;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
