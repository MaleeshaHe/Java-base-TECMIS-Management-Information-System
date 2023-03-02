package com.tecmis.mis;

import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    @FXML
    private Button btn_changeMode;

    @FXML
    private ImageView changeImage;

    @FXML
    private ImageView exit;

    @FXML
    private BorderPane parent;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private double mode = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        btn_changeMode.setOnMouseClicked(event -> {
            if (mode == 0){
                parent.getStylesheets().add("style/light_mode.css");
                parent.getStylesheets().remove("style/dark_mode.css");
                Image image = new Image("images/night-mode.png");
                changeImage.setImage(image);
                mode = 1;
            } else if (mode == 1) {
                parent.getStylesheets().add("style/dark_mode.css");
                parent.getStylesheets().remove("style/light_mode.css");
                Image image = new Image("images/brightness (1).png");
                changeImage.setImage(image);
                mode = 0;
            }
        });
    }

    public void logInbtn(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("admin/admin-home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1050,600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.resizableProperty().setValue(false);
        new FadeIn(root).play();
    }
}
