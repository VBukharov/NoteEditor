package org.bukharov.noteeditor;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.bukharov.noteeditor.model.Note;
import org.bukharov.noteeditor.controller.*;


public class MainApp extends Application {
    
    private static final Logger logger = Logger.getLogger(MainApp.class);
    
    private Stage primaryStage;
    ObservableList<Note> noteList = FXCollections.observableArrayList();

    @Override
    public void stop() throws Exception {
        DBConnector.closeConnection();
    }

    public MainApp() {
        try {
            DBConnector.connect();
            DBConnector.initNoteList(noteList);
        }catch(ClassNotFoundException | SQLException e){
            logger.error("Error with connection.", e);
        }
    }

    public ObservableList<Note> getNoteList() {
        return noteList;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage)throws Exception{
        try{
        this.primaryStage = stage;
        showNoteOverview();
        }catch(Exception e){
            logger.error(e);
            throw new Exception(e);
        }
    }

    public void showNoteOverview() {
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/fxml/NoteOverview.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Scene scene = new Scene(page);

        primaryStage.setTitle("Note Book");
        primaryStage.setScene(scene);
        primaryStage.show();

        NoteOverviewController controller = loader.getController();
        controller.setMainApp(this);
        }catch(IOException e){
            logger.error("Wrong file name or smth", e);
        }
    }

    public boolean showNoteCreateEditDialog(Note note) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/createEditOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Note");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialog.setScene(scene);

            NoteEditDialogController controller = loader.getController();
            controller.setDialogStage(dialog);
            controller.setNote(note);
            dialog.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            logger.error("Wrong file name or smth", e);
            return false;
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
