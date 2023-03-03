package com.tecmis.mis.admin.notice;

import com.tecmis.mis.admin.user.UserDetails;
import com.tecmis.mis.admin.user.UserItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Notice implements Initializable {
    @FXML
    private VBox noticeLayout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<NoticeDetails> notices = new ArrayList<>(notice());
        for (NoticeDetails notice : notices) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("notice-item.fxml"));

            try {
                HBox hBox = fxmlLoader.load();
                NoticeItemController use = fxmlLoader.getController();
                use.setData(notice);
                noticeLayout.getChildren().add(hBox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<NoticeDetails> notice() {
        List<NoticeDetails> ls = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "root", "")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM notice");

            while (resultSet.next()) {
                NoticeDetails notice = new NoticeDetails();
                notice.setId(Integer.parseInt(resultSet.getString("notice_id")));
                notice.setTitel(resultSet.getString("title"));
                notice.setDate(resultSet.getString("date"));
                notice.setTime(resultSet.getString("time"));
                ls.add(notice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }
}
