/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.exceptions;

/**
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class WSException extends Exception {

    public WSException(Exception ex) {
    }

    WSException() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
