package com.tecmis.mis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

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

    public void okbtn(ActionEvent actionEvent) {
    }
}
