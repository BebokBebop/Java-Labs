package pl.polsl.lab.server.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * console messages for server administrator
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ServerView {

    /**
     * print a message in a console that the server has started.
     */
    public void printServerStartMsg() {
        System.out.println("Server started.");
    }

    /**
     * print a message in a console that the server is being closed.
     */
    public void printEndMsg() {
        System.out.println("Closing...");
    }

    /**
     * print a message in a console that an IO Error occured.
     */
    public void printIOError() {
        System.out.println("IO Error");
    }

    /**
     * print a message in a console that wrong parameters have been given.
     */
    public void printWrongParameterError() {

        System.out.println("Wrong parameter used! Use -h, to get info.");
    }

    /**
     * prints a message in a console containing info on how to use the parameter
     * '-t'.
     */
    public void printHelp() {
        System.out.println("Use -t, to generate file with names of all methods requiring testing.");
    }

    /**
     * creates a file and writes names of all the methgods that require testing
     * inside
     *
     * @param methods string containing the names of the methods to be put in
     * the file
     */
    public void writeFileWithTestingRequiringMethods(String methods) {
        FileWriter fr = null;
        File file = new File("MethodsRequiringTesting.txt");
        try {
            fr = new FileWriter(file);
        } catch (IOException e) {
            System.err.println("couldn't open file with testing requiring methods!");
        }
        try {
            fr.write(methods);
        } catch (IOException ex) {
            System.err.println("can't write to file with testing requiring methods!");
        }

        try {
            fr.close();
        } catch (IOException e) {
            System.err.println("can't close the file with testing requiring methods!");
        }
    }

    /**
     * writes a message that an IO Error occured while reading the ".properties" file.
     */
    public void printIOErrorWhileReadingProperties() {
        System.out.println("IO Error while reading properties file");
    }

    /**
     * writes a message that the file containing names of all the mehods requiring testing has been created.
     */
    public void printFileCreatedMsg() {
        System.out.println("File containing names of all the mehods requiring testing created. To start the server run without '-t' switch.");
    }
}
