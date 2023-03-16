package com.tecmis.mis.admin.timetable;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.admin.course.CourseDetails;
import com.tecmis.mis.admin.notice.NoticeController;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.student.notice.ViewNoticeControlloer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
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
        comboDepartment.setItems(FXCollections.observableArrayList("Engineering Technology","Information & Communication Technology","Biosystems Technology"));
        comboLevel.setItems(FXCollections.observableArrayList("Level 01","Level 02","Level 03","Level 04"));
    }

    @FXML
    private void editTimetable() throws IOException {
        if(timeTable.getSelectionModel().getSelectedItem() != null){
            try{
                timetableDetails = timeTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-timetable.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                EditTimetableControlloer senddata = fxmlLoader.getController();
                senddata.showInformation(timetableDetails.getTid(),timetableDetails.getTitle(),timetableDetails.getPdffile());

                Stage stage = new Stage();
                stage.setTitle("Edit Time Table");
                javafx.scene.image.Image image = new Image("images/appIcon.png");
                stage.getIcons().add(image);
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
            alert.setContentText("If you want to update any Time Table, First you select the row that you want to update");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteTimetable(){

        if(timeTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Time Table");
            alert.setContentText("Are you sure delete this Time Table");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    timetableDetails = timeTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM `timetable` WHERE tid='"+timetableDetails.getTid()+"'";
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
            alert.setContentText("If you want to delete any Timetable, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }

    @FXML
    void choosePdf(ActionEvent event) {
        try {
            FileChooser fileopen = new FileChooser();
            File pdfFile = fileopen.showOpenDialog(stage);

            pdfBytes = readPDF(pdfFile);
            txtMaterials.setText(pdfFile.getName());

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
    void clear(ActionEvent event) {
        reset();
    }

    @FXML
    void uploadPdf(ActionEvent event) {

        if(txtTimetableTitle.getText().length() == 0 || txtMaterials.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {
            try {
                String title = txtTimetableTitle.getText();
                int department = comboDepartment.getSelectionModel().getSelectedIndex()+1;
                int level = comboLevel.getSelectionModel().getSelectedIndex()+1;
                byte[] pdf = pdfBytes;

                connection = DbConnect.getConnect();
                query = "INSERT INTO timetable (title,depId,level,pdffile) VALUES (?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,title);
                preparedStatement.setInt(2,department);
                preparedStatement.setInt(3,level);
                preparedStatement.setBytes(4,pdf);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Upload");
                alert.setContentText("successfully upload new Time Table");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                }

            }catch (Exception e){
                System.out.println(e);
            }
            loadData();
        }
    }

    private void reset(){
        txtTimetableTitle.setText("");
        txtMaterials.setText("");
   }

    @FXML
    private void refreshTable() {
        try {
            timetableList.clear();

            query = "SELECT * from department,timetable WHERE timetable.depId=department.depId";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                timetableList.add(new TimetableDetails(
                        resultSet.getInt("tid"),
                        resultSet.getString("title"),
                        resultSet.getInt("depId"),
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
        depCol.setCellValueFactory(new PropertyValueFactory<>("short_name"));
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
