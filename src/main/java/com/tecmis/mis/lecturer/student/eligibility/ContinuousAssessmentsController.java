package com.tecmis.mis.lecturer.student.eligibility;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.lecturer.student.marks.MarksDetails;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ContinuousAssessmentsController implements Initializable {
    @FXML
    private TableView<CADetails> cAtable;
    @FXML
    private TableColumn<CADetails, String> tgnumCol;
    @FXML
    private TableColumn<CADetails, String> stNameCol;
    @FXML
    private TableColumn<CADetails, String> courseCol;
    @FXML
    private TableColumn<CADetails, String> marksCol;
    @FXML
    private TableColumn<CADetails, String> statusCol;

    @FXML
    private JFXTextField txtKeyword;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CADetails caDetails=null;
    ObservableList<CADetails> caList = FXCollections.observableArrayList();
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
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    cAtable.setItems(caList);

        cAtable.setItems(caList);
        new FadeIn(cAtable).play();

        FilteredList<CADetails> filteredData = new FilteredList<>(caList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(CADetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (CADetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (CADetails.getCourseCode().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (CADetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (CADetails.getCourseName().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else if (CADetails.getStatus().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<CADetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(cAtable.comparatorProperty());
        cAtable.setItems(sortedData);
        new FadeIn(cAtable).play();
    }

    public void refreshTable(){
        caList.clear();
        try{
            connection = DbConnect.getConnect();
            int depId=Integer.parseInt(UserSession.getUserDepId());
            query="SELECT user.tgnum,user.fname,course.courseCode,course.courseName,SUM(studentmark.marks) AS 'Sum' FROM user,studentmark,course,examination,studentcourse WHERE examination.ExamType IN ('Quiz', 'Mid term', 'Assessment') AND user.tgnum=studentmark.tgnum AND user.depId=course.depId AND examination.ExamId=studentmark.examId AND studentcourse.courseCode=course.courseCode AND studentcourse.tgnum=user.tgnum AND examination.CourseCode=course.courseCode AND user.depId='"+depId+"' GROUP BY studentmark.tgnum,course.courseName";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                caList.add(new CADetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getString("courseCode"),
                        resultSet.getString("courseName"),
                        resultSet.getFloat("Sum")));
                cAtable.setItems(caList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }
}
