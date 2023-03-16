package com.tecmis.mis.admin.timetable;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditTimetableControlloer implements Initializable {
    @FXML
    private JFXComboBox<String> comboDepartment;

    @FXML
    private JFXComboBox<String> comboLevel;

    @FXML
    private JFXTextField txtMaterials;

    @FXML
    private JFXTextField txtTimetableTitle;

    @FXML
    private Label error;

    @FXML
    private Stage stage;
    private Scene scene;
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    int tid;
    byte[] pdfBytes;

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
                int id = tid;

                connection = DbConnect.getConnect();
                query = "UPDATE timetable SET title = ?, depId= ?, level = ?, pdffile = ? WHERE tid = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,title);
                preparedStatement.setInt(2,department);
                preparedStatement.setInt(3,level);
                preparedStatement.setBytes(4,pdf);
                preparedStatement.setInt(5,id);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Update");
                alert.setContentText("successfully update Time Table");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-timetable.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.close();
                }

            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboDepartment.setItems(FXCollections.observableArrayList("Engineering Technology","Information & Communication Technology","Biosystems Technology"));
        comboLevel.setItems(FXCollections.observableArrayList("Level 01","Level 02","Level 03","Level 04"));
    }

    @FXML
    void clearTimetable(ActionEvent event) {
        reset();
    }

    private void reset(){
        txtTimetableTitle.setText("");
        txtMaterials.setText("");
    }
    public void showInformation(int tid, String title, byte[] pdf){
        this.tid = tid;
        txtTimetableTitle.setText(title);
    }
}
