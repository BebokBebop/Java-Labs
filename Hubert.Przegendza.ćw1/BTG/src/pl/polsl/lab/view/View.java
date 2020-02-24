/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.view;

import java.util.Scanner;

/**
 * View is the only way to communicate anything to the user
 *
 * @author Bebok
 * @version 1.0
 */
public class View {

    /**
     * prompts user for a number input
     */
    public void askForCount() {
        System.out.println("Ile druzyn? [2-100]: ");
    }

    /**
     * prints a berger table
     * @param line berger table in a string 
     */
    public void printBergerTable(String line) {
        System.out.println(line);
    }

    /**
     * communicates to the user, that the starting parameter was not a correct number
     */
    public void printWrongParameterError() {
        System.out.println("Podano zly parametr!");
    }
    /**
     * communicates to the user, that the typed in number was not correct 
     */
    public void printWrongNumberError() {
        System.out.println("Podaj poprawna liczbe!");
    }

}
