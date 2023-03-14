package com.tecmis.mis.technical_officer.medical;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.admin.timetable.TimetableDetails;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicalController implements Initializable {

    @FXML
    private DatePicker date;

    @FXML
    private TableColumn<MedicalDetails, String> dateCol;

    @FXML
    private JFXButton deleteNotice;

    @FXML
    private TableColumn<MedicalDetails, String> depCol;

    @FXML
    private TableColumn<MedicalDetails, String> docCol;

    @FXML
    private Label error;

    @FXML
    private TableView<MedicalDetails> medicalTable;

    @FXML
    private TableColumn<MedicalDetails, String> titleCol;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtDoc;

    @FXML
    private JFXTextField txtTitle;

    @FXML
    private JFXTextField txtStudentTg;


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

    @FXML
    void refreshTable() {
        try {
            medicalList.clear();

            query = "SELECT * from medical";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                medicalList.add(new MedicalDetails(
                        resultSet.getInt("m_id"),
                        resultSet.getString("m_title"),
                        resultSet.getString("m_description"),
                        resultSet.getString("start_date"),
                        resultSet.getBytes("document"),
                        resultSet.getString("end_date"),
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
        depCol.setCellValueFactory(new PropertyValueFactory<>("students_tg"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("start_date"));

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
        docCol.setCellFactory(cellFoctory);
        medicalTable.setItems(medicalList);
    }

    @FXML
    void showPdf(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
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
    }

    @FXML
    void choosePdf(ActionEvent event) {
        try {
            FileChooser fileopen = new FileChooser();
            File pdfFile = fileopen.showOpenDialog(stage);

            pdfBytes = readPDF(pdfFile);
            txtDoc.setText(pdfFile.getName());

        } catch (Exception e) {
        }
    }
    private byte[] readPDF(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            return outputStream.toByteArray();
        }
    }

    @FXML
    void uploadMedical(ActionEvent event) {
        if(txtStudentTg.getText().length() == 0 ||txtTitle.getText().length() == 0 || txtDescription.getText().length() == 0 || txtDoc.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {
            try {
                String tgnum = txtStudentTg.getText();
                String title = txtTitle.getText();
                String description = txtDescription.getText();

                LocalDate birtDate = date.getValue();
                String date = String.valueOf(birtDate);

                byte[] pdf = pdfBytes;

                connection = DbConnect.getConnect();
                query = "INSERT INTO medical (students_tg,m_title,m_description,start_date,document) VALUES (?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,tgnum);
                preparedStatement.setString(2,title);
                preparedStatement.setString(3,description);
                preparedStatement.setString(4,date);
                preparedStatement.setBytes(5,pdf);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Upload");
                alert.setContentText("successfully upload Medical");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    clear();
                }

            }catch (Exception e){
                System.out.println(e);
            }
            loadData();
        }
    }

    @FXML
    void clear() {

    }

    @FXML
    void deleteTimetable(ActionEvent event) {

    }

    @FXML
    void editTimetable(ActionEvent event) {

    }

}
