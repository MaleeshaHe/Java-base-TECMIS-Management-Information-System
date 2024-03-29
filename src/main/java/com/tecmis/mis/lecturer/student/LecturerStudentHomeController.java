package com.tecmis.mis.lecturer.student;


import animatefx.animation.FadeIn;
import animatefx.animation.FadeInDown;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.jdbc.MysqlSQLXML;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.lecturer.student.attendance.AttendanceDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LecturerStudentHomeController implements Initializable {
    @FXML
    private Label tgnum;
    @FXML
    private Label name;
    @FXML
    private Label email;
    @FXML
    private Label phonenum;
    @FXML
    private Label depName;
    @FXML
    private Circle pic;
    @FXML
    private BorderPane borderpane;
    @FXML
    private BorderPane borderpane2;
    @FXML
    private TableView<LecturerStudentDetails> studentTable;
    @FXML
    private TableColumn<LecturerStudentDetails, String> tgnumCol;
    @FXML
    private TableColumn<LecturerStudentDetails, String> stNameCol;
    @FXML
    private TableColumn<LecturerStudentDetails, String> stPhoneCol;
    @FXML
    private JFXTextField txtKeyword;
    @FXML
    private JFXComboBox<String> comboStudent;
    @FXML
    private Label heading;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    Connection connection = null ;
    String query = null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    LecturerStudentDetails lecturerStudentDetails = null;
    ObservableList<LecturerStudentDetails> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataToTable();
        showData();
    }

    private void showData(){
        try {
            int depId= Integer.parseInt(UserSession.getUserDepId());
            connection = DbConnect.getConnect();
            query = "SELECT * FROM user,department WHERE user.depId=department.depId AND user_roll='Student' AND user.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                tgnum.setText(": "+resultSet.getString("tgnum"));
                name.setText(": "+resultSet.getString("fname")+" "+resultSet.getString("lname"));
                email.setText(": "+resultSet.getString("email"));
                phonenum.setText(": "+resultSet.getString("phone_num"));
                depName.setText(": "+resultSet.getString("short_name"));

                InputStream is = resultSet.getBinaryStream("profile_pic");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1){
                    os.write(content,0,size);
                }
                os.close();
                is.close();

                pic.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void refreshTable(){
        try {
            studentList.clear();
            int depId= Integer.parseInt(UserSession.getUserDepId());
            query = "SELECT * FROM user,department WHERE user.depId=department.depId AND user_roll='Student' AND user.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                studentList.add(new LecturerStudentDetails(
                        resultSet.getString("tgnum"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getString("phone_num")));
                studentTable.setItems(studentList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void loadDataToTable(){
        connection = DbConnect.getConnect();
        refreshTable();

        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        stPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        studentTable.setItems(studentList);
        new FadeIn(studentTable).play();

        FilteredList<LecturerStudentDetails> filteredData = new FilteredList<>(studentList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(LecturerStudentDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (LecturerStudentDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (LecturerStudentDetails.getLname().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (LecturerStudentDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (LecturerStudentDetails.getPhoneNum().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<LecturerStudentDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(studentTable.comparatorProperty());
        studentTable.setItems(sortedData);
        new FadeIn(studentTable).play();
    }
    private void showData(String tg){
        try {
            int depId= Integer.parseInt(UserSession.getUserDepId());
            connection = DbConnect.getConnect();
            query = "SELECT * FROM user,department WHERE user.depId=department.depId AND user_roll='Student' AND user.depId="+depId+" AND user.tgnum='"+tg+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                tgnum.setText(": "+resultSet.getString("tgnum"));
                name.setText(": "+resultSet.getString("fname")+" "+resultSet.getString("lname"));
                email.setText(": "+resultSet.getString("email"));
                phonenum.setText(": "+resultSet.getString("phone_num"));
                depName.setText(": "+resultSet.getString("short_name"));

                InputStream is = resultSet.getBinaryStream("profile_pic");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1){
                    os.write(content,0,size);
                }
                os.close();
                is.close();

                pic.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void btnStudentDetailsView(ActionEvent actionEvent)throws IOException{
        try{
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lecturer-student-home.fxml")));
            borderpane.getChildren().removeAll();
            borderpane.setCenter(view);
            new FadeInDown(view).play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void viewStudent(MouseEvent event){
        if(event.isPrimaryButtonDown() && event.getClickCount() == 2){
            try{
                lecturerStudentDetails = studentTable.getSelectionModel().getSelectedItem();
                showData(lecturerStudentDetails.getTgnum());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void btnStudentEligibilityView(ActionEvent actionEvent)throws IOException{
        borderpane2.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("eligibility/eligibility.fxml")));
        borderpane2.getChildren().removeAll();
        borderpane2.setCenter(view);
        new FadeInDown(view).play();
    }
    public void btnStudentMarksView(ActionEvent actionEvent)throws IOException{
        borderpane2.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("marks/marks.fxml")));
        borderpane2.getChildren().removeAll();
        borderpane2.setCenter(view);
        new FadeInDown(view).play();
    }
    public void btnStudentAttendanceView(ActionEvent actionEvent)throws IOException{
        borderpane2.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("attendance/attendance.fxml")));
        borderpane2.getChildren().removeAll();
        borderpane2.setCenter(view);
        new FadeInDown(view).play();
    }
    public void btnStudentMedicalView(ActionEvent actionEvent)throws IOException{
        borderpane2.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("medical/medical.fxml")));
        borderpane2.getChildren().removeAll();
        borderpane2.setCenter(view);
        new FadeInDown(view).play();
    }
}
