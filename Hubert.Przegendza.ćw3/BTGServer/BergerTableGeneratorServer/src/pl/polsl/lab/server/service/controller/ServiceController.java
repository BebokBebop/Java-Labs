package pl.polsl.lab.server.service.controller;

import java.io.IOException;
import java.net.Socket;
import pl.polsl.lab.server.exceptions.MyException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.polsl.lab.server.communication.ServerCommunication;
import pl.polsl.lab.server.model.BergerTableGenerator;

/**
 * controlls individual connection / client / thread
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ServiceController extends Thread {

    /**
     * socket representing connection to the client.
     */
    private Socket socket;

    /**
     * used to communicate with the client.
     */
    private ServerCommunication sComm;

    /**
     * model used to generate a berger table.
     */
    private BergerTableGenerator bergerTableGen;

    /**
     * creates an instance of ServiceController.
     *
     * @param socket socket representing connection to the client
     * @param btg reference to an object of a model
     */
    public ServiceController(Socket socket, BergerTableGenerator btg){
        bergerTableGen = btg;
        this.socket = socket;
    }

    /**
     * Realizes the service - communicates with the client to get a number and
     * to send a berger table generated using that number as the amount of
     * teams.
     */
    @Override
    public void run() {
        try {
            sComm = new ServerCommunication(socket);
        } catch (IOException ex) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            sComm.sendWelcomeMsg();
            while (true) {
                String receivedCommand = null;
                try {
                    receivedCommand = sComm.receiveCommand();
                } catch (IOException ex) { // client got closed
                    Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                } catch (MyException ex) {
                    sComm.sendErrorMsgWrongProtocol();
                }

                if (receivedCommand != null) {

                    if (receivedCommand.toUpperCase().equalsIgnoreCase("HELP")) {
                        sComm.sendConfirmHelpCommand();
                        sComm.sendHelpMsg();
                    } else if (receivedCommand.toUpperCase().equalsIgnoreCase("helpPutty")) {
                        sComm.sendConfirmHelpCommandForPutty();
                        sComm.sendHelpMsgForPutty();
                    } else if (receivedCommand.toUpperCase().equalsIgnoreCase("QUIT")) {
                        sComm.sendConfirmDisconnectCommand();
                        sComm.sendDisconnectMsg();
                        break;
                    } else if (receivedCommand.toUpperCase().equalsIgnoreCase("quitputty")) {
                        sComm.sendConfirmDisconnectCommandForPutty();
                        sComm.sendDisconnectMsgForPutty();
                        break;
                    } else {
                        int teamNumber = -1;
                        try {
                            teamNumber = Integer.parseInt(receivedCommand);
                        } catch (NumberFormatException | NullPointerException nfe) {
                            sComm.sendWrongParamMsg();
                            teamNumber = -1;
                        }
                        if (teamNumber != -1) {
                            sComm.sendConfirmGenerateCommand(teamNumber);
                            try {
                                sComm.sendBGTable(bergerTableGen.generateBergerTableNew(teamNumber));
                            } catch (MyException ex) {
                                sComm.sendErrorMsgWrongNumber();
                            }
                        }
                    }
                }
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
