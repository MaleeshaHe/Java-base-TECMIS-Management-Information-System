package com.tecmis.mis.admin.course;

import com.jfoenix.controls.JFXButton;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.admin.notice.NoticeDetails;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseControlloer implements Initializable {
    @FXML
    private TableColumn<NoticeDetails, String> materialCol;

    @FXML
    private TableView<CourseDetails> courseTable;
    @FXML
    private TableColumn<NoticeDetails, String> courseCodeCol;

    @FXML
    private TableColumn<NoticeDetails, String> courseNameCol;

    @FXML
    private TableColumn<NoticeDetails, Integer> creditCol;
    @FXML
    private JFXButton addNewCourse;

    @FXML
    private JFXButton deleteCourse;

    @FXML
    private JFXButton editCourse;

    @FXML
    private JFXButton refreshBtn;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    CourseDetails courseDetails = null ;
    ObservableList<CourseDetails> courseList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    @FXML
    private void deleteCourse(){

        if(courseTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Course");
            alert.setContentText("Are you sure delete this Course");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    courseDetails = courseTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM `course` WHERE cId='"+courseDetails.getCid()+"'";
                    connection = DbConnect.getConnect();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();

                } catch (SQLException ex) {
                    Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to delete any course, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }

    @FXML
    private void refreshTable() {
        try {
            courseList.clear();

            query = "SELECT * FROM `course`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                courseList.add(new CourseDetails(
                        resultSet.getInt("cId"),
                        resultSet.getString("courseCode"),
                        resultSet.getString("courseName"),
                        resultSet.getInt("credit"),
                        resultSet.getString("material")));
                courseTable.setItems(courseList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        creditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        materialCol.setCellValueFactory(new PropertyValueFactory<>("material"));
        courseTable.setItems(courseList);
    }
}
