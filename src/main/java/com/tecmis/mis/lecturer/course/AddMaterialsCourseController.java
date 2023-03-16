package com.tecmis.mis.lecturer.course;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class AddMaterialsCourseController {
    @FXML
    private Label txtCourseCode;

    @FXML
    private Label txtCourseName;

    @FXML
    private Label txtCredit;

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


    @FXML
    void addCourseMaterials(ActionEvent event) {

        if(txtMaterials.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the Field");
        }
        else {
            try {
                String materials = txtMaterials.getText();
                String courseCode=CourseDetails.getCourseCode();
                connection = DbConnect.getConnect();
                query = "UPDATE course SET material=? WHERE courseCode = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, materials);
                preparedStatement.setString(2,courseCode );
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Updated");
                alert.setContentText("successfully added course material");
                Optional<ButtonType> result = alert.showAndWait();

            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
    @FXML
    void clearCourse(ActionEvent event) {
        reset();
    }

    private void reset(){
        txtMaterials.setText("");
    }
    public void showInformation(String courseCode,String courseName, int credit, String material){
        txtCourseCode.setText(courseCode);
        txtCourseName.setText(courseName);
        txtCredit.setText(String.valueOf(credit));
        txtMaterials.setText(material);
    }
}
