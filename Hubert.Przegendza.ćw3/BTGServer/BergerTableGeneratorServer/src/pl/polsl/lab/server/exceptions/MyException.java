package pl.polsl.lab.server.exceptions;

/**
 * Custom exception type, only difference in name
 * @author Hubert Przegendza
 * @version 1.0
 */
public class MyException extends Exception{

    public MyException()
    {
        super();
    }
    public MyException(String message)
    {
        super(message);
    }
}
