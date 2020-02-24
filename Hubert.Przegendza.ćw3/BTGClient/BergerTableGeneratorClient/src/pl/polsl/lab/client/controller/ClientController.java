package pl.polsl.lab.client.controller;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import pl.polsl.lab.client.communicator.ClientCommunicator;
import pl.polsl.lab.client.view.ClientView;

/**
 * Establishes a connection with the server and manages the flow of data between
 * the client and the server
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ClientController implements Closeable {

    /**
     * default port number used by the server.
     */
    final private int DEF_PORT = 8888;
    /**
     * port number used by the server.
     */
    private int PORT;
    /**
     * field represents the socket waiting for client connections.
     */
    private Socket clientSocket;
    /**
     * Address of the server.
     */
    private String ADDRESS;
    /**
     * Default address of the server.
     */
    final private String DEF_ADDRESS = "localhost";
    /**
     * object used to communicate with the server (send and receive messages).
     */
    private ClientCommunicator cComm = null;
    /**
     * object used to write information to the clients console.
     */
    final private ClientView cView;

    /**
     * Reads PORT and ADDRESS from properties file and creates the clientSocket.
     */
    public ClientController() {
        cView = new ClientView();
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(".properties")) {
            properties.load(in);
            PORT = Integer.parseInt(properties.getProperty("port"));
            ADDRESS = properties.getProperty("address");
        } catch (IOException e) {
            cView.writeErrorMsgNoProperties();
            PORT = DEF_PORT;
            ADDRESS = DEF_ADDRESS;
        }
    }

    /**
     * checks the parameter, manages the flow of data between the server and the
     * client usinig cComm
     *
     * @param args switch '-h' to print info about the app
     */
    public void execute(String[] args) {

        if (args.length > 1) {
            cView.printWrongParamtersError();
        }
        if (args.length == 1) {
            if (args[0].equals("-h")) {
                cView.printHelp();
            } else {
                cView.printWrongParamtersError();
            }
        } else {
            //has to be done after checking the parameter to avoid creating a socket if the parameters are wrong 
            try {
                clientSocket = new Socket(ADDRESS, PORT);
            } catch (IOException ex) {
                cView.writeErrorMsgIOError();
            }
            try {
                cComm = new ClientCommunicator(clientSocket);
            } catch (IOException ex) {
                cView.writeErrorMsgIOError();
            }

            String userMsg;
            String serverMsg;
            if (cComm == null) {
                cView.printErrorMdgCantConnect();
            } else {
                try {
                    while ((serverMsg = cComm.getMessageFromServer()) != null) {
                        String protocol[] = serverMsg.split("::");
                        if (protocol.length < 3) {
                            cView.writeErrorMsgWrongProtocol();
                            break; //server is broken - no use of staying connected
                        } else if (!protocol[0].equals("start") || !protocol[protocol.length - 1].equals("end\n")) {
                            cView.writeErrorMsgWrongProtocol();
                            break; //server is broken - no use of staying connected
                        }
                        if (protocol.length == 4) {
                            if (protocol[1].equals("123")) //code meaning that the command to generate has been accepted
                            {
                                cView.printMessageFromServer(protocol[1] + " : " + protocol[2]);
                            }
                            if (protocol[1].equals("321")) //code meaning that the command to quit has been accepted
                            {
                                cView.printMessageFromServer(protocol[1] + " : " + protocol[2]);
                                break;
                            }
                            if (protocol[1].equals("111")) //code meaning that the command to print help has been accepted
                            {
                                cView.printMessageFromServer(protocol[1] + " : " + protocol[2]);
                            }
                            continue;
                        } else {
                            cView.printMessageFromServer(protocol[1]);

                            userMsg = cView.readFromUser();
                            cComm.sendMsgToServer(userMsg);
                        }
                    }
                    clientSocket.close();
                    cView.printConnectionClosed();
                } catch (IOException ex) {
                    cView.writeErrorMsgIOError();
                    cView.printConnectionClosed();
                }
            }
        }

    }

    /**
     * closes the clientSocket
     *
     * @throws IOException if socket cannot be closed
     */
    @Override
    public void close() throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }

    }
}
