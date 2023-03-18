package com.tecmis.mis.lecturer.student.marks;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class AddMarksController implements Initializable {
    @FXML
    private JFXTextField txtmarks;
    @FXML
    private JFXComboBox<String> comboExamType;
    @FXML
    private JFXComboBox<String> comboCourse;
    @FXML
    private JFXComboBox<String> comboTgnum;
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
    String examId=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboExamType.setItems(FXCollections.observableArrayList("Quiz","Assessment","Mid term","Final theory","Final practical"));
        comboboxCourseDataLoad();
        comboboxTgnumDataLoad();
    }
    public void  comboboxCourseDataLoad(){
        try {
            int depId= Integer.parseInt(UserSession.getUserDepId());
            connection = DbConnect.getConnect();
            query="SELECT * FROM course WHERE course.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()){
                data.add(new String(resultSet.getString("courseName")));
            }
            comboCourse.setItems(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void  comboboxTgnumDataLoad(){
        try {
            int depId= Integer.parseInt(UserSession.getUserDepId());
            connection = DbConnect.getConnect();
            query="SELECT * FROM user,department WHERE user.depId=department.depId AND user_roll='Student' AND user.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()){
                data.add(new String(resultSet.getString("tgnum")));
            }
            comboTgnum.setItems(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearMarks(javafx.event.ActionEvent event) {
        reset();
    }

    @FXML
    void addMarks(javafx.event.ActionEvent event) throws SQLException{
        if(comboExamType.getValue()==null || comboCourse.getValue()==null || comboTgnum.getValue()==null|| txtmarks.getText().isEmpty()){
            new Shake(error).play();
            error.setText("Please fill all Fields");
        }else{
            try {
                String examType=comboExamType.getValue();
                String course=comboCourse.getValue();
                String tgnum=comboTgnum.getValue();
                Float marks=Float.parseFloat(txtmarks.getText());
//                System.out.println(examType);
//                System.out.println(course);
//                System.out.println(tgnum);

                try{
                    connection = DbConnect.getConnect();
                    query="SELECT ExamId FROM examination,course WHERE examination.ExamType='"+examType+"' AND course.courseName='"+course+"' AND course.courseCode=examination.CourseCode";
                    preparedStatement = connection.prepareStatement(query);
                    resultSet = preparedStatement.executeQuery();

                    List<String> examIds = new ArrayList<>();
                    while (resultSet.next()) {
                        examId = resultSet.getString("ExamId"); // retrieve the value of ExamId column in the current row
                        examIds.add(examId); // add the value to the list
                    }



                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                connection = DbConnect.getConnect();
                query = "INSERT INTO studentmark (tgnum,ExamId,Marks) VALUES (?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,tgnum);
                preparedStatement.setString(2,examId);
                preparedStatement.setFloat(3,marks);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Added");
                alert.setContentText("successfully Added new Marks");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-marks.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void reset(){
        txtmarks.setText("");
        comboTgnum.setValue("");
        comboCourse.setValue("");
        comboExamType.setValue("");
    }
}
