/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukharov.noteeditor.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Vladislav
 */
public class DateUtil {
    

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy\nHH:mm:ss");
       
    public static String format(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(formatter);
    }

    public static String getDateTime(){
        return LocalDateTime.now().format(formatter);
    }
    
    public static LocalDateTime getLocaleDate(){
        return LocalDateTime.now();
    }
    
    public static LocalDateTime parse(String date) {
        try {
            return LocalDateTime.parse(date,formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
