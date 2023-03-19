package com.tecmis.mis.lecturer.student.eligibility;

import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInRight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class EligibilityController {
    @FXML
    private BorderPane borderpane;

    @FXML
    public void btnContinuousAssessments()throws IOException{
        borderpane.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("continuousAssessments.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInRight(view).play();
    }
    @FXML
    public void btncheckEligibitiy()throws IOException{
        borderpane.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("checkEligibility.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInRight(view).play();
    }
    @FXML
    public void btnfinalMarks()throws IOException{
        borderpane.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("finalMarks.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInRight(view).play();
    }
    @FXML
    public void btnGPA()throws IOException{
        borderpane.getChildren().removeAll();
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gpa.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInRight(view).play();
    }
}
