package pl.polsl.lab.server.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import pl.polsl.lab.server.exceptions.MyException;

/**
 * Communicates with the client (sends and receives data)
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ServerCommunication {

    /**
     * socket representing connection to the client.
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
     * Creates input and output streams
     *
     * @param socket socket representing connection to the client
     * @throws IOException if input or output can not be created
     */
    public ServerCommunication(Socket socket) throws IOException {
        this.socket = socket;
        output = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream())), true);
        input = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
    }

    /**
     * sends a message to the client, that the given number was out of the
     * range.
     */
    public void sendErrorMsgWrongNumber() {
        output.println("start::WRONG NUMBER! MUST BE [2-100].::end");
    }

    /**
     * sends a welcoming message to the client.
     */
    public void sendWelcomeMsg() {
        output.println("start::Welcome to Berger Table Generator! Type in 'HELP' for info. Type in 'QUIT', to disconnect from the server.::end");
    }

    /**
     * sends a message to the client, that the given parameter was wrong (not a
     * number or 'help' or 'quit'.
     */
    public void sendWrongParamMsg() {
        output.println("start::Wrong parameter! Type in 'HELP', to get info.::end");
    }

    /**
     * sends a message to the client, that a wrong protocol was used.
     */
    public void sendErrorMsgWrongProtocol() {
        output.println("start::Wrong protocol! Type in 'HELP', to get info.::end");
    }

    /**
     * Reads a message sent by the client
     *
     * @return a command sent by the client (quit / help / a number)
     * @throws IOException if the stream could not have been read
     * @throws MyException if the protocol used was incorrect (no 'start' in the
     * beginning or 'end' at the end or not using the '::' separators)
     */
    public String receiveCommand() throws IOException, MyException {
        String line = input.readLine();
        if (line != null) {
            if (line.equalsIgnoreCase("help")) {
                return "helpPutty";
            }
            if (line.equalsIgnoreCase("QUIT")) {
                return "quitPutty";
            }
        } else {
            throw new IOException();
        }
        String protocol[] = line.split("::");
        if (protocol.length != 3) {
            throw new MyException();
        }
        if (!protocol[0].equals("start") || !protocol[protocol.length - 1].equals("end")) {
            throw new MyException();
        }
        //protocol[1] is what's between 'start::' and '::end'
        return protocol[1];
    }

    /**
     * translates a berger table to a string and sends it to the client
     *
     * @param bgt 2d array of numbers representing a berger table
     */
    public void sendBGTable(List<List<Integer>> bgt) {
        String bergerTableString = "start::\n\r";
        int parity = 0;
        for (List<Integer> i : bgt) {
            for (Integer j : i) {
                bergerTableString += j;
                if (parity++ % 2 == 0) {
                    bergerTableString += '-';
                } else {
                    bergerTableString += ' ';
                }
            }
            bergerTableString += "\n\r";
        }
        bergerTableString += "::end";
        output.println(bergerTableString);
    }

    /**
     * sends instructions to the client without protocol formatting.
     */
    public void sendHelpMsgForPutty() {
        output.println("Type in: \"start::XXX::end\" where XXX can be a number([2-100]), to receive a berger table or 'QUIT', to disconnect from the server (or 'help' to get this message).");
    }

    /**
     * sends a message to the client, that they are being disconnected, without
     * protocol formatting.
     */
    public void sendDisconnectMsgForPutty() {
        output.println("Disconnecting...");
    }

    /**
     * sends a message to the client, that they are being disconnected.
     */
    public void sendDisconnectMsg() {
        output.println("start::Disconnecting...::end");
    }

    /**
     * sends a one-line message, that the command to disconnect has been
     * accepted and is being processed. starts with a code signifying success
     */
    public void sendConfirmDisconnectCommand() {
        output.println("start::321::command to disconnect accepted::end");
    }

    /**
     * sends a one-line message, that the command to generate a berger table has
     * been accepted and is being processed. starts with a code signifying
     * success
     * @param teamNumber how many teams is the generator trying to generate table for
     */
    public void sendConfirmGenerateCommand(int teamNumber) {
        output.println("start::123::generating a berger table for " + Integer.toString(teamNumber) + " teams::end");
    }

    /**
     * sends instructions to the client.
     */
    public void sendHelpMsg() {
        output.println("start::Type in a number([2-100]), to receive a berger table.Type in 'QUIT', to disconnect from the server.::end");
    }

    /**
     * sends a one-line message, that the command to send info has been accepted
     * and is being processed. starts with a code signifying success
     */
    public void sendConfirmHelpCommand() {
        output.println("start::111::command to print help accepted::end");
    }

    /**
     * sends a one-line message, that the command to send info has been accepted
     * and is being processed. Starts with a code signifying success and is sent
     * without protocol formatting.
     */
    public void sendConfirmHelpCommandForPutty() {
        output.println("111 command to print help accepted");
    }
    /**
     * sends a one-line message, that the command to disconnect has been accepted
     * and is being processed. Starts with a code signifying success and is sent
     * without protocol formatting.
     */
    public void sendConfirmDisconnectCommandForPutty() {
        output.println("321 command to disconnect accepted");
    }
}
