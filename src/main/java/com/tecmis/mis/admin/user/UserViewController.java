//package com.tecmis.mis.admin.user;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//import java.io.IOException;
//import java.net.URL;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class UserViewController implements Initializable {
//
//    @FXML
//    private VBox userLayout;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        List<UserDetails> users = new ArrayList<>(users());
//        for (UserDetails user : users) {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("user-item.fxml"));
//
//            try {
//                HBox hBox = fxmlLoader.load();
//                UserItemController use = fxmlLoader.getController();
//                use.setData(user);
//                userLayout.getChildren().add(hBox);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private List<UserDetails> users() {
//        List<UserDetails> ls = new ArrayList<>();
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "root", "")) {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
//
//            while (resultSet.next()) {
//                UserDetails user = new UserDetails();
//                user.setName(resultSet.getString("name"));
//                user.setImgScr(resultSet.getString("image"));
//                user.setPhoneNumber(resultSet.getString("phone"));
//                user.setEmail(resultSet.getString("email"));
//                ls.add(user);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ls;
//    }
//}
