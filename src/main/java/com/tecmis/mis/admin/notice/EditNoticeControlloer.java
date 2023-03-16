package com.tecmis.mis.admin.notice;

import animatefx.animation.Shake;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class EditNoticeControlloer {
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
    private Label error;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    int notice_id;

    @FXML
    void clearNotice(ActionEvent event) {
        reset();
    }

    @FXML
    void addNotice(ActionEvent event) {

        if(txtTitle.getText().length() == 0 || txtTime.getText().length() == 0 || txtDate.getText().length() == 0 || txtContent.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Update Notice");
            alert1.setContentText("Are you sure update this Notice");
            Optional<ButtonType> aresult = alert1.showAndWait();

            if(aresult.get() == ButtonType.OK){
                try {

                    String noTitle = txtTitle.getText();
                    String noDate = txtDate.getText();
                    String noTime = txtTime.getText();
                    String noContent = txtContent.getText();

                    connection = DbConnect.getConnect();
                    query = "UPDATE notice SET title = ?, date = ?, time = ?, content = ? WHERE notice_id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,noTitle);
                    preparedStatement.setString(2,noDate);
                    preparedStatement.setString(3,noTime);
                    preparedStatement.setString(4,noContent);
                    preparedStatement.setInt(5,notice_id);
                    preparedStatement.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("successfully updated");
                    alert.setContentText("successfully updated Notice");
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
        }




    }

    private void reset(){
        txtTitle.setText("");
        txtDate.setText("");
        txtTime.setText("");
        txtContent.setText("");
    }
    public void showInformation(int id, String title,String date, String time, String content){
        notice_id = id;
        txtTitle.setText(title);
        txtDate.setText(date);
        txtTime.setText(time);
        txtContent.setText(content);
    }
}
