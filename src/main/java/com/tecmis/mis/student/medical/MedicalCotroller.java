package com.tecmis.mis.student.medical;

import com.tecmis.mis.UserSession;
import com.tecmis.mis.db_connect.DbConnect;
import com.tecmis.mis.student.notice.NoticeController;
import com.tecmis.mis.student.notice.NoticeDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicalCotroller implements Initializable {
    @FXML
    private TableColumn<MedicalDetails, String> descCol;

    @FXML
    private TableColumn<MedicalDetails, String> edateCol;

    @FXML
    private TableView<MedicalDetails> medicalTable;

    @FXML
    private TableColumn<MedicalDetails, String> sdateCol;

    @FXML
    private TableColumn<MedicalDetails, String> titleCol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    NoticeDetails noticeDetails = null ;

    ObservableList<MedicalDetails> medicalList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    @FXML
    private void refreshTable() {
        try {
            medicalList.clear();
            String tg = UserSession.getUserTgNum();
            query = "SELECT * FROM medical WHERE students_tg='"+tg+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                medicalList.add(new MedicalDetails(
                        resultSet.getString("m_title"),
                        resultSet.getString("m_description"),
                        resultSet.getString("start_date"),
                        resultSet.getString("end_date"),
                        resultSet.getInt("m_id"),
                        resultSet.getString("students_tg")));
                medicalTable.setItems(medicalList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();
        titleCol.setCellValueFactory(new PropertyValueFactory<>("m_title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("m_description"));
        sdateCol.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        edateCol.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        medicalTable.setItems(medicalList);
    }
}
