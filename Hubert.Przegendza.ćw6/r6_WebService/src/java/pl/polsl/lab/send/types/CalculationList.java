package pl.polsl.lab.send.types;

/**
 * Calculation list element.
 * @author Hubert Przegendza
 * @version 1.0
 */
public class CalculationList {
    private String ID;
    private Integer argument;
    private String userLogin;

    public CalculationList(String ID, Integer argument, String user){
        this.ID = ID;
        this.argument = argument;
        this.userLogin = user;
    }
    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return the argument
     */
    public Integer getArgument() {
        return argument;
    }

    /**
     * @param argument the argument to set
     */
    public void setArgument(Integer argument) {
        this.argument = argument;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return userLogin;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.userLogin = user;
    }
}
