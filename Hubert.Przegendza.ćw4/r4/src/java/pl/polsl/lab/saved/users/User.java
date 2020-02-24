package pl.polsl.lab.saved.users;

/**
 * Represents a single user
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class User {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    /**
     * checks whether a given password is correct
     *
     * @param password password to be compared to the saved one
     * @return true if correct, otherwise false
     */
    public boolean checkPassword(String password) {
        if (this.password.equals(password)) {
            return true;
        }
        return false;
    }

    /**
     * Constructor for the User class
     *
     * @param name name of the user to be created
     * @param password password of the user to be created
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

}