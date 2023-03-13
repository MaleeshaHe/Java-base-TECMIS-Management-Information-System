package com.tecmis.mis.student.notice;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ViewNoticeControlloer {
    @FXML
    private TextArea contenttxt;
    @FXML
    private Text datetxt;

    @FXML
    private Text timetxt;

    @FXML
    private Text titletxt;
    public void showInformation(int id, String title,String date, String time, String content){

        titletxt.setText(title);
        datetxt.setText(date);
        timetxt.setText(time);
        contenttxt.setText(content);
    }
}
