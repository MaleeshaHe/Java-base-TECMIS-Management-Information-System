package com.tecmis.mis.admin;

import animatefx.animation.FadeInDown;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminHomeController implements Initializable {

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private BorderPane mainPane;

    @FXML
    private BorderPane borderpane;

    @FXML
    private JFXButton about;

    @FXML
    private JFXButton user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/user-view.fxml")));
            borderpane.setCenter(view);
        } catch (IOException ex) {
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    public void btnUser(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/user-view.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnNotice(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("notice/notice.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnCourse(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("course/course.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnTimeTable(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("timetable/timetable.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }
}

