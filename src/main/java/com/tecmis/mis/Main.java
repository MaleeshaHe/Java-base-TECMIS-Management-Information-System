package com.tecmis.mis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    double x,y =0;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root =   FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/admin-home.fxml")));
        primaryStage.setTitle("TECMIS");
      //  primaryStage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        primaryStage.setScene(new Scene(root, 1050,600));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}