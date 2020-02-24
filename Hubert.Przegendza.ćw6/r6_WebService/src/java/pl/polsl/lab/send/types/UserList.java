package pl.polsl.lab.send.types;

/**
 * User list element.
 * @author Hubert Przegendza
 * @version 1.0
 */
public class UserList {

    private String ID;
    private String login;
    private String password;
    private int noOfCalcs;

    public UserList(String ID, String login, String password, int noOfCalcs) {
        this.ID = ID;
        this.login = login;
        this.password = password;
        this.noOfCalcs = noOfCalcs;
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
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the noOfCalcs
     */
    public int getNoOfCalcs() {
        return noOfCalcs;
    }

    /**
     * @param noOfCalcs the noOfCalcs to set
     */
    public void setNoOfCalcs(int noOfCalcs) {
        this.noOfCalcs = noOfCalcs;
    }
}
