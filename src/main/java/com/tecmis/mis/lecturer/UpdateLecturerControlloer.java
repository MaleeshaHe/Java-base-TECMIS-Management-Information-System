package com.tecmis.mis.lecturer;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateLecturerControlloer implements Initializable {
    @FXML
    private Circle imageView;
    @FXML
    private JFXTextField txtAddress;

    @FXML
    private DatePicker txtDoB;

    @FXML
    private JFXTextField txtFname;

    @FXML
    private JFXTextField txtLname;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private Label error;
    @FXML
    private BorderPane borderpane;
    @FXML
    private JFXComboBox<String> comboDepartment;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private FileInputStream fis;
    private File img;
    String query = null;
    Connection connection = null ;
    ResultSet resultSet = null ;
    PreparedStatement preparedStatement = null ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("..\\..\\..\\..\\images\\profilePic.jpg"))));
        comboDepartment.setItems(FXCollections.observableArrayList("Engineering Technology","Information & Communication Technology","Biosystems Technology","Multidisciplinary Studies"));
        loadData();
    }

    private void loadData(){
        String usertg = UserSession.getUserTgNum();
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM user WHERE tgnum='"+usertg+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                txtFname.setText(resultSet.getString("fname"));
                txtLname.setText(resultSet.getString("lname"));
                txtPhoneNumber.setText(resultSet.getString("phone_num"));
                txtAddress.setText(resultSet.getString("address"));
                txtDoB.setValue(LocalDate.parse(resultSet.getString("dob")));

                InputStream is = resultSet.getBinaryStream("profile_pic");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1){
                    os.write(content,0,size);
                }
                os.close();
                is.close();

                imageView.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }


    @FXML
    void clearBtn() {
        txtFname.setText("");
        txtLname.setText("");
        txtPhoneNumber.setText("");
        txtAddress.setText("");
        imageView.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("..\\..\\..\\..\\images\\profilePic.jpg"))));
    }

    @FXML
    void choosePhoto(ActionEvent event) {
        try {
            FileChooser fileopen = new FileChooser();
            img = fileopen.showOpenDialog(stage);
            imageView.setFill(new ImagePattern(new Image(img.toURI().toString(),0,0,true,true)));
        }catch (Exception e){
        }

    }

    @FXML
    void updateUser(ActionEvent event) {
        if(txtAddress.getText().isEmpty() || txtPhoneNumber.getText().isEmpty() || txtFname.getText().isEmpty() || txtLname.getText().isEmpty() || txtDoB.getValue().lengthOfYear() == 0 || comboDepartment.getSelectionModel().isEmpty()){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {
            LocalDate birtDate = txtDoB.getValue();
            String fname = txtFname.getText();
            String lname = txtLname.getText();
            String phoneNum = txtPhoneNumber.getText();
            String address = txtAddress.getText();
            String dob = String.valueOf(birtDate);
            int department = comboDepartment.getSelectionModel().getSelectedIndex()+1;

            String usertg = UserSession.getUserTgNum();

            try {
                connection = DbConnect.getConnect();
                query = "UPDATE user SET fname= ?, lname = ?, phone_num = ?, dob = ?, address = ?,depId=?,profile_pic = ? WHERE tgnum = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,fname);
                preparedStatement.setString(2,lname);
                preparedStatement.setString(3,phoneNum);
                preparedStatement.setString(4,dob);
                preparedStatement.setString(5,address);
                preparedStatement.setInt(6,department);
                fis = new FileInputStream(img);
                preparedStatement.setBinaryStream(7, (InputStream)fis, (int)img.length());
                preparedStatement.setString(8,usertg);

                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfull");
                alert.setContentText("Successfully updated User");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    clearBtn();
                }

            }catch (Exception e){
                System.out.println(e);
            }

        }
    }
}
