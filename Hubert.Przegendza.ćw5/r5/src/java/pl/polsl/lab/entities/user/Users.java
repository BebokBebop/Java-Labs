package pl.polsl.lab.entities.user;

import pl.polsl.lab.entities.calculation.Calculation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DB entity representing an user
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
@Entity
@Table(name = "USERS")
public class Users {

    /**
     * User's id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private String id;

    /**
     * User's login
     */
    @Column(name = "LOGIN")
    private String login;

    /**
     * User's password
     */
    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "USER_ID")
    private List<Calculation> calculations = new ArrayList<>();

    /**
     * Creates an User object.
     */
    public Users() {
    }

    /**
     * Creates an User object with set parameters.
     *
     * @param login User's login.
     * @param password User's password.
     */
    public Users(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Creates an User object with set parameters.
     *
     * @param id User's id.
     * @param login User's login.
     * @param password User's password.
     */
    public Users(String id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public String getID() {
        return this.id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    public List<Integer> getHistory() {
        List<Integer> ret = new ArrayList<>();
        for (Calculation c : getCalculations()) {
            ret.add(c.getArgument());
        }
        return ret;
    }

    /**
     * @return the calculations
     */
    public List<Calculation> getCalculations() {
        return calculations;
    }

    /**
     * @param calculations the calculations to set
     */
    public void setCalculations(List<Calculation> calculations) {
        this.calculations = calculations;
    }

    /**
     * Gets amount of times a specific argument has been used.
     * @param argument which argument.
     * @return # of times a specific argument has been used.
     */
    public Integer getAmountOfCalculationsFor(Integer argument) {
        Integer count = 0;
        for (Calculation c : calculations) {
            if (Objects.equals(c.getArgument(), argument)) {
                count+=1;
            }
        }
        return count;
    }

}
