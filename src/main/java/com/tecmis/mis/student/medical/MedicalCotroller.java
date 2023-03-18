package com.tecmis.mis.student.medical;

import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.student.notice.NoticeController;
import com.tecmis.mis.student.notice.NoticeDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicalCotroller implements Initializable {
    @FXML
    private TableColumn<MedicalDetails, String> descCol;

    @FXML
    private TableColumn<com.tecmis.mis.technical_officer.medical.MedicalDetails, String> edateCol;

    @FXML
    private TableView<MedicalDetails> medicalTable;

    @FXML
    private TableColumn<MedicalDetails, String> sdateCol;

    @FXML
    private TableColumn<MedicalDetails, String> titleCol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    NoticeDetails noticeDetails = null ;

    ObservableList<MedicalDetails> medicalList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    @FXML
    private void refreshTable() {
        try {
            medicalList.clear();
            String tg = UserSession.getUserTgNum();
            query = "SELECT * FROM medical WHERE students_tg='"+tg+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                medicalList.add(new MedicalDetails(
                        resultSet.getString("m_title"),
                        resultSet.getString("m_description"),
                        resultSet.getString("start_date"),
                        resultSet.getString("end_date"),
                        resultSet.getInt("m_id"),
                        resultSet.getString("students_tg")));
                medicalTable.setItems(medicalList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();
        titleCol.setCellValueFactory(new PropertyValueFactory<>("m_title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("m_description"));
        sdateCol.setCellValueFactory(new PropertyValueFactory<>("start_date"));

        Callback<TableColumn<com.tecmis.mis.technical_officer.medical.MedicalDetails, String>, TableCell<com.tecmis.mis.technical_officer.medical.MedicalDetails, String>> cellFoctory = (TableColumn<com.tecmis.mis.technical_officer.medical.MedicalDetails, String> param) -> {
            final TableCell<com.tecmis.mis.technical_officer.medical.MedicalDetails, String> cell = new TableCell<com.tecmis.mis.technical_officer.medical.MedicalDetails, String>() {
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

                                try {
                                    connection = DbConnect.getConnect();
                                    query = "SELECT document FROM medical WHERE students_tg='"+UserSession.getUserTgNum()+"'";
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
        edateCol.setCellFactory(cellFoctory);
        medicalTable.setItems(medicalList);
    }
}
