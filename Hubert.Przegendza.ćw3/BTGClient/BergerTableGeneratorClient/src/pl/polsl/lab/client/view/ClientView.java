package pl.polsl.lab.client.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * writes and reads text from the clients console
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ClientView {

    /**
     * buffered input character stream.
     */
    private BufferedReader inFromUser;

    /**
     * creates an instance of input stream.
     */
    public ClientView() {
        inFromUser = new BufferedReader(
                new InputStreamReader(System.in));
    }

    /**
     * reads and returns a line of text from clients console
     *
     * @return line read from the console
     * @throws IOException if line cannot be read
     */
    public String readFromUser() throws IOException {
        return inFromUser.readLine();
    }

    /**
     * writes a message that the server uses wrong protocol.
     */
    public void writeErrorMsgWrongProtocol() {
        System.out.println("Server message uses wrong protocol!");
    }

    /**
     * prints a string to the clients console. Used for messages sent by the
     * server.
     *
     * @param msg string to be printed to the console
     */
    public void printMessageFromServer(String msg) {
        System.out.println(msg);
    }

    /**
     * writes a message that an IO Error occured.
     */
    public void writeErrorMsgIOError() {
        System.out.println("IO Error");
    }

    /**
     * writes a message that the ".properties" file could not be found and that default values will be used.
     */
    public void writeErrorMsgNoProperties() {
        System.out.println("No properties file found! Using defaults: address: localhost, PORT: 8888");
    }

    /**
     * writes a message that wrong parameters have been used.
     */
    public void printWrongParamtersError() {
        System.out.println("Wrong paramter used! Type '-h' to get info.");
    }
    /**
     * writes info on the app.
     */
    public void printHelp() {
        System.out.println("This app creates a client connecting to a server that can generate a berger table. The address and port are specified in the \".properties\" file.");
    }

    /**
     * writes a message that the app could not connect to the server.
     */
    public void printErrorMdgCantConnect() {
        System.out.println("Fatal Error! Can't connect to the Server.");
    }
    
    /**
     * writes a message that the connection has been closed.
     */
    public void printConnectionClosed() {
        System.out.println("Connection closed.");
    }
}
