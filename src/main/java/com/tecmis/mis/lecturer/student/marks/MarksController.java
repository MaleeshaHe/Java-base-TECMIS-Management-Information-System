package com.tecmis.mis.lecturer.student.marks;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
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

public class MarksController implements Initializable {
    @FXML
    private JFXTextField txtKeyword;
    @FXML
    private TableColumn<MarksDetails, String> tgnumCol;
    @FXML
    private TableColumn<MarksDetails, String> stNameCol;
    @FXML
    private TableColumn<MarksDetails, String> courseCol;
    @FXML
    private TableColumn<MarksDetails, String> examCol;
    @FXML
    private TableColumn<MarksDetails, String> marksCol;
    @FXML
    private TableView<MarksDetails> marksTable;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    MarksDetails marksDetails=null;
    ObservableList<MarksDetails> marksList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void loadData(){
        connection = DbConnect.getConnect();
        refreshTable();
        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        examCol.setCellValueFactory(new PropertyValueFactory<>("examtype"));
        marksCol.setCellValueFactory(new PropertyValueFactory<>("marks"));

        marksTable.setItems(marksList);
        new FadeIn(marksTable).play();

        FilteredList<MarksDetails> filteredData = new FilteredList<>(marksList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(MarksDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (MarksDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (MarksDetails.getExamtype().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (MarksDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (MarksDetails.getCourseName().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else if (MarksDetails.getCourseCode().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<MarksDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(marksTable.comparatorProperty());
        marksTable.setItems(sortedData);
        new FadeIn(marksTable).play();
    }
    public void refreshTable(){
        try{
            marksList.clear();
            int depId= Integer.parseInt(UserSession.getUserDepId());
            query="SELECT * FROM studentmark, USER, course, examination WHERE studentmark.tgnum = USER.tgnum AND examination.CourseCode = course.courseCode AND examination.ExamId=studentmark.ExamId AND user.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                marksList.add(new MarksDetails(
                        resultSet.getString("ExamId"),
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getString("courseName"),
                        resultSet.getString("courseCode"),
                        resultSet.getString("ExamType"),
                        resultSet.getFloat("Marks")));
                marksTable.setItems(marksList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void addMarks(){
        try {
            marksDetails = marksTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-marks.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Marks");
            javafx.scene.image.Image image = new Image("images/appIcon.png");
            stage.getIcons().add(image);
            stage.resizableProperty().setValue(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MarksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void editMarks(){
        if(marksTable.getSelectionModel().getSelectedItem() != null){
            try {
                marksDetails = marksTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-marks.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                EditMarksController senddata = fxmlLoader.getController();
                senddata.showInformation(marksDetails.getExamId(),marksDetails.getExamtype(),marksDetails.getCourseName(),marksDetails.getTgnum(),marksDetails.getMarks());

                Stage stage = new Stage();
                stage.setTitle("Edit Marks");
                javafx.scene.image.Image image = new Image("images/appIcon.png");
                stage.getIcons().add(image);
                stage.resizableProperty().setValue(false);
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException ex) {
                Logger.getLogger(MarksController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to update any notice, First you select the row that you want to update");
            alert.showAndWait();
        }

    }
    @FXML
    private void deleteMarks(javafx.event.ActionEvent event) throws SQLException {
        if(marksTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Marks");
            alert.setContentText("Are you sure to delete this Marks");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                try {
                    marksDetails = marksTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM `studentmark` WHERE tgnum='" + marksDetails.getTgnum() + "' AND ExamId='" + marksDetails.getExamId() + "'";
                    connection = DbConnect.getConnect();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("If you want to delete any student marks, First you select the row that you want to delete");
                alert.showAndWait();
        }
    }

}
