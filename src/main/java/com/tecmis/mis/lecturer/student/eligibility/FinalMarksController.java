package com.tecmis.mis.lecturer.student.eligibility;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class FinalMarksController implements Initializable {
    @FXML
    private TableView<FinalMarksDetails> gradeTable;
    @FXML
    private TableColumn<FinalMarksDetails, String> tgnumCol;
    @FXML
    private TableColumn<FinalMarksDetails, String> stNameCol;
    @FXML
    private TableColumn<FinalMarksDetails, String> courseCol;
    @FXML
    private TableColumn<FinalMarksDetails, String> marksCol;
    @FXML
    private TableColumn<FinalMarksDetails, String> gradesCol;
    @FXML
    private JFXTextField txtKeyword;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    FinalMarksDetails finalMarksDetails=null;
    ObservableList<FinalMarksDetails> finalMarksList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();
        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        marksCol.setCellValueFactory(new PropertyValueFactory<>("marks"));
        gradesCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeTable.setItems(finalMarksList);
        new FadeIn(gradeTable).play();

        FilteredList<FinalMarksDetails> filteredData = new FilteredList<>(finalMarksList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(FinalMarksDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (FinalMarksDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (FinalMarksDetails.getCourseCode().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (FinalMarksDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (FinalMarksDetails.getCourseName().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else if (FinalMarksDetails.getGrade().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                }else {
                    return false;
                }

            });
        });
        SortedList<FinalMarksDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(gradeTable.comparatorProperty());
        gradeTable.setItems(sortedData);
        new FadeIn(gradeTable).play();

    }
    public void refreshTable(){
        finalMarksList.clear();
        try{
            connection = DbConnect.getConnect();
            int depId=Integer.parseInt(UserSession.getUserDepId());
            query="SELECT user.tgnum,user.fname,course.courseCode,course.courseName,SUM(studentmark.marks) AS 'Sum' FROM user,studentmark,course,examination,studentcourse WHERE user.tgnum=studentmark.tgnum AND user.depId=course.depId AND examination.ExamId=studentmark.examId AND studentcourse.courseCode=course.courseCode AND studentcourse.tgnum=user.tgnum AND examination.CourseCode=course.courseCode AND user.depId='"+depId+"' GROUP BY studentmark.tgnum,course.courseName";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                finalMarksList.add(new FinalMarksDetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getString("courseCode"),
                        resultSet.getString("courseName"),
                        resultSet.getFloat("Sum")));
                gradeTable.setItems(finalMarksList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
