package com.tecmis.mis.admin.course;

import com.jfoenix.controls.JFXButton;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.admin.notice.NoticeDetails;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private TableColumn<CourseDetails, String> materialCol;

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
    private void editCourse(){
        if(courseTable.getSelectionModel().getSelectedItem() != null){
            try {
                courseDetails = courseTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-course.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                EditCourseControlloer senddata = fxmlLoader.getController();
                senddata.showInformation(courseDetails.getCid(),courseDetails.getCourseCode(),courseDetails.getCourseName(),courseDetails.getCredit(),courseDetails.getMaterial());

                Stage stage = new Stage();
                stage.setTitle("Edit Course");
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
            alert.setContentText("If you want to update any course, First you select the row that you want to update");
            alert.showAndWait();
        }

    }
    @FXML
    private void addCourse(){
        try {
            courseDetails = courseTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-course.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create a new course");
            stage.resizableProperty().setValue(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        Callback<TableColumn<CourseDetails, String>, TableCell<CourseDetails, String>> cellFoctory = (TableColumn<CourseDetails, String> param) -> {
            final TableCell<CourseDetails, String> cell = new TableCell<CourseDetails, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        Hyperlink openMaterial = new Hyperlink("Open");
                        openMaterial.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-border-radius:5;"
                                        + "-fx-max-width:80;"
                                        +"-fx-text-fill:#000000;"
                                        + "-fx-border-color:#cecece;"
                        );

                        openMaterial.setOnMouseClicked((MouseEvent event) -> {

                            if(courseTable.getSelectionModel().getSelectedItem() != null){
                                    courseDetails = courseTable.getSelectionModel().getSelectedItem();
                                    Desktop desktop = Desktop.getDesktop();
                                    try {
                                        desktop.browse(URI.create(courseDetails.getMaterial()));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                            }
                            else{
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning");
                                alert.setContentText("If you want to open any course material, First you select the row that you want to open");
                                alert.showAndWait();
                            }
                        });

                        HBox managebtn = new HBox(openMaterial);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(openMaterial, new Insets(2, 2, 3, 3));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        materialCol.setCellFactory(cellFoctory);
        courseTable.setItems(courseList);
    }
}
