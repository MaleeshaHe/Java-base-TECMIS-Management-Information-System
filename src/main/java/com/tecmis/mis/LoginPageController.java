package com.tecmis.mis;

import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    Connection con = null;
    Statement stmt = null;
    @FXML
    private Label error;
    @FXML
    private JFXPasswordField passwordlog;

    @FXML
    private JFXTextField user_name;

    @FXML
    private BorderPane parent;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    String userName;
    String passwordl;

    int userId;

    public LoginPageController() {
        con = JDBC.connection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void logInbtn(ActionEvent event) throws IOException {
        try {
            stmt = con.createStatement();
            String query = "SELECT * from users";

            userName = user_name.getText();
            passwordl = passwordlog.getText();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                if(userName.equals(rs.getString(6)) && passwordl.equals(rs.getString(7))){

                    if(rs.getString(12).equals("Admin")){

                        userId = Integer.parseInt(rs.getString(1));

                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/admin-home.fxml")));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root, 1050,600);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                        stage.show();
                        stage.resizableProperty().setValue(false);
                        new FadeIn(root).play();

                    } else if (rs.getString(11).equals("student")) {

                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/admin-homse.fxml")));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root, 1050,600);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                        stage.show();
                        stage.resizableProperty().setValue(false);
                        new FadeIn(root).play();

                    }
                }
                else {
                    new Shake(error).play();
                    error.setText("Incorrect User name or Password");
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }

}
