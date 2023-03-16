package com.tecmis.mis;

import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import com.tecmis.mis.admin.AdminHomeController;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.student.StudentHomeController;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

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

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet rs = null ;


    String userName;
    String passwordl;

    int userId;
    int depId;
    String userTg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void logInbtn(ActionEvent event) throws IOException {

        if(user_name.getText().length() == 0){
            user_name.setStyle("-fx-background-color: rgba(255,0,0,0.30)");
            new Shake(user_name).play();
        } else if (passwordlog.getText().length() == 0) {
            passwordlog.setStyle("-fx-background-color: rgba(255,0,0,0.30)");
            new Shake(passwordlog).play();
        } else {

            try {

                connection = DbConnect.getConnect();
                query = "SELECT * from user";
                preparedStatement = connection.prepareStatement(query);
                rs = preparedStatement.executeQuery();

                userName = user_name.getText();
                passwordl = passwordlog.getText();

                while (rs.next()){
                    if(userName.equals(rs.getString("email")) && passwordl.equals(rs.getString("password"))){

                        if(rs.getString("user_roll").equals("Admin")){

                            userTg = rs.getString("tgnum");
                            depId = Integer.parseInt(rs.getString("depId"));

                            Set<String> privileges = new HashSet<>();
                            privileges.add("adminPrivilege");

                            UserSession.getInstance(userTg, privileges,String.valueOf(depId));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin/admin-home.fxml"));
                            root = loader.load();
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root, 1050,600);
                            stage.setScene(scene);
                            stage.centerOnScreen();
                            stage.show();
                            stage.resizableProperty().setValue(false);
                            new FadeIn(root).play();

                        } else if(rs.getString("user_roll").equals("Student")) {

                            userTg = rs.getString("tgnum");
                            depId = Integer.parseInt(rs.getString("depId"));

                            Set<String> privileges = new HashSet<>();
                            privileges.add("studentPrivilege");

                            UserSession.getInstance(userTg, privileges,String.valueOf(depId));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("student/student-home.fxml"));
                            root = loader.load();
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root, 1050,600);
                            stage.setScene(scene);
                            stage.centerOnScreen();
                            stage.show();
                            stage.resizableProperty().setValue(false);
                            new FadeIn(root).play();

                        } else if(rs.getString("user_roll").equals("Technical Officer")) {

                            userTg = rs.getString("tgnum");
                            depId = Integer.parseInt(rs.getString("depId"));

                            Set<String> privileges = new HashSet<>();
                            privileges.add("studentPrivilege");

                            UserSession.getInstance(userTg, privileges,String.valueOf(depId));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("technical_officer/technical-officer-home.fxml"));
                            root = loader.load();
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root, 1050,600);
                            stage.setScene(scene);
                            stage.centerOnScreen();
                            stage.show();
                            stage.resizableProperty().setValue(false);
                            new FadeIn(root).play();

                        }else if(rs.getString("user_roll").equals("Lecturer")) {

                            userTg = rs.getString("tgnum");
                            depId = Integer.parseInt(rs.getString("depId"));

                            Set<String> privileges = new HashSet<>();
                            privileges.add("lecturerPrivilege");

                            UserSession.getInstance(userTg, privileges,String.valueOf(depId));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("lecturer/lecturer-home.fxml"));
                            root = loader.load();
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

}
