/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukharov.noteeditor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;
import org.bukharov.noteeditor.DBConnector;
import org.bukharov.noteeditor.MainApp;
import org.bukharov.noteeditor.model.Note;

/**
 *
 * @author Vladislav
 */
public class NoteOverviewController {

    private static final Logger logger = Logger.getLogger(NoteOverviewController.class);

    @FXML
    private TableView<Note> noteTable;
    @FXML
    private TableColumn<Note, String> dateTimeColumn;
    @FXML
    private TableColumn<Note, String> textColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        logger.info("initialize columns");
        dateTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        textColumn.setCellValueFactory(cellData -> cellData.getValue().getNoteProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.getPrimaryStage().setResizable(false);
        noteTable.setItems(mainApp.getNoteList());
    }

    @FXML
    private void handleEditNote() {
        logger.info("Handle edit note.");
        Note selectedNote = noteTable.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            logger.info("Note selected");
            boolean okClicked = mainApp.showNoteCreateEditDialog(selectedNote);
        } else {
            logger.info("No note selected. Show alert.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Edit");
            alert.setHeaderText("No note selected");
            alert.setContentText("Select note to delete in the table!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateNote() {
        logger.info("Handle create note.");
        Note tmpNote = new Note();
        boolean okClicked = mainApp.showNoteCreateEditDialog(tmpNote);
        if (okClicked) {
            logger.info("Note created.");
            mainApp.getNoteList().add(tmpNote);
        }
    }

    @FXML
    private void handleDeleteNote() {
        logger.info("Handle delete note.");
        int indexToDelete = noteTable.getSelectionModel().getSelectedIndex();
        if (indexToDelete >= 0) {
            DBConnector.deleteNoteFromDB(noteTable.getItems().get(indexToDelete));
            noteTable.getItems().remove(indexToDelete);
        } else {
            logger.info("No note selected. Show alert");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Delete");
            alert.setHeaderText("No note selected");
            alert.setContentText("Select note to delete in the table!");
            alert.showAndWait();
        }
    }
}
