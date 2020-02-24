package pl.polsl.lab.send.types;

import java.io.Serializable;
import java.util.List;

/**
 * Row of a generated berger table.
 * @author Hubert Przegendza
 * @version 1.0
 */
public class BTResultRow implements Serializable{
    private List<Integer> result;

    public BTResultRow(List<Integer> result){
        this.result = result;
    }
    
    /**
     * @return the result
     */
    public List<Integer> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<Integer> result) {
        this.result = result;
    }
}
