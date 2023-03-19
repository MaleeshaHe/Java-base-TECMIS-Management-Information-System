package com.tecmis.mis.lecturer.student.attendance;


import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable {


    @FXML
    private TableColumn<AttendanceDetails, String> tgnumCol;
    @FXML
    private TableColumn<AttendanceDetails, String> stNameCol;
    @FXML
    private TableColumn<AttendanceDetails, String> courseCol;
    @FXML
    private TableColumn<AttendanceDetails, String> stateCol;
    @FXML
    private TableColumn<AttendanceDetails, String> dateCol;
    @FXML
    private TableView<AttendanceDetails> attendanceTable;
    @FXML
    private JFXComboBox<String> comboStudent;
    @FXML
    private JFXTextField txtKeyword;
    @FXML
    private Stage stage;
    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    AttendanceDetails attendanceDetails = null;
    ObservableList<AttendanceDetails> attendanceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    public void refreshTable() {
        try {
            attendanceList.clear();
            int depId= Integer.parseInt(UserSession.getUserDepId());
            query = "SELECT * from attendance,user WHERE attendance.tgnum=user.tgnum AND user.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                attendanceList.add(new AttendanceDetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getString("CourseCode"),
                        resultSet.getString("date"),
                        resultSet.getString("state"),
                        resultSet.getString("lname")));
                attendanceTable.setItems(attendanceList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();
        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));

        attendanceTable.setItems(attendanceList);
        new FadeIn(attendanceTable).play();

        FilteredList<AttendanceDetails> filteredData = new FilteredList<>(attendanceList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(AttendanceDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (AttendanceDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (AttendanceDetails.getLname().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (AttendanceDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (AttendanceDetails.getDate().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else if (AttendanceDetails.getCourseCode().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else if (AttendanceDetails.getState().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                }else {
                    return false;
                }

            });
        });

        SortedList<AttendanceDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(attendanceTable.comparatorProperty());
        attendanceTable.setItems(sortedData);
        new FadeIn(attendanceTable).play();
    }
}
