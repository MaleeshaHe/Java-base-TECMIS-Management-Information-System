package com.tecmis.mis.technical_officer.medical;

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
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditMedicalControlloer implements Initializable {
    @FXML
    private DatePicker date;

    @FXML
    private Label error;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtDoc;

    @FXML
    private JFXTextField txtStudentTg;

    @FXML
    private JFXTextField txtTitle;

    @FXML
    private Stage stage;
    private Scene scene;
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    int mid;
    byte[] pdfBytes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    void uploadPdf(ActionEvent event) {

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
                query = "UPDATE medical SET students_tg = ?, m_title= ?, m_description = ?, start_date = ? , document = ? WHERE m_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,tgnum);
                preparedStatement.setString(2,title);
                preparedStatement.setString(3,description);
                preparedStatement.setString(4,date);
                preparedStatement.setBytes(5,pdf);
                preparedStatement.setInt(6,mid);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Update");
                alert.setContentText("successfully update Medical");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-medical.fxml")));
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


    @FXML
    void clear(ActionEvent event) {
        reset();
    }


    private void reset(){
        txtStudentTg.setText("");
        txtTitle.setText("");
        txtDescription.setText("");
        txtDoc.setText("");
    }

    public void showInformation(int mid, String studentTG, String mTitle, String mDescription){
        this.mid = mid;
        txtStudentTg.setText(studentTG);
        txtTitle.setText(mTitle);
        txtDescription.setText(mDescription);
    }
}
