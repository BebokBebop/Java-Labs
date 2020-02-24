package pl.polsl.lab.entities.calculation;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pl.polsl.lab.entities.result.Result;
import pl.polsl.lab.entities.user.Users;

/**
 * DB entity representing a single calculation - amount of teams and the result
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
@Entity
@Table(name = "CALCULATION")
public class Calculation implements Serializable {

    /**
     * Calculation's id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private String id;

    /**
     * foreign key linked with user_id
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "USER_ID")
    private Users user;

    /**
     * foreign key linked with argument
     */
    @ManyToOne
    @JoinColumn(name = "ARGUMENT")
    private Result result;

    /**
     * Creates a Calculation object.
     */
    public Calculation() {
    }

    /**
     * Creates a Calculation object with set parameters.
     *
     * @param result Result of this calculation.
     * @param user Argument of this calculation.
     */
    public Calculation(Result result, Users user) {
        this.result = result;
        this.user = user;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the argument
     */
    public Integer getArgument() {
        return Integer.parseInt(result.getArgument());
    }

    /**
     * @return the user
     */
    public Users getUser() {
        return user;
    }

    public void setUser(Object object) {
        user = null;
    }

}
