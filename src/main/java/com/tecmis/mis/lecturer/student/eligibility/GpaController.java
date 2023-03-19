package com.tecmis.mis.lecturer.student.eligibility;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class GpaController implements Initializable {
    @FXML
    private TableView<GpaDetails> gpaTable;
    @FXML
    private TableColumn<GpaDetails, String> tgnumCol;
    @FXML
    private TableColumn<GpaDetails, String> stNameCol;
    @FXML
    private TableColumn<GpaDetails, String> sgpaCol;
    @FXML
    private TableColumn<GpaDetails, String> cgpaCol;
    @FXML
    private JFXTextField txtKeyword;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    GpaDetails gpaDetails = null;
    ObservableList<GpaDetails> gpaList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();
        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        sgpaCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        cgpaCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        gpaTable.setItems(gpaList);

        new FadeIn(gpaTable).play();

        FilteredList<GpaDetails> filteredData = new FilteredList<>(gpaList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(GpaDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (GpaDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }  else if (GpaDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                }  else {
                    return false;
                }

            });
        });

        SortedList<GpaDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(gpaTable.comparatorProperty());
        gpaTable.setItems(sortedData);
        new FadeIn(gpaTable).play();
    }
    public void refreshTable(){
        gpaList.clear();
        try{
            connection = DbConnect.getConnect();
            int depId=Integer.parseInt(UserSession.getUserDepId());
            query="SELECT user.tgnum,user.fname,SUM(studentmark.marks) AS 'Sum' FROM user,studentmark,course,examination,studentcourse WHERE user.tgnum=studentmark.tgnum AND user.depId=course.depId AND examination.ExamId=studentmark.examId AND studentcourse.courseCode=course.courseCode AND studentcourse.tgnum=user.tgnum AND examination.CourseCode=course.courseCode AND user.depId='"+depId+"' GROUP BY studentmark.tgnum";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                gpaList.add(new GpaDetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getFloat("Sum")));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
