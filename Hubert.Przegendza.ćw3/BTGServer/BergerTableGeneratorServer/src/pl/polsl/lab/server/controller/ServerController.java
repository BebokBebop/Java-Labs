package pl.polsl.lab.server.controller;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import pl.polsl.lab.server.annotations.RequiresTesting;
import pl.polsl.lab.server.model.BergerTableGenerator;
import pl.polsl.lab.server.service.controller.ServiceController;
import pl.polsl.lab.server.view.ServerView;

/**
 * Connects clients to a service and potentially creates a file containing names
 * of all the methods that require testing
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ServerController implements Closeable {

    /**
     * default port number.
     */
    final private int DEF_PORT = 8888;

    /**
     * port number.
     */
    private int PORT;
    /**
     * represents the socket waiting for client connections.
     */
    private ServerSocket serverSocket;
    /**
     * object used to write information to the server console.
     */
    private ServerView sView;

    /**
     * Reads PORT from properties file and creates the server socket.
     */
    public ServerController() {
        sView = new ServerView();
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(".properties")) {
            properties.load(in);
            PORT = Integer.parseInt(properties.getProperty("port"));
        } catch (IOException e) {
            sView.printIOErrorWhileReadingProperties();
            PORT = DEF_PORT;
        }
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            sView.printIOError();
        }
    }

    /**
     * activates a new service for all connecting clients and potentially (if a
     * '-t' parameter is given) creates a file containing names of all the
     * methods that require testing
     *
     * @param args number of teams to generate a table or a switch "-t", which
     * makes the app create a file with names of all the methods, that require
     * testing.
     */
    public void execute(String[] args) {
        boolean printMethodsRequiringTesting = false;
        BergerTableGenerator bergerTableGen = new BergerTableGenerator();

        if (args.length == 1) {
            if (args[0].equals("-t")) {
                printMethodsRequiringTesting = true;
                sView.printFileCreatedMsg();
            } else if (args[0].equals("-h")) {
                sView.printHelp();
            } else {
                sView.printWrongParameterError();
            }

            if (printMethodsRequiringTesting) {
                String methodsRequiringTesting = "";
                Method[] methods = bergerTableGen.getClass().getMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(RequiresTesting.class)) {
                        methodsRequiringTesting += m.getName() + '\n';
                    }
                }
                sView.writeFileWithTestingRequiringMethods(methodsRequiringTesting);
            }
        } else {
            try {
                sView.printServerStartMsg();
                while (true) {
                    Socket socket = serverSocket.accept();
                    ServiceController service = new ServiceController(socket, bergerTableGen);
                    service.start();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                sView.printEndMsg();
            }
        }

    }

    /**
     * closes the serverSocket
     * @throws IOException if socket cannot be closed
     */
    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }

    }

}
