package com.tecmis.mis.technical_officer.attendance;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.technical_officer.medical.EditMedicalControlloer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TableColumn<Attendance, String> dateCol;

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
        dtpDate.setValue(null);
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
                        resultSet.getString("date")));
                table_attendance.setItems(attlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("Course_Code"));
        stuTGCol.setCellValueFactory(new PropertyValueFactory<>("Student_TG"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("Attendance_State"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("AttDate"));
        table_attendance.setItems(attlist);
    }

    public void addAttendance(){
        if(cmbCourseCode.getValue() == null || cmbStudentTG.getValue() == null || cmbState.getValue() == null || dtpDate.getValue() == null){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else{
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

    @FXML
    void setEditAttendance(javafx.event.ActionEvent event) {
        if(table_attendance.getSelectionModel().getSelectedItem() != null){
            try{
                Attendance attendance = table_attendance.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-attendance.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                EditAttendanceControlloer senddata = fxmlLoader.getController();
                senddata.showInformation(attendance.getCourse_Code(),attendance.getStudent_TG(),attendance.getAttendance_State(),attendance.getAttDate());

                Stage stage = new Stage();
                stage.setTitle("Edit Attendance");
                javafx.scene.image.Image image = new Image("images/appIcon.png");
                stage.getIcons().add(image);
                stage.resizableProperty().setValue(false);
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to update any Attendance, First you select the row that you want to update");
            alert.showAndWait();
        }
    }

    @FXML
    void setDeleteAttendance(javafx.event.ActionEvent event) {
        if(table_attendance.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Attendance");
            alert.setContentText("Are you sure delete this Attendance");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    Attendance attendance = table_attendance.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM `attendance` WHERE CourseCode='"+attendance.getCourse_Code()+"'AND tgnum='"+attendance.getStudent_TG()+"' AND State='"+attendance.getAttendance_State()+"' AND date='"+attendance.getAttDate()+"'";
                    connection = DbConnect.getConnect();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    loadAttendance();

                } catch (SQLException ex) {
                    Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to delete any Attendance, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }
}
