package com.tecmis.mis.student.notice;

import com.jfoenix.controls.JFXButton;
import com.tecmis.mis.admin.notice.EditNoticeControlloer;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoticeController implements Initializable {
    @FXML
    private TableColumn<NoticeDetails, String> datecol;

    @FXML
    private TableColumn<NoticeDetails, Integer> idcol;

    @FXML
    private TableView<NoticeDetails> noticeTable;

    @FXML
    private TableColumn<NoticeDetails, String> timecol;

    @FXML
    private TableColumn<NoticeDetails, String> titlecol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    NoticeDetails noticeDetails = null ;

    ObservableList<NoticeDetails> noticeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    @FXML
    private void refreshTable() {
        try {
            noticeList.clear();

            query = "SELECT * FROM `notice`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                noticeList.add(new NoticeDetails(
                        resultSet.getInt("notice_id"),
                        resultSet.getString("title"),
                        resultSet.getString("date"),
                        resultSet.getString("time"),
                        resultSet.getString("content")));
                noticeTable.setItems(noticeList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idcol.setCellValueFactory(new PropertyValueFactory<>("notice_id"));
        titlecol.setCellValueFactory(new PropertyValueFactory<>("title"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timecol.setCellValueFactory(new PropertyValueFactory<>("time"));
        noticeTable.setItems(noticeList);
    }

    @FXML
    void showNotice(MouseEvent event) {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            try {
                noticeDetails = noticeTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view-notice.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                ViewNoticeControlloer senddata = fxmlLoader.getController();
                senddata.showInformation(noticeDetails.getNotice_id(),noticeDetails.getTitle(),noticeDetails.getDate(),noticeDetails.getTime(),noticeDetails.getContent());

                Stage stage = new Stage();
                stage.setTitle("View Notice");
                javafx.scene.image.Image image = new Image("images/appIcon.png");
                stage.getIcons().add(image);
                stage.resizableProperty().setValue(false);
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException ex) {
                Logger.getLogger(com.tecmis.mis.admin.notice.NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
