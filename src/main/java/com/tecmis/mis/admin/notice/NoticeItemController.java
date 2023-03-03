package com.tecmis.mis.admin.notice;

import com.tecmis.mis.admin.user.UserDetails;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NoticeItemController implements Initializable {
    @FXML
    private Label date;

    @FXML
    private Label n_id;

    @FXML
    private Label time;

    @FXML
    private Label title;

    public void setData(NoticeDetails notice){

        n_id.setText(notice.getId());
        title.setText(notice.getTitel());
        time.setText(notice.getTime());
        date.setText(notice.getDate());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
