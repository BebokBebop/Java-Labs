/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab;

import pl.polsl.lab.controller.Controller;
import java.util.Scanner;

/**
 * Entry point for the application
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class BTGen {

    /**
     * @param args the command line arguments. Can be a number [2-100] or a
     * switch "-t". Number determines the amount of teams to generate the table
     * for and the switch makes the app create a file with names of all the
     * methods, that require testing.
     */
    public static void main(String[] args) {

        //main controller for the application
        Controller controller = new Controller();

        controller.execute(args);
    }
}
