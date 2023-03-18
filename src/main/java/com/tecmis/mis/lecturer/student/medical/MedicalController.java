package com.tecmis.mis.lecturer.student.medical;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class MedicalController implements Initializable{
    @FXML
    private TableColumn<MedicalDetails, String> tgnumCol;
    @FXML
    private TableColumn<MedicalDetails,String> stNameCol;
    @FXML
    private TableColumn<MedicalDetails ,String> medStartDateCol;
    @FXML
    private TableColumn<MedicalDetails ,String> medEndDateCol;
    @FXML
    private TableColumn<MedicalDetails , String>  medDcoCol;
    @FXML
    private TableColumn<MedicalDetails , String>  medTitleCol;
    @FXML
    private TableView<MedicalDetails> medicalTable;
    @FXML
    public JFXComboBox<String> comboStudent;
    @FXML
    private JFXTextField txtKeyword;

    @FXML
    private Stage stage;
    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    MedicalDetails medicalDetails = null ;
    byte[] pdfBytes;
    ObservableList<MedicalDetails> medicalList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void refreshTable(){
        try {
            medicalList.clear();
            int depId= Integer.parseInt(UserSession.getUserDepId());
            query="SELECT * from medical,user WHERE medical.students_tg=user.tgnum AND user.depId="+depId+"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                medicalList.add(new MedicalDetails(
                        resultSet.getInt("m_id"),
                    resultSet.getString("tgnum"),
                    resultSet.getString("fname"),
                    resultSet.getString("lname"),
                    resultSet.getString("m_title"),
                    resultSet.getString("start_date"),
                    resultSet.getString("end_date"),
                    resultSet.getBytes("document")));
                medicalTable.setItems(medicalList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void loadData(){
        connection = DbConnect.getConnect();
        refreshTable();
        tgnumCol.setCellValueFactory(new PropertyValueFactory<>("tgnum"));
        stNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        medTitleCol.setCellValueFactory(new PropertyValueFactory<>("m_title"));
        medStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        //medEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        Callback<TableColumn<MedicalDetails, String>, TableCell<MedicalDetails, String>> cellFoctory = (TableColumn<MedicalDetails, String> param) -> {
            final TableCell<MedicalDetails, String> cell = new TableCell<MedicalDetails, String>() {
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

                            if(medicalTable.getSelectionModel().getSelectedItem() != null){
                                medicalDetails = medicalTable.getSelectionModel().getSelectedItem();

                                try {
                                    connection = DbConnect.getConnect();
                                    query = "SELECT document FROM medical WHERE m_id='"+medicalDetails.getM_id()+"'";
                                    preparedStatement = connection.prepareStatement(query);
                                    resultSet = preparedStatement.executeQuery();

                                    while (resultSet.next()){

                                        InputStream is = resultSet.getBinaryStream("document");
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
                                alert.setContentText("If you want to open any Medical Document, First you select the row that you want to open");
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
        medDcoCol.setCellFactory(cellFoctory);
        medicalTable.setItems(medicalList);

        medicalTable.setItems(medicalList);
        new FadeIn(medicalTable).play();

        FilteredList<MedicalDetails> filteredData = new FilteredList<>(medicalList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(MedicalDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (MedicalDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (MedicalDetails.getLname().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (MedicalDetails.getTgnum().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (MedicalDetails.getStartDate().indexOf(searchKeyword)>- 1 ) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<MedicalDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(medicalTable.comparatorProperty());
        medicalTable.setItems(sortedData);
        new FadeIn(medicalTable).play();
    }



}
