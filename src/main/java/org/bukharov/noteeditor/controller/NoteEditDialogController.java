/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukharov.noteeditor.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.bukharov.noteeditor.DBConnector;
import org.bukharov.noteeditor.model.Note;

/**
 *
 * @author Vladislav
 */
public class NoteEditDialogController {
    
    private static final Logger logger = Logger.getLogger(NoteEditDialogController.class);

    @FXML
    private Label dateTime;
    @FXML
    private TextArea noteText;

    private Stage dialogStage;
    private Note note;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        logger.info("NoteEditDialogController initialized.");
        dateTime.setText(DateUtil.getDateTime());
        Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dateTime.setText(DateUtil.getDateTime());
            }
        }));
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
    }

    public void setLabelText() {
        dateTime.setText(DateUtil.getDateTime());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.setResizable(false);
    }

    public void setNote(Note note) {
        this.note = note;
        noteText.setText(note.getNoteValue());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        logger.info("Handle Ok.");
        if (checkInput()) {
            note.setDateTime(DateUtil.getLocaleDate());
            note.setNoteValue(noteText.getText());
                DBConnector.writeNoteToDB(note);
                okClicked = true;
                dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        logger.info("Handle cansel. Close window.");
        dialogStage.close();
    }

    private boolean checkInput() {
        logger.info("Check input.");
        String errMsg = "";
        if (noteText.getText() == null || noteText.getText().length() == 0) {
            errMsg += "Empty note!";
        } else if (noteText.getText().length() > 100) {
            errMsg += "Note's length must be in 100 symbols!";
        }
        if (errMsg.length() != 0) {
            logger.info("Wrong input.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected:");
            alert.setContentText(errMsg);

            alert.showAndWait();

            return false;
        } else {
            logger.info("Correct input.");
            return true;
        }
    }
}
