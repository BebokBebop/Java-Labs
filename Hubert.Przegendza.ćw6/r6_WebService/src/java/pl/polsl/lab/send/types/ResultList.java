package pl.polsl.lab.send.types;

/**
 * Result list element.
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ResultList {
    private String argument;
    private int noOfCalcs;
    private String result;

    public ResultList(String argument, int noOfCalcs, String result){
        this.argument = argument;
        this.noOfCalcs = noOfCalcs;
        this.result = result;
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

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }
}
