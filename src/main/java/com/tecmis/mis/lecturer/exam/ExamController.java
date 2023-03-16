package com.tecmis.mis.lecturer.exam;

import com.jfoenix.controls.JFXComboBox;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.admin.notice.NoticeDetails;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExamController implements Initializable {
    @FXML
    private JFXComboBox<String> comboCourse;
    @FXML
    private TableView<ExamDetails> examTable;
    @FXML
    private TableColumn<NoticeDetails, String> examCodeCol;

    @FXML
    private TableColumn<NoticeDetails, String> courseNameCol;

    @FXML
    private TableColumn<NoticeDetails, Integer> examType;
    @FXML
    private TableColumn<ExamDetails, String> examScore;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    ExamDetails examDetails = null ;
    ObservableList<ExamDetails> examList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        comboboxDataLoad();
    }

    private void comboboxDataLoad(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM course WHERE course.depId=4 OR course.depId="+ UserSession.getUserDepId()+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()){
                data.add(new String(resultSet.getString("courseName")));
            }
            comboCourse.setItems(data);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        examCodeCol.setCellValueFactory(new PropertyValueFactory<>("ExamId"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        examType.setCellValueFactory(new PropertyValueFactory<>("ExamType"));
        examScore.setCellValueFactory(new PropertyValueFactory<>("ExamScore"));
        examTable.setItems(examList);
    }
    private void refreshTable() {
        try {
            examList.clear();
            query = "SELECT * FROM examination,course WHERE course.depId=4 AND examination.CourseCode=course.courseCode OR course.depId="+UserSession.getUserDepId()+" AND examination.CourseCode=course.courseCode";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                examList.add(new com.tecmis.mis.lecturer.exam.ExamDetails(
                        resultSet.getString("ExamId"),
                        resultSet.getString("courseName"),
                        resultSet.getString("ExamType"),
                        resultSet.getInt("ExamScore")));
                examTable.setItems(examList);
            }




        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void refreshTable(String coursecode) {
        try {
            examList.clear();
            query = "SELECT * FROM examination,course WHERE course.depId=4 AND examination.CourseCode=course.courseCode OR course.depId="+UserSession.getUserDepId()+" AND examination.CourseCode=course.courseCode AND examination.CourseCode='"+coursecode+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                examList.add(new com.tecmis.mis.lecturer.exam.ExamDetails(
                        resultSet.getString("ExamId"),
                        resultSet.getString("courseName"),
                        resultSet.getString("ExamType"),
                        resultSet.getInt("ExamScore")));
                examTable.setItems(examList);
            }




        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @FXML
    private void findExams(){
        String coursename = comboCourse.getValue();
        String coursecode = null;
        try {
            connection = DbConnect.getConnect();
            query = "SELECT courseCode FROM course WHERE courseName='"+coursename+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                coursecode = resultSet.getString("courseCode");
            }
            refreshTable(coursecode);

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
