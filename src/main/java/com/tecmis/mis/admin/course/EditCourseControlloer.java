package com.tecmis.mis.admin.course;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditCourseControlloer implements Initializable {
    @FXML
    private JFXComboBox<String> comboCourseType;
    @FXML
    private JFXComboBox<String> comboDepartment;

    @FXML
    private JFXTextField txtCourseCode;

    @FXML
    private JFXTextField txtCourseName;

    @FXML
    private JFXTextField txtCredit;

    @FXML
    private JFXTextField txtMaterials;


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


    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboDepartment.setItems(FXCollections.observableArrayList("Engineering Technology","Information & Communication Technology","Biosystems Technology","Multidisciplinary Studies"));
        comboCourseType.setItems(FXCollections.observableArrayList("Theory","Practical"));
    }


    @FXML
    void clearCourse(ActionEvent event) {
        reset();
    }

    @FXML
    void addCourse(ActionEvent event) {

        if(comboDepartment.getValue() == null || comboCourseType.getValue() == null || txtCourseCode.getText().length() == 0 || txtCourseName.getText().length() == 0 || txtCredit.getText().length() == 0 || txtMaterials.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {
            try {
                String courseCode = txtCourseCode.getText();
                String courseName = txtCourseName.getText();
                String courseType = comboCourseType.getValue();
                String credit = txtCredit.getText();
                String materials = txtMaterials.getText();
                int department = comboDepartment.getSelectionModel().getSelectedIndex()+1;


                connection = DbConnect.getConnect();
                query = "UPDATE course SET courseCode = ?, courseName= ?, credit = ?, material = ?, depId = ?, courseType = ? WHERE courseCode = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,courseCode);
                preparedStatement.setString(2,courseName);
                preparedStatement.setString(3,credit);
                preparedStatement.setString(4, materials);
                preparedStatement.setInt(5,department);
                preparedStatement.setString(6,courseType);
                preparedStatement.setString(7,courseCode);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully updated");
                alert.setContentText("successfully updated Course");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-course.fxml")));
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

    private void reset(){
        txtCourseCode.setText("");
        txtCourseName.setText("");
        txtCredit.setText("");
        txtMaterials.setText("");
    }

    public void showInformation(String courseCode,String courseName, int credit, String material, String courseType, String depName){
        txtCourseCode.setText(courseCode);
        txtCourseName.setText(courseName);
        txtCredit.setText(String.valueOf(credit));
        txtMaterials.setText(material);
        comboCourseType.getSelectionModel().select(courseType);
        comboDepartment.getSelectionModel().select(depName);
    }

}
