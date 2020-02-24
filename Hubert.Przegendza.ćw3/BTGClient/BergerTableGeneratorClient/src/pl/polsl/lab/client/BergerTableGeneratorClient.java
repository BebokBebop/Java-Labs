package pl.polsl.lab.client;

import pl.polsl.lab.client.controller.ClientController;

/**
 * Entry point for the application
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class BergerTableGeneratorClient {

    /**
     * @param args the command line arguments ('-h')
     */
    public static void main(String[] args) {
        ClientController cController = new ClientController();
        cController.execute(args);
    }

}
