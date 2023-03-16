<<<<<<< Updated upstream
package com.tecmis.mis.lecturer.notice;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;



public class SeeNoticeControlloer {

    @FXML
    private TextArea contenttxt;
    @FXML
    private Text datetxt;

    @FXML
    private Text timetxt;

    @FXML
    private Text titletxt;


    public void showInformation(String title,String date, String time, String content){
        titletxt.setText(title);
        datetxt.setText(date);
        timetxt.setText(time);
        contenttxt.setText(content);
    }
}
=======
package com.tecmis.mis.lecturer.notice;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;



public class SeeNoticeControlloer {

    @FXML
    private TextArea contenttxt;
    @FXML
    private Text datetxt;

    @FXML
    private Text timetxt;

    @FXML
    private Text titletxt;


    public void showInformation(String title,String date, String time, String content){
        titletxt.setText(title);
        datetxt.setText(date);
        timetxt.setText(time);
        contenttxt.setText(content);
}
}
>>>>>>> Stashed changes
