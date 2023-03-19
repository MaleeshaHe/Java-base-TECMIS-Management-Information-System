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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CheckEligibilityController implements Initializable {
    @FXML
    private TableView<CheckEligibiltyDetails> eligibiltityTable;
    @FXML
    private TableColumn<CheckEligibiltyDetails, String> tgnumCol;
    @FXML
    private TableColumn<CheckEligibiltyDetails, String> stNameCol;
    @FXML
    private TableColumn<CheckEligibiltyDetails, String> courseCol;
    @FXML
    private TableColumn<CheckEligibiltyDetails, String> attendanceCol;
    @FXML
    private TableColumn<CheckEligibiltyDetails, String> caMarksCol;
    @FXML
    private TableColumn<CheckEligibiltyDetails, String> statusCol;
    @FXML
    private JFXTextField txtKeyword;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CheckEligibiltyDetails checkEligibiltyDetails =null;
    ObservableList<CheckEligibiltyDetails> checkeligibiltyDetailsList = FXCollections.observableArrayList();
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
        attendanceCol.setCellValueFactory(new PropertyValueFactory<>("attendanceCount"));
        caMarksCol.setCellValueFactory(new PropertyValueFactory<>("caMarks"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        eligibiltityTable.setItems(checkeligibiltyDetailsList);

        eligibiltityTable.setItems(checkeligibiltyDetailsList);
        new FadeIn(eligibiltityTable).play();

        FilteredList<CheckEligibiltyDetails> filteredData = new FilteredList<>(checkeligibiltyDetailsList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(CheckEligibiltyDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (CheckEligibiltyDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (CheckEligibiltyDetails.getCourseCode().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (CheckEligibiltyDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (CheckEligibiltyDetails.getCourseName().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else if (CheckEligibiltyDetails.getStatus().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<CheckEligibiltyDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(eligibiltityTable.comparatorProperty());
        eligibiltityTable.setItems(sortedData);
        new FadeIn(eligibiltityTable).play();
    }
    public void refreshTable(){
        checkeligibiltyDetailsList.clear();
        try {
            connection = DbConnect.getConnect();
            int depId=Integer.parseInt(UserSession.getUserDepId());
            query="SELECT attendance.tgnum, user.fname,course.courseCode, course.courseName, COUNT(DISTINCT attendance.CourseCode, attendance.State, attendance.date) AS attendance_count, SUM(sm.marks)/COUNT(DISTINCT attendance.CourseCode, attendance.State, attendance.date) AS 'CaMarks'\n" +
                    "FROM attendance\n" +
                    "JOIN user ON attendance.tgnum = user.tgnum\n" +
                    "JOIN studentmark sm ON attendance.tgnum = sm.tgnum\n" +
                    "JOIN examination e ON sm.examId = e.ExamId\n" +
                    "JOIN course ON e.CourseCode = course.courseCode AND course.depId = user.depId AND attendance.CourseCode=course.courseCode\n" +
                    "WHERE attendance.State = 'Present' AND e.ExamType IN ('Quiz', 'Mid term', 'Assessment') and user.depId='"+depId+"'\n" +
                    "GROUP BY attendance.tgnum, attendance.CourseCode\n";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                checkeligibiltyDetailsList.add(new CheckEligibiltyDetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getString("courseName"),
                        resultSet.getString("courseCode"),
                        resultSet.getFloat("attendance_count"),
                        resultSet.getFloat("CaMarks")));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
