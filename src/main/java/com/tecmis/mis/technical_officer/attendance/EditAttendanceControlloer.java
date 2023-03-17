package com.tecmis.mis.technical_officer.attendance;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditAttendanceControlloer implements Initializable {
    @FXML
    private JFXButton btnEditAttendance;

    @FXML
    private JFXButton btnEditClear;
    @FXML
    private TableView<Attendance> table_attendance;

    @FXML
    private JFXComboBox<Attendance> cmbEditCourseCode;

    @FXML
    private JFXComboBox<Attendance> cmbEditState;

    @FXML
    private JFXComboBox<Attendance> cmbEditStudentTG;

    @FXML
    private DatePicker dtpEditDate;

    @FXML
    private Label errorEdit;

    @FXML
    private Stage stage;
    private Scene scene;
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setState();
        setComboCourseCode();
        setComboStudentTG();
    }
    @FXML
    void clear(ActionEvent event) {
        reset();
        setState();
        setComboCourseCode();
        setComboStudentTG();
    }

    public void reset(){
        cmbEditCourseCode.setItems(FXCollections.emptyObservableList());
        cmbEditStudentTG.setItems(FXCollections.emptyObservableList());
        cmbEditState.setItems(FXCollections.emptyObservableList());
        dtpEditDate.setValue(null);
    }

    public void setState(){
        ObservableList stateList = FXCollections.observableArrayList("Present","Absent");
        cmbEditState.setItems(stateList);
    }

    @FXML
    void uploadAttendance(ActionEvent event) {

        if(cmbEditCourseCode.getValue() == null || cmbEditCourseCode.getValue() == null || cmbEditState.getValue() == null || dtpEditDate.getValue() == null){
            new Shake(errorEdit).play();
            errorEdit.setText("Please fill the all Fields");
        }
        else {
            try {
                String eCourseCode = cmbEditCourseCode.getValue().toString();
                String eStudentTG = cmbEditStudentTG.getValue().toString();
                String eState = cmbEditState.getValue().toString();
                String eDate = String.valueOf(dtpEditDate.getValue());

                connection = DbConnect.getConnect();
                query = "UPDATE attendance SET CourseCode = ?, tgnum= ?, State = ?, date = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,eCourseCode);
                preparedStatement.setString(2,eStudentTG);
                preparedStatement.setString(3,eState);
                preparedStatement.setString(4,eDate);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Update");
                alert.setContentText("successfully update Attendance");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-attendance.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.close();
                }

            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void showInformation(String courseCode, String studentTg, String attendanceState, String attDate) {
        cmbEditCourseCode.setAccessibleText(courseCode);
        cmbEditStudentTG.setAccessibleText(studentTg);
        cmbEditState.setAccessibleText(attendanceState);
        dtpEditDate.setValue(LocalDate.parse(attDate));
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
        cmbEditCourseCode.setItems(FXCollections.observableArrayList(data));
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
        cmbEditStudentTG.setItems(FXCollections.observableArrayList(data));
    }


}
