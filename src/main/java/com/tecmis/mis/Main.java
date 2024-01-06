package com.tecmis.mis;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    double x,y =0;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root =   FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-page.fxml")));
        primaryStage.setTitle("TECMIS");

        Image image = new Image("images/appIcon.png");
        primaryStage.getIcons().add(image);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.resizableProperty().setValue(false);
        new FadeIn(root).play();
    }

    public static void main(String[] args) {
        launch();
    }
}

//this is main file
//javaFX