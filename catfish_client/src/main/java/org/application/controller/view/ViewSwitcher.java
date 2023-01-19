package org.application.controller.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * ViewSwitcher - a class that takes the responsibility to switch views for the static scene.
 * Decrease a boilerplate code where the switching is executed.
 * Static scene in short is a singleton pattern.
 * */

public class ViewSwitcher {

    private static Scene scene;

    static {
        try {
            FXMLLoader loader =  new FXMLLoader(ViewSwitcher.class.getResource(View.SIGN_IN.getLocation()));
            Parent root = loader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void switchScene(View view){
        try {
            FXMLLoader loader = new FXMLLoader(ViewSwitcher.class.getResource(view.getLocation()));
            Parent root = loader.load();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }
}
