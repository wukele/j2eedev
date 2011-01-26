
package com.example.customerservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Customer {

    protected String name;
    protected List<String> address;
    protected int numOrders;
    protected double revenue;
    protected BigDecimal test;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the address property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAddress() {
        if (address == null) {
            address = new ArrayList<String>();
        }
        return this.address;
    }

    /**
     * Gets the value of the numOrders property.
     * 
     */
    public int getNumOrders() {
        return numOrders;
    }

    /**
     * Sets the value of the numOrders property.
     * 
     */
    public void setNumOrders(int value) {
        this.numOrders = value;
    }

    /**
     * Gets the value of the revenue property.
     * 
     */
    public double getRevenue() {
        return revenue;
    }

    /**
     * Sets the value of the revenue property.
     * 
     */
    public void setRevenue(double value) {
        this.revenue = value;
    }

    /**
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTest() {
        return test;
    }

    /**
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTest(BigDecimal value) {
        this.test = value;
    }

}
