package pl.polsl.lab.send.types;

/**
 * History list element.
 * @author Hubert Przegendza
 * @version 1.0
 */
public class History {
    private String argument;
    
    public History(String argument){
        this.argument = argument;
    }

    /**
     * @return the argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * @param argument the argument to set
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }
}
