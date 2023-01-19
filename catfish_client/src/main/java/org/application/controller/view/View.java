package org.application.controller.view;

//Enum with fxml files' paths
public enum View {
    SIGN_IN("/scenes/SignIn.fxml"),
    SIGN_UP("/scenes/SignUp.fxml"),
    HOME("/scenes/Home.fxml");

    private String location;

    View(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
