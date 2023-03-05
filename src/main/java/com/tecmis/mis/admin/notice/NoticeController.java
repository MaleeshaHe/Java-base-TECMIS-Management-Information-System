package com.tecmis.mis.admin.notice;

import com.jfoenix.controls.JFXButton;
import com.tecmis.mis.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoticeController implements Initializable {
    @FXML
    private JFXButton addNewNotice;

    @FXML
    private JFXButton refreshBtn;
    @FXML
    private JFXButton deleteNotice;

    @FXML
    private JFXButton editNotice;

    @FXML
    private TableColumn<NoticeDetails, String> datecol;

    @FXML
    private TableColumn<NoticeDetails, String> editcol;

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
    private void addNotice(){
        try {
            noticeDetails = noticeTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-notice.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create a new Notice");
            stage.resizableProperty().setValue(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void editNotice(){
        if(noticeTable.getSelectionModel().getSelectedItem() != null){
            try {
                noticeDetails = noticeTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-notice.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                EditNoticeControlloer senddata = fxmlLoader.getController();
                senddata.showInformation(noticeDetails.getNotice_id(),noticeDetails.getTitle(),noticeDetails.getDate(),noticeDetails.getTime(),noticeDetails.getContent());

                Stage stage = new Stage();
                stage.setTitle("Edit Notice");
                stage.resizableProperty().setValue(false);
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to update any notice, First you select the row that you want to update");
            alert.showAndWait();
        }

    }

    @FXML
    private void deleteNotice(){

        if(noticeTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Notice");
            alert.setContentText("Are you sure delete this Notice");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    noticeDetails = noticeTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM `notice` WHERE notice_id='"+noticeDetails.getNotice_id()+"'";
                    connection = DbConnect.getConnect();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();

                } catch (SQLException ex) {
                    Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to delete any notice, First you select the row that you want to delete");
            alert.showAndWait();
        }
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
}
