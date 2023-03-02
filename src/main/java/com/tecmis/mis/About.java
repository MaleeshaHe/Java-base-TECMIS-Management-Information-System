//package com.tecmis.mis;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class About implements Initializable {
//
//    @FXML
//    private VBox userLayout;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        List<UserDetails> users = new ArrayList<>(users());
//        for(int i=0; i<users.size();i++){
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("user-item.fxml"));
//
//            try {
//                HBox hBox = fxmlLoader.load();
//                UserItemController use = fxmlLoader.getController();
//                use.setData(users.get(i));
//                userLayout.getChildren().add(hBox);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private List<UserDetails> users(){
//        List<UserDetails> ls = new ArrayList<>();
//        UserDetails user = new UserDetails();
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//        user.setName("Maleesha Herath");
//        user.setImgScr("/images/user.png");
//        user.setPhoneNumber("077-8236207");
//        user.setEmail("Malishaherath77@gmail.com");
//        ls.add(user);
//
//
//
//
//
//
//        return ls;
//    }
//}
