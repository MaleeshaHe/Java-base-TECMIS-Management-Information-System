package com.tecmis.mis.admin.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    private Label dep;

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private Label phonenum;

    @FXML
    private Circle pic;

    @FXML
    private Label tgnum;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateShow();
        timeNow();
    }
    private void dateShow(){
        SimpleDateFormat sdf = new SimpleDateFormat("'Today is 'MMMM dd, yyyy");
        String datenow = sdf.format(new Date());
        date.setText(datenow);
    }
    private void timeNow(){
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh : mm : ss a");
            while(true){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e);
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    time.setText(timenow); // This is the label
                });
            }
        });
        thread.start();
    }

}
