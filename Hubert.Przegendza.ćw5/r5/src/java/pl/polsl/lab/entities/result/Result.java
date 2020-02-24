package pl.polsl.lab.entities.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import pl.polsl.lab.entities.calculation.Calculation;

/**
 * DB entity representing results for already calculated arguments
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
@Entity
@Table(name = "RESULT")
public class Result implements Serializable {
    /**
     * Result's id
     */
    @Id
    @Column(name = "ARGUMENT")
    private String argument;

    /**
     * Result of calculation. The highest possible result is 30000 characters.
     */
    @Column(name = "RESULT", columnDefinition = "CLOB")
    private String result;

    /**
     * Calculations that have used this argument.
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "ARGUMENT")
    private List<Calculation> calculations = new ArrayList<>();

    /**
     * Creates a Result object.
     */
    public Result() {
    }

    /**
     * Creates a Result object with set parameters.
     * @param argument Argument used to get this result.
     * @param result Result for this argument.
     */
    public Result(String argument, String result) {
        this.argument = argument;
        this.result = result;
    }

    /**
     * @return the argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @return list of calculations that have used this argument and have received this result
     */
    public List<Calculation> getCalculations() {
        return calculations;
    }
}
