package com.tecmis.mis.lecturer.student.marks;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class EditMarksController {
    @FXML
    private JFXTextField txtmarks;
    @FXML
    private Label comboExamType;
    @FXML
    private Label comboCourse;
    @FXML
    private Label comboTgnum;
    @FXML
    private Label error;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    String examId;
    @FXML
    void addMarks(ActionEvent event) throws SQLException {
        if(txtmarks.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the marks Field");
        }else{
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Update Marks");
            alert1.setContentText("Are you sure update this Marks");
            Optional<ButtonType> aresult = alert1.showAndWait();

            if(aresult.get() == ButtonType.OK){
                String tgnum=comboTgnum.getText();
                Float marks=Float.parseFloat(txtmarks.getText());

                connection = DbConnect.getConnect();
                query = "UPDATE studentmark SET Marks = ? WHERE tgnum = ? AND ExamId= ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setFloat(1,marks);
                preparedStatement.setString(2,tgnum);
                preparedStatement.setString(3,examId);
                preparedStatement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully updated");
                alert.setContentText("successfully updated Marks");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
    }
    public void reset(){
        txtmarks.setText("");
    }

    public void showInformation(String examId,String examtype, String courseName,String tgnum, Float marks) {
        this.examId=examId;
        comboExamType.setText(examtype);
        comboCourse.setText(courseName);
        comboTgnum.setText(tgnum);
        txtmarks.setText(Float.toString(marks));
    }
}
