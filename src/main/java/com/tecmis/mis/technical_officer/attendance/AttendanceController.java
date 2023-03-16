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
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable {
    @FXML
    private JFXButton btnAttendanceUpload;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXComboBox<?> cmbCourseCode;

    @FXML
    private JFXComboBox<?> cmbState;

    @FXML
    private JFXComboBox<?> cmbStudentTG;

    @FXML
    private TableColumn<?, ?> courseCodeCol;

    @FXML
    private TableColumn<?, ?> dateCol;

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
    private TableColumn<?, ?> stateCol;

    @FXML
    private TableColumn<?, ?> stuTGCol;

    @FXML
    private TableView<?> timeTable;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

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

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        while (resultSet.next()){
            data.add(new String(resultSet.getString(1)));
        }
        cmbCourseCode.setItems(FXCollections.observableArrayList(data));
    }
}
