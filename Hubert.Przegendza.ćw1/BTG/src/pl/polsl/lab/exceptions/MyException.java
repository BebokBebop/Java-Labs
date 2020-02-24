/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.exceptions;

/**
 * Custom exception type, only difference in name
 * @author Hubert Przegendza
 * @version 1.0
 */
public class MyException extends Exception{

    public MyException()
    {
        super();
    }
    public MyException(String message)
    {
        super(message);
    }
}
