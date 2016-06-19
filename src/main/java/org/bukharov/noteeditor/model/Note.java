/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukharov.noteeditor.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.bukharov.noteeditor.controller.DateUtil;

/**
 *
 * @author Vladislav
 */
public class Note {
    
    private IntegerProperty id;
    private final StringProperty dateTime;
    private final StringProperty noteValue;
    
    public Note(){
        this(-1, null, null);
    }
    public Note(int id, String dateTime, String noteValue){
        this.id = new SimpleIntegerProperty(id);
        this.dateTime = new SimpleStringProperty(dateTime);
        this.noteValue = new SimpleStringProperty(noteValue);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public String getNoteValue() {
        return noteValue.get();
    }
    
    public StringProperty getDateProperty(){
        return dateTime;
    }
    public StringProperty getNoteProperty(){
        return noteValue;
    }
    
    public void setDateTime(LocalDateTime dateTime){
        this.dateTime.set(DateUtil.format(dateTime));
    }
    
    public void setNoteValue(String noteValue){
        this.noteValue.set(noteValue);
    }
    
    public void setId(int id){
        this.id = new SimpleIntegerProperty(id);
    }
    public Integer getId(){
        return id.get();
    }
    public IntegerProperty getIdProperty(){
        return id;
    }
}
