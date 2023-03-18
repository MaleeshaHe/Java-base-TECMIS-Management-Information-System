package com.tecmis.mis.student.course;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.admin.course.EditCourseControlloer;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.admin.notice.NoticeDetails;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseControlloer implements Initializable {

    @FXML
    private JFXComboBox<String> comboCourse;
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
        comboboxDataLoad();
    }

    private void comboboxDataLoad(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM course WHERE depId=4 OR depId="+UserSession.getUserDepId()+"";
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

    @FXML
    private void refreshTable() {
        try {
            courseList.clear();
            String tg = UserSession.getUserTgNum();
            query = "SELECT * FROM studentcourse,course WHERE course.courseCode=studentcourse.courseCode AND tgnum='"+tg+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                courseList.add(new CourseDetails(
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
                        Image image = new Image(getClass().getResourceAsStream("..\\..\\..\\..\\..\\images\\delete-24.png"));
                        JFXButton deleteBtn = new JFXButton("");
                        deleteBtn.setGraphic(new ImageView(image));

                        openMaterial.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-border-radius:5;"
                                        + "-fx-max-width:80;"
                                        +"-fx-text-fill:#000000;"
                                        + "-fx-border-color:#cecece;"
                        );

                        deleteBtn.setOnMouseClicked((MouseEvent event) -> {
                            deleteC();
                        });

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

                        HBox managebtn = new HBox(openMaterial,deleteBtn);
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

    @FXML
    private void enrollCourse(){
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

        }catch (Exception e){
            System.out.println(e);
        }

        try {
            connection = DbConnect.getConnect();
            query = "INSERT INTO studentcourse (tgnum,courseCode) VALUES (?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,UserSession.getUserTgNum());
            preparedStatement.setString(2,coursecode);
            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("successfull");
            alert.setContentText("successfully Enroll course");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                loadData();
            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Already added this course");
            alert.showAndWait();
        }
    }

    public void deleteC(){
        if(courseTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Are you sure delete this one");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                try {
                    courseDetails = courseTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM `studentcourse` WHERE courseCode='" + courseDetails.getCourseCode() + "'";
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
            alert.setContentText("If you want to delete, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }
}
