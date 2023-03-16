package com.tecmis.mis.technical_officer.attendance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.tecmis.mis.JDBC.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.bytedeco.javacpp.presets.opencv_core;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable {
    @FXML
    private JFXButton btnAttendanceUpload;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXComboBox<Attendance> cmbCourseCode;

    @FXML
    private JFXComboBox<Attendance> cmbState;

    @FXML
    private JFXComboBox<Attendance> cmbStudentTG;

    @FXML
    private TableColumn<Attendance, String> courseCodeCol;

    @FXML
    private TableColumn<Attendance, Date> dateCol;

    @FXML
    private JFXButton deleteAttendance;

    @FXML
    private DatePicker dtpDate;

    @FXML
    private JFXButton editAttendance;

    @FXML
    private Label error;

    @FXML
    private Label lblAttendance;

    @FXML
    private JFXButton refreshAttendance;

    @FXML
    private TableColumn<Attendance, String> stateCol;

    @FXML
    private TableColumn<Attendance, String> stuTGCol;

    @FXML
    private TableView<Attendance> table_attendance;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    ObservableList<Attendance> attlist = FXCollections.observableArrayList();

    @FXML
    void clear(ActionEvent event) {

    }

    @FXML
    void setDeleteAttendance(ActionEvent event) {

    }

    @FXML
    void setEditAttendance(ActionEvent event) {

    }

    @FXML
    void refreshTable(ActionEvent event) {

    }

    public void setComboCourseCode(){
        connection = DbConnect.getConnect();
        query = "SELECT courseCode FROM course";

        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            resultSet = preparedStatement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList data = FXCollections.observableArrayList();

        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                data.add(new String(resultSet.getString(1)));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        cmbCourseCode.setItems(FXCollections.observableArrayList(data));
    }

    public void setComboStudentTG(){
        connection = DbConnect.getConnect();
        query = "SELECT tgnum FROM user WHERE user_roll = 'Student'";

        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            resultSet = preparedStatement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList data = FXCollections.observableArrayList();

        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                data.add(new String(resultSet.getString(1)));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        cmbStudentTG.setItems(FXCollections.observableArrayList(data));
    }

    public void setState(){
        ObservableList stateList = FXCollections.observableArrayList("Present","Absent");
        cmbState.setItems(stateList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboCourseCode();
        setComboStudentTG();
        setState();
        loadAttendance();
    }

    public void reset(){
        cmbCourseCode.setItems(FXCollections.emptyObservableList());
        cmbStudentTG.setItems(FXCollections.emptyObservableList());
        cmbState.setItems(FXCollections.emptyObservableList());
        dtpDate.setDayCellFactory(null);
    }

    public void loadAttendance(){
        connection = DbConnect.getConnect();
        attlist.clear();
        query = "SELECT * FROM attendance";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                attlist.add(new Attendance(
                        resultSet.getString("CourseCode"),
                        resultSet.getString("tgnum"),
                        resultSet.getString("State"),
                        resultSet.getString("date")
                ));
                table_attendance.setItems(attlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("Course_Code"));
        stuTGCol.setCellValueFactory(new PropertyValueFactory<>("Student_TG"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("Attendance_State"));
        //dateCol.setCellValueFactory(new PropertyValueFactory<>("sDate"));
    }

    public void addAttendance(){
        String aCourceCode = String.valueOf(cmbCourseCode.getValue());
        String aStudentTG = String.valueOf(cmbStudentTG.getValue());
        String aState = String.valueOf(cmbState.getValue());
        String aDate = String.valueOf(dtpDate.getValue());

        connection = DbConnect.getConnect();
        query = "INSERT INTO attendance(CourseCode,tgnum,State,date) VALUES (?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,aCourceCode);
            preparedStatement.setString(2,aStudentTG);
            preparedStatement.setString(3,aState);
            preparedStatement.setString(4, aDate);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadAttendance();
    }
}
