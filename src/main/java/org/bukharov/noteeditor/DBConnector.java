/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukharov.noteeditor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.bukharov.noteeditor.model.Note;

/**
 *
 * @author Vladislav
 */
public class DBConnector {

    private static final Logger logger = Logger.getLogger(DBConnector.class);

    private static Connection connection;

    public static void connect() throws SQLException, ClassNotFoundException {
        logger.info("Open connection.");
        if (connection == null) {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/notebase", "root", "000000");
        }
    }

    public static ResultSet getResultSet(String query) throws SQLException {
        Statement stmnt = connection.createStatement();
        return stmnt.executeQuery(query);
    }

    public static void closeConnection() throws SQLException {
        logger.info("Close connection.");
        if (connection != null) {
            connection.close();
        }
    }

    public static void initNoteList(final ObservableList<Note> noteList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Thread created: initNoteList.");
                try {
                    ResultSet result = getResultSet("select * from note");
                    while (result.next()) {
                        Note note = new Note(result.getInt("id"), result.getString("datetime"), result.getString("text"));
                        noteList.add(note);
                    }
                } catch (SQLException e) {
                    logger.error("Error with initiate", e);
                }
                logger.info("Thread closed: initNoteList.");
            }
        }).start();
    }

    public static void writeNoteToDB(final Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Thread created: Write to DB.");
                try {
                    String insertQuery;
                    PreparedStatement stmnt;
                    if (note.getId() < 0) {
                        insertQuery = "insert into note(datetime,text) values(?,?)";
                        stmnt = connection.prepareStatement(insertQuery);
                        stmnt.setString(1, note.getDateTime());
                        stmnt.setString(2, note.getNoteValue());
                        stmnt.execute();
                        stmnt = connection.prepareStatement("select * from note where datetime=? and text=?");
                        stmnt.setString(1, note.getDateTime());
                        stmnt.setString(2, note.getNoteValue());
                        ResultSet res = stmnt.executeQuery();
                        if (res.next()) {
                            note.setId(res.getInt("id"));
                        }
                    } else {
                        insertQuery = "update note set datetime=?,text=? where id=?";
                        stmnt = connection.prepareStatement(insertQuery);
                        stmnt.setString(1, note.getDateTime());
                        stmnt.setString(2, note.getNoteValue());
                        stmnt.setInt(3, note.getId());
                        stmnt.execute();
                    }
                    logger.info("Successful write to database");
                } catch (SQLException e) {
                    logger.error("Error wirh write to database", e);
                }
                logger.info("Thread closed: Write to DB.");
            }
        }).start();
    }

    public static void deleteNoteFromDB(final Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Thread created: Delete from DB.");
                try {
                    String insertQuery;
                    PreparedStatement stmnt;
                    insertQuery = "delete from note where id=?";
                    stmnt = connection.prepareStatement(insertQuery);
                    stmnt.setInt(1, note.getId());
                    stmnt.execute();
                    logger.info("Successful delete from database");
                } catch (SQLException e) {
                    logger.error("Error with delete from database", e);
                }
                logger.info("Thread closed: Delete from DB.");
            }
        }).start();

    }
}
