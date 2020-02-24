package pl.polsl.lab.client.communicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Communicates with the server using a special protocol: all messages must
 * begin with "start::" and end with "::end"
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ClientCommunicator {

    /**
     * socket representing connection to the server.
     */
    private Socket socket;
    /**
     * buffered input character stream.
     */
    private BufferedReader input;
    /**
     * Formatted output character stream.
     */
    private PrintWriter output;

    /**
     * Creates input and output streamsto be used to communicate with the server
     *
     * @param socket socket representing connection to the server
     * @throws IOException if the passed socket is null
     */
    public ClientCommunicator(Socket socket) throws IOException {
        this.socket = socket;
        if (socket == null) {
            throw new IOException();
        }
        output = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream())), true);
        input = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
    }

    /**
     * reads a full message from the server and returns translated version of it
     * (without the protocol beginning and ending)
     *
     * @return translated message using the protocol
     * @throws IOException if line cannot be read from the server
     */
    public String getMessageFromServer() throws IOException {
        String serverMsg = input.readLine();
        serverMsg += '\n';
        while (!serverMsg.contains("::end")) {
            String temp = input.readLine();
            if (temp == null) {
                break;
            }
            serverMsg += temp + '\n';
        }
        return serverMsg;
    }

    /**
     * wraps a message in the protocol and sends it to the server
     * @param message message to be sent to the server
     */
    public void sendMsgToServer(String message) {
        String temp = "start::";
        temp += message;
        temp += "::end";

        output.println(temp);
    }
}
