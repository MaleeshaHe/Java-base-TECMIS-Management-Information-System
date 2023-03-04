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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoticeController implements Initializable {
    @FXML
    private JFXButton addNewNotice;

    @FXML
    private JFXButton refreshBtn;

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
                        resultSet.getString("time")));
                noticeTable.setItems(noticeList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idcol.setCellValueFactory(new PropertyValueFactory<>("notice_id"));
        titlecol.setCellValueFactory(new PropertyValueFactory<>("title"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timecol.setCellValueFactory(new PropertyValueFactory<>("time"));

        noticeDetails = noticeTable.getSelectionModel().getSelectedItem();

        //add cell of button edit
        Callback<TableColumn<NoticeDetails, String>, TableCell<NoticeDetails, String>> cellFoctory = (TableColumn<NoticeDetails, String> param) -> {
            // make cell containing buttons
            final TableCell<NoticeDetails, String> cell = new TableCell<NoticeDetails, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {


                        JFXButton deleteIcon = new JFXButton("Delete");
                        JFXButton editIcon = new JFXButton("Edit");

                        deleteIcon.setStyle(
                                "-fx-background-color: #ff0000;"
                                        + "-fx-fill:#ffffff;"
                                        +"-fx-text-fill: #ffff;"
                        );
                        editIcon.setStyle(
                                "-fx-background-color: #5271FF;"
                                        +"-fx-text-fill: #ffff;"
                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
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

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        editcol.setCellFactory(cellFoctory);
        noticeTable.setItems(noticeList);


    }
}
