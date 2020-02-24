package pl.polsl.lab.server;

import pl.polsl.lab.server.controller.ServerController;

/**
 * Entry point for the application
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class BergerTableGeneratorServer {

    /**
     * main method of the app
     *
     * @param args the command line arguments. Can be empty or a switch "-t",
     * that makes the app create a file with names of all the methods, that
     * require testing without turning on the server.
     */
    public static void main(String args[]) {
        ServerController sController = new ServerController();
        sController.execute(args);
    }
}
