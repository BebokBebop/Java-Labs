/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.controller;

import pl.polsl.lab.view.View;
import pl.polsl.lab.exceptions.MyException;
import java.util.Scanner;
import pl.polsl.lab.model.BergerTableGenerator;

/**
 * Controlls the flow of data between the Model (class BergerTableGenerator), the View and the user
 * @author Hubert Przegendza
 * @version 1.0
 */
public class Controller {

    /**
     * controlls the model and uses the view, to get the number of teams and generate a berger table for them
     * @param args number of teams to generate a table for
     */
    public void execute(String[] args) {
        /**
         * maxNumber maximum amount of teams - using higher numbers might freeze the computer
         */
        int maxNumber = 100;
        
        /**
         * view object used to communicate with the user
         */
        View view = new View();
        
        /**
         * teamCount amount of teams to generate a berger table for
         */
        int teamCount = 0;
        
        //in case the args parameter is wrong
        try {
            /**
             * parses the string argument to an int and checks for exceptions
             */
            getCount(args);
        } catch (MyException e) {
            view.printWrongParameterError();
        }
           
        //prompts the user for teamCount via console
        view.askForCount();
        //loop makes sure, that the user types in an actual number
        while (teamCount == 0) {
            /**
             * scanner reads users input from the console
             */
            Scanner scanner = new Scanner(System.in);
            //in case a wrong number is typed by the user
            try {
                teamCount = Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                teamCount = 0;
                view.printWrongNumberError();
                view.askForCount();
            }
            if (teamCount > maxNumber || teamCount < 2) {
                teamCount = 0;
                view.printWrongNumberError();
                view.askForCount();
            }
        }

        /**
         * the model used to generate a berger table
         */
        BergerTableGenerator generator = new BergerTableGenerator();
        
        /**
         * string containing a full berger table to be output by the View class
         */
        String bergerTable = null;
        
        //in case something goes wrong during generation (currently there is no possibility catching an exception)
        try {
            bergerTable = generator.generateBergerTable(teamCount);
        } catch (MyException e) {
            //never happens
        }
        
        //print the final berger table in the console
        view.printBergerTable(bergerTable);
    }
    
    /**
     * parses a given string to an int
     * @param args string to parse to int
     * @return count - number of teams
     * @throws MyException in case the parameter is not a correct number
     */
    private int getCount(String args[]) throws MyException {
        /**
         * temporary variable to store the number
         */
        int teamCount = 0;
        if (args.length == 1) {
            //in case the string does not contain a number
            try {
                teamCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                throw new MyException();
            }
        }
        return teamCount;
    }
}
