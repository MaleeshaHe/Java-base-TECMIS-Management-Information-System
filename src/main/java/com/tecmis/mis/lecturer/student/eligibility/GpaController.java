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
    ResultSet resultSet1 = null;
    GpaDetails gpaDetails = null;
    float credit=2,totGPV=0;
    int totCredit;
    int sem;
    String grade;
    float sgpa,cgpa;
    ObservableList<GpaDetails> gpaList = FXCollections.observableArrayList();
    ObservableList<FinalMarksDetails> finalMarksList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();
        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        sgpaCol.setCellValueFactory(new PropertyValueFactory<>("sgpa"));
        cgpaCol.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
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
            try{
                query="SELECT user.tgnum,user.fname,course.courseCode,course.courseName,SUM(studentmark.marks) AS 'Sum' FROM user,studentmark,course,examination,studentcourse WHERE user.tgnum=studentmark.tgnum AND user.depId=course.depId AND examination.ExamId=studentmark.examId AND studentcourse.courseCode=course.courseCode AND studentcourse.tgnum=user.tgnum AND examination.CourseCode=course.courseCode AND user.depId='"+depId+"' GROUP BY studentmark.tgnum,course.courseName";
                preparedStatement = connection.prepareStatement(query);
                resultSet1 = preparedStatement.executeQuery();

                while(resultSet1.next()){
                    if(resultSet1.getFloat("Sum")>=90){

                    }else if (resultSet1.getFloat("Sum") >= 85) {
                        grade= "A";
                    }else if (resultSet1.getFloat("Sum") >= 80) {
                        grade= "A-";
                    } else if (resultSet1.getFloat("Sum") >= 75) {
                        grade= "B+";
                    } else if (resultSet1.getFloat("Sum") >= 70) {
                        grade= "B";
                    } else if (resultSet1.getFloat("Sum") >= 65) {
                        grade= "C+";
                    }else if (resultSet1.getFloat("Sum") >= 60) {
                        grade= "C";
                    }else if (resultSet1.getFloat("Sum") >= 55) {
                        grade= "D+";
                    }else if (resultSet1.getFloat("Sum") >= 50) {
                        grade= "D";
                    }else if (resultSet1.getFloat("Sum") >= 40) {
                        grade= "E";
                    } else {
                        grade= "F";
                    }
                    totCredit+=credit;
                    switch (grade){
                        case "A+":
                        case "A":
                            totGPV+=(4.00*credit);
                            break;
                        case "A-":
                            totGPV+=(3.70*credit);
                            break;
                        case "B+":
                            totGPV+=(3.30*credit);
                            break;
                        case "B":
                            totGPV+=(3.00*credit);
                            break;
                        case "B-":
                            totGPV+=(2.70*credit);
                            break;
                        case "C+":
                            totGPV+=(2.30*credit);
                            break;
                        case "C":
                            totGPV+=(2.00*credit);
                            break;
                        case "C-":
                            totGPV+=(1.70*credit);
                            break;
                        case "D+":
                            totGPV+=(1.30*credit);
                            break;
                        case "D":
                            totGPV+=(1.00*credit);
                            break;
                        case "E":
                            totGPV+=(0.00*credit);
                            break;
                        case "F":
                            System.out.println("CA Fail");
                            break;
                        default:
                            System.out.println("Default");
                            break;
                    }
                    sgpa=totGPV/totCredit;
                }

            }catch (Exception e){
                throw new RuntimeException(e);
            }
            while (resultSet.next()){
                gpaList.add(new GpaDetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        sgpa));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
