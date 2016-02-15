/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.common.util.ValidationUtils;

/**
 * Represents an Address object in the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Address
{
    private String city;

    private Long id;

    private Double latitude;

    private Double longitude;

    private String state;

    private String streetAddress;

    private String zipCode;

    /**
     * Public constructor.
     */
    public Address()
    {
        setCity("");
        setStreetAddress("");
        setState("");
        setZipCode("");
        setLatitude(new Double(0.0));
        setLongitude(new Double(0.0));
    }

    /**
     * Checks this address with another to see if both are empty
     * 
     * @param addressToCompare
     *            the address to compare to this one
     * @return
     */
    private boolean checkBothEmpty(Address addressToCompare)
    {
        return (this.isEmpty() && addressToCompare.isEmpty());
    }

    /**
     * Checks to see if the zips are the same.
     * 
     * @param addressToCompare
     * @return
     */
    private boolean checkZip(Address addressToCompare)
    {
        return addressToCompare.getZipCode() != null && !addressToCompare.getZipCode().equals(this.getZipCode());
    }

    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude()
    {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude()
    {
        return longitude;
    }

    /**
     * @return the state
     */
    public String getState()
    {
        return state;
    }

    /**
     * @return the streetAddress
     */
    public String getStreetAddress()
    {
        return streetAddress;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode()
    {
        return zipCode;
    }

    /**
     * Checks to see if any of the address fields have been completed. If any field has been completed, false is
     * returned, otherwise true is returned. This only checks user changeable data (ie, id is ignored)
     * 
     * @return true if no address fields have data, otherwise false.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (ValidationUtils.isStringEmpty(this.city) && ValidationUtils.isStringEmpty(this.state) 
        		&& ValidationUtils.isStringEmpty(this.streetAddress) && ValidationUtils.isStringEmpty(this.zipCode))
        {
            returnVal = true;
        }

        return returnVal;
    }

    /**
     * Compares two addresses to determine if the address data is the same. This only checks user changeable data (ie,
     * id is ignored)
     * 
     * @param addressToCompare
     *            the address to compare this one to
     * @return true if they are the same, otherwise false
     */
    public boolean isEqualTo(Address addressToCompare)
    {
        boolean returnVal = true;

        if (addressToCompare != null)
        {
            if (checkBothEmpty(addressToCompare))
            {
                returnVal = true;
            }
            else if (!addressToCompare.getCity().equals(this.getCity()))
            {
                returnVal = false;
            }
            else if (!addressToCompare.getState().equals(this.getState()))
            {
                returnVal = false;
            }
            else if (!addressToCompare.getStreetAddress().equals(this.getStreetAddress()))
            {
                returnVal = false;
            }
            else if (checkZip(addressToCompare))
            {
                returnVal = false;
            }
        }
        else
        {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * Checks to see if the minimum requirements for an address have been met. Zip Code is not required anywhere in this
     * application.
     * 
     * @return true if the minimum requirements for an address have been met, otherwise false
     */
    public boolean minRequirementsMet()
    {
        boolean returnVal = true;

        if (this.city == null || this.city.length() < 1)
        {
            returnVal = false;

        }
        // << EPO esta vers�o n�o utiliza estado
//        else if (this.state == null || this.state.length() < 1)
//        {
//            returnVal = false;
//
//        }
        // >>
        else if (this.streetAddress == null || this.streetAddress.length() < 1)
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * This method will reset the required fields on the address
     */
    public void reset()
    {
        setCity("");
        setState("");
        setStreetAddress("");
        setZipCode("");
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * @param streetAddress
     *            the streetAddress to set
     */
    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = streetAddress;
    }

    /**
     * @param zipCode
     *            the zipCode to set
     */
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * Returns the address as a string in the format Street City, State Zip Code
     * 
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer returnString = new StringBuffer();

        returnString.append(streetAddress);

        if (!ValidationUtils.isStringEmpty(city) && returnString.length() > 0)
        {
            returnString.append(" ");
        }
        returnString.append(city);

        if (!ValidationUtils.isStringEmpty(state) && returnString.length() > 0)
        {
            returnString.append(", ");
        }
        returnString.append(state);

        if (!ValidationUtils.isStringEmpty(zipCode) && returnString.length() > 0)
        {
            returnString.append("  ");
        }
        returnString.append(zipCode);

        return returnString.toString();
    }
}
