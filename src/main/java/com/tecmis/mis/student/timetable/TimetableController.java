package com.tecmis.mis.student.timetable;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TimetableController implements Initializable {
    @FXML
    private Stage stage;
    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    @FXML
    private JFXComboBox<String> comboDepartment;

    @FXML
    private JFXComboBox<String> comboLevel;

    @FXML
    private Label error;

    @FXML
    private TableView<TimetableDetails> timeTable;
    @FXML
    private TableColumn<TimetableDetails, String> titleCol;
    @FXML
    private TableColumn<TimetableDetails, Integer> depCol;
    @FXML
    private TableColumn<TimetableDetails, Integer> levelCol;
    @FXML
    private TableColumn<TimetableDetails, String> timeCol;

    @FXML
    private JFXTextField txtMaterials;

    @FXML
    private JFXTextField txtTimetableTitle;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    TimetableDetails timetableDetails = null ;
    byte[] pdfBytes;
    ObservableList<TimetableDetails> timetableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }


    @FXML
    private void refreshTable() {
        try {
            timetableList.clear();
            int userId = Integer.parseInt(UserSession.getUserName());
            query = "SELECT * from department,timetable,users WHERE timetable.depName=department.depId AND users.depId=department.depId AND users.user_id='"+userId+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                timetableList.add(new TimetableDetails(
                        resultSet.getInt("tid"),
                        resultSet.getString("title"),
                        resultSet.getString("depName"),
                        resultSet.getString("short_name"),
                        resultSet.getInt("level"),
                        resultSet.getBytes("pdffile")));
                timeTable.setItems(timetableList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        depCol.setCellValueFactory(new PropertyValueFactory<>("depName"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));

        Callback<TableColumn<TimetableDetails, String>, TableCell<TimetableDetails, String>> cellFoctory = (TableColumn<TimetableDetails, String> param) -> {
            final TableCell<TimetableDetails, String> cell = new TableCell<TimetableDetails, String>() {
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

                            if(timeTable.getSelectionModel().getSelectedItem() != null){
                                timetableDetails = timeTable.getSelectionModel().getSelectedItem();

                                try {
                                    connection = DbConnect.getConnect();
                                    query = "SELECT pdffile FROM timetable WHERE tid='"+timetableDetails.getTid()+"'";
                                    preparedStatement = connection.prepareStatement(query);
                                    resultSet = preparedStatement.executeQuery();

                                    while (resultSet.next()){

                                        InputStream is = resultSet.getBinaryStream("pdffile");
                                        OutputStream os = new FileOutputStream(new File("doc.pdf"));
                                        byte[] content = new byte[1024];
                                        int size = 0;
                                        while ((size = is.read(content)) != -1){
                                            os.write(content,0,size);
                                        }
                                        os.close();
                                        is.close();

                                        String path = "doc.pdf"; // provide the path to the PDF file
                                        File file = new File(path);
                                        Desktop.getDesktop().open(file);

                                    }

                                }catch (Exception e){
                                    System.out.println(e);
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
        timeCol.setCellFactory(cellFoctory);

        timeTable.setItems(timetableList);
    }

    @FXML
    void showPdf(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            timetableDetails = timeTable.getSelectionModel().getSelectedItem();

            try {
                connection = DbConnect.getConnect();
                query = "SELECT pdffile FROM timetable WHERE tid='"+timetableDetails.getTid()+"'";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){

                    InputStream is = resultSet.getBinaryStream("pdffile");
                    OutputStream os = new FileOutputStream(new File("doc.pdf"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content)) != -1){
                        os.write(content,0,size);
                    }
                    os.close();
                    is.close();

                    String path = "doc.pdf"; // provide the path to the PDF file
                    File file = new File(path);
                    Desktop.getDesktop().open(file);

                }

            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
