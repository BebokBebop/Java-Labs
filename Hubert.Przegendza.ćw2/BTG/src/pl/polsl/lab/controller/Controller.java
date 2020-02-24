/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import pl.polsl.lab.view.View;
import pl.polsl.lab.exceptions.MyException;
import java.util.Scanner;
import pl.polsl.lab.annotations.RequiresTesting;
import pl.polsl.lab.model.BergerTableGenerator;

/**
 * Controlls the flow of data between the Model (class BergerTableGenerator),
 * the View and the user
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class Controller {

    /**
     * controlls the model and uses the view, to get the number of teams and
     * generate a berger table for them
     *
     * @param args number of teams to generate a table or a switch "-t", which
     * makes the app create a file with names of all the methods, that require
     * testing.
     */
    public void execute(String[] args) {
        //maximum amount of teams - using higher numbers might freeze the computer
        int maxNumber = 100;
        int minNumber = 2;

        //object used to communicate with the user
        View view = new View();
        //the model used to generate a berger table
        BergerTableGenerator generator = new BergerTableGenerator();

        //amount of teams to generate a berger table for
        int teamCount = 0;
        boolean printMethodsRequiringTesting = false;
        //in case the args parameter is wrong
        if (args.length == 1) {
            try {
                //parses the string argument to an int and checks for exceptions
                teamCount = getCount(args[0]);
            } catch (MyException e) {
                if (!args[0].equals("-t")) {
                    view.printWrongParameterError();
                } else {
                    printMethodsRequiringTesting = true;
                }
            }
        }

        //Creating a file with the names of all the methods, that should be tested
        //feature for the programmer, so it's done here instead of using the View
        if (printMethodsRequiringTesting) {
            Method[] methods = generator.getClass().getMethods();
            FileWriter fr = null;
            File file = new File("MethodsRequiringTesting.txt");
            try {
                //file.createNewFile();
                fr = new FileWriter(file);
            } catch (IOException e) {
                System.err.println("couldn't open file with testing requiring methods!");
            }
            for (Method m : methods) {
                if (m.isAnnotationPresent(RequiresTesting.class)) {
                    try {
                        fr.write(m.getName());
                    } catch (IOException ex) {
                        System.err.println("can't write to file with testing requiring methods!");
                    }
                }
            }
            try {
                fr.close();
            } catch (IOException e) {
                System.err.println("can't close the file with testing requiring methods!");
            }
        }

        if (teamCount > 100 || teamCount < 2) {
            //prompts the user for teamCount via console
            view.askForCount();
            //loop makes sure, that the user types in an actual number
            while (teamCount == 0) {
                //reads users input from the console
                Scanner scanner = new Scanner(System.in);
                //in case a wrong number is typed by the user
                try {
                    teamCount = Integer.parseInt(scanner.next());
                } catch (NumberFormatException e) {
                    teamCount = 0;
                    view.printWrongNumberError();
                    view.askForCount();
                }
                if (teamCount > maxNumber || teamCount < minNumber) {
                    teamCount = 0;
                    view.printWrongNumberError();
                    view.askForCount();
                }
            }
        }

        //string containing a full berger table to be output by the View class
        String bergerTableString = new String();
        List<List<Integer>> bergerTable2D = null;
        //in case something goes wrong during generation (currently there is no possibility catching an exception)
        try {
            bergerTable2D = generator.generateBergerTableNew(teamCount);
        } catch (MyException e) {
            //never happens
        }
        int parity = 0;
        for (List<Integer> i : bergerTable2D) {
            for (Integer j : i) {
                bergerTableString += j;
                if (parity++ % 2 == 0) {
                    bergerTableString += '-';
                } else {
                    bergerTableString += '\t';
                }
            }
            bergerTableString += '\n';
        }
        //print the final berger table in the console
        view.printBergerTable(bergerTableString);
    }

    /**
     * parses a given string to an int
     *
     * @param arg string to parse to int
     * @return teamCount - number of teams
     * @throws MyException in case the parameter is not a correct number
     */
    private int getCount(String arg) throws MyException {

        //temporary variable to store the number
        int teamCount = 0;

        //in case the string does not contain a number
        try {
            teamCount = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new MyException();
        }

        return teamCount;
    }
}
