package org.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.application.controller.view.View;
import org.application.controller.view.ViewSwitcher;

/**
 * A controller class which receives commands from Front-End and handle them
 * */
public class SignIn {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button signIn;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Hyperlink passwordRestore;


    @FXML
    void initialize(){
        signUpLink.setOnAction(event -> ViewSwitcher.switchScene(View.SIGN_UP));
    }
}
