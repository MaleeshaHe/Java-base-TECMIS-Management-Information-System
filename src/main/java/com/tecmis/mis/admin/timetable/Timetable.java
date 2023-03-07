package com.tecmis.mis.admin.timetable;

import com.tecmis.mis.db_connect.DbConnect;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Base64;
import java.util.ResourceBundle;

public class Timetable implements Initializable {

    byte [] photo = null;
    String filename = null;
    @FXML
    private Label imageIcon;

    @FXML
    private ImageView imageIcon2;



    @FXML
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    Connection connection = null ;

    private static final String TABLE_NAME = "timetable";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    void viewPdf(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        //imageIcon.setIcon(new ImageIcon(file.toString()));
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(1 ,2,6));
        filename = file.getAbsolutePath();

        try{
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte [] buf = new byte[1024];

            for(int readnum; (readnum = fis.read(buf)) != -1;){
                bos.write(buf, 0,readnum);
            }

            photo = bos.toByteArray();

        }catch(Exception e){}
    }
    }

