//package com.tecmis.mis.admin.user;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
//import java.net.URL;
//import java.util.Objects;
//import java.util.ResourceBundle;
//
//public class UserItemController implements Initializable {
//
//    @FXML
//    private ImageView img;
//
//    @FXML
//    private Label name;
//
//    @FXML
//    private Label phone;
//
//    @FXML
//    private Label email;
//
//    public void setData(UserDetails user){
//        Image imgProfile = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImgScr())));
//        img.setImage(imgProfile);
//
//        name.setText(user.getName());
//
//        phone.setText(user.getPhoneNumber());
//
//        email.setText(user.getEmail());
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//    }
//
//
//}
