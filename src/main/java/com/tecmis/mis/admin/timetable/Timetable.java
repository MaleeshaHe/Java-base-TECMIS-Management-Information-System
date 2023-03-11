package com.tecmis.mis.admin.timetable;


import ch.qos.logback.core.Layout;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.skin.Utils;
import com.tecmis.mis.admin.course.CourseDetails;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Timetable{
    @FXML
    private Stage primaryStage;
    @FXML
    private JFXTextField textArea;

    @FXML
    private JFXTextField textName;
    @FXML
    private Circle circleImage;
    @FXML
    private Circle circleImage1;

    private FileInputStream fis;
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;


    @FXML
    void addimage(ActionEvent event) {

        FileChooser fileopen = new FileChooser();
        File file = fileopen.showOpenDialog(primaryStage);

        textArea.setText(file.getAbsolutePath());
        circleImage.setFill(new ImagePattern(new Image(file.toURI().toString(),0,0,true,true)));

        try {
            connection = DbConnect.getConnect();
            query = "INSERT INTO user (name,image) VALUES (?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"Test");
            fis = new FileInputStream(file);
            preparedStatement.setBinaryStream(2, (InputStream)fis, (int)file.length());
            preparedStatement.executeUpdate();
            System.out.println("Image insert");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void loadData(ActionEvent event) {

        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM `image`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                textName.setText(resultSet.getString("name"));

                InputStream is = resultSet.getBinaryStream("img");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1){
                    os.write(content,0,size);
                }
                os.close();
                is.close();

                circleImage1.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }

}

