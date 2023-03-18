package com.tecmis.mis.student.attendance;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.admin.user.UserDetails;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.student.notice.NoticeController;
import com.tecmis.mis.student.notice.NoticeDetails;
import com.tecmis.mis.student.notice.ViewNoticeControlloer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttendanceController implements Initializable {
    @FXML
    private TableView<AttendanceDetails> attendanceTable;

    @FXML
    private JFXTextField txtKeyword;

    @FXML
    private TableColumn<AttendanceDetails, String> courseCodeCol;

    @FXML
    private TableColumn<AttendanceDetails, String> courseNameCol;

    @FXML
    private TableColumn<AttendanceDetails, String> dateCol;

    @FXML
    private TableColumn<AttendanceDetails, String> stateCol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    ObservableList<AttendanceDetails> attendanceList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        attendanceTable.setItems(attendanceList);

        FilteredList<AttendanceDetails> filteredData = new FilteredList<>(attendanceList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(AttendanceDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (AttendanceDetails.getCourseCode().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (AttendanceDetails.getCourseName().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<AttendanceDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(attendanceTable.comparatorProperty());
        attendanceTable.setItems(sortedData);
        new FadeIn(attendanceTable).play();
    }

    private void refreshTable() {
        try {
            attendanceList.clear();

            query = "SELECT * FROM attendance,course WHERE attendance.CourseCode=course.courseCode AND attendance.tgnum='"+ UserSession.getUserTgNum() +"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                attendanceList.add(new AttendanceDetails(
                        resultSet.getString("courseCode"),
                        resultSet.getString("courseName"),
                        resultSet.getString("tgnum"),
                        resultSet.getString("date"),
                        resultSet.getString("State")));
                attendanceTable.setItems(attendanceList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
