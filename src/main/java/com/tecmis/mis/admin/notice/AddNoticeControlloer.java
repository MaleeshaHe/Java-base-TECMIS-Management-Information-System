package com.tecmis.mis.admin.notice;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNoticeControlloer {
    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton clearBtn;

    @FXML
    private JFXTextArea txtContent;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtTime;

    @FXML
    private JFXTextField txtTitle;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    @FXML
    void clearNotice(ActionEvent event) {
        reset();
    }

    @FXML
    void addNotice(ActionEvent event) {
        try {
            String noTitle = txtTitle.getText();
            String noDate = txtDate.getText();
            String noTime = txtTime.getText();
            String noContent = txtContent.getText();

            connection = DbConnect.getConnect();
            query = "INSERT INTO notice (title,date,time,content) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,noTitle);
            preparedStatement.setString(2,noDate);
            preparedStatement.setString(3,noTime);
            preparedStatement.setString(4,noContent);
            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("successfully created");
            alert.setContentText("successfully created new Notice");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                reset();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-notice.fxml")));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.close();
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void reset(){
        txtTitle.setText("");
        txtDate.setText("");
        txtTime.setText("");
        txtContent.setText("");
    }

}
