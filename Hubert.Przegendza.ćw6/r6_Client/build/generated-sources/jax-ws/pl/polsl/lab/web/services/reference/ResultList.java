
package pl.polsl.lab.web.services.reference;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resultList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="argument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="noOfCalcs" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultList", propOrder = {
    "argument",
    "noOfCalcs",
    "result"
})
public class ResultList {

    protected String argument;
    protected int noOfCalcs;
    protected String result;

    /**
     * Gets the value of the argument property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Sets the value of the argument property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArgument(String value) {
        this.argument = value;
    }

    /**
     * Gets the value of the noOfCalcs property.
     * 
     */
    public int getNoOfCalcs() {
        return noOfCalcs;
    }

    /**
     * Sets the value of the noOfCalcs property.
     * 
     */
    public void setNoOfCalcs(int value) {
        this.noOfCalcs = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResult(String value) {
        this.result = value;
    }

}
