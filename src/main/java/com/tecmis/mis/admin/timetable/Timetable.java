package com.tecmis.mis.admin.timetable;

import com.tecmis.mis.db_connect.DbConnect;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Timetable implements Initializable {

    @FXML
    private Stage stage;

    Connection connection = null ;

    private static final String TABLE_NAME = "timetable";
 //   private final File pdfFile = new File("F:\\Campus\\Level 02\\Semester 01\\Object Oriented Programming Practicum\\min.pdf");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private byte[] readPDF(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            return outputStream.toByteArray();
        }
    }

    private void insertPDF(Connection connection, byte[] pdfBytes) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " (pdffile) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBytes(1, pdfBytes);
            statement.executeUpdate();
        }
    }

    public void loadPdf() {
        try {

            FileChooser fileopen = new FileChooser();
            File pdfFile = fileopen.showOpenDialog(stage);

            connection = DbConnect.getConnect();
            // Read the PDF file as a byte array
            byte[] pdfBytes = readPDF(pdfFile);

            // Insert the byte array into the database table
            insertPDF(connection, pdfBytes);

            System.out.println("PDF saved to database");

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

}
