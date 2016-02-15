/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.common.util.ValidationUtils;

/**
 * Represents a Vehicle object from the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Vehicle
{
    private String color;

    private Long id;

    private String make;

    private String model;

    private String registrationNumber;

    private String registrationState;

    private String vehicleIdentificationNumber;

    private String year;

    /**
     * Checks this vehicle with another to see if both are empty
     * 
     * @param vehicleToCompare
     *            the vehicle to compare to this one
     * @return
     */
    private boolean checkBothEmpty(Vehicle vehicleToCompare)
    {
        return (this.isEmpty() && vehicleToCompare.isEmpty());
    }

    /**
     * This method will check to see if any of the optional fields are null.
     * 
     * @param vehicleToCompare
     * @return
     */
    private boolean compareOptionalFields(Vehicle vehicleToCompare)
    {
        boolean returnVal = true;

        if (vehicleToCompare.getColor() != null && (!vehicleToCompare.getColor().equals(this.getColor())))
        {
            returnVal = false;
        }
        else if (vehicleToCompare.getRegistrationNumber() != null
            && (!vehicleToCompare.getRegistrationNumber().equals(this.getRegistrationNumber())))
        {
            returnVal = false;
        }
        else if (vehicleToCompare.getRegistrationState() != null
            && (!vehicleToCompare.getRegistrationState().equals(this.getRegistrationState())))
        {
            returnVal = false;
        }
        else if (vehicleToCompare.getYear() != null && (!vehicleToCompare.getYear().equals(this.getYear())))
        {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * This method will check to see if any of the required fields are null.
     * 
     * @param vehicleToCompare
     * @return
     */
    private boolean compareRequiredFields(Vehicle vehicleToCompare)
    {
        boolean returnVal = true;

        if (vehicleToCompare.getMake() != null && (!vehicleToCompare.getMake().equals(this.getMake())))
        {
            returnVal = false;
        }
        else if (vehicleToCompare.getModel() != null && (!vehicleToCompare.getModel().equals(this.getModel())))
        {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * @return the color
     */
    public String getColor()
    {
        return color;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the make
     */
    public String getMake()
    {
        return make;
    }

    /**
     * @return the model
     */
    public String getModel()
    {
        return model;
    }

    /**
     * @return the registrationNumber
     */
    public String getRegistrationNumber()
    {
        return registrationNumber;
    }

    /**
     * @return the registrationState
     */
    public String getRegistrationState()
    {
        return registrationState;
    }

    /**
     * @return the vehicleIdentificationNumber
     */
    public String getVehicleIdentificationNumber()
    {
        return vehicleIdentificationNumber;
    }

    /**
     * @return the year
     */
    public String getYear()
    {
        return year;
    }

    /**
     * This method will check to see if the required fields are populated on a vehicle.
     * 
     * @return
     */
    public boolean hasRequiredFields()
    {
        boolean returnVal = true;
        if (ValidationUtils.isStringEmpty(make))
        {
            returnVal = false;
        }
        else if (ValidationUtils.isStringEmpty(model))
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * Checks to see if any of the vehicle fields have been completed. If any field has been completed, false is
     * returned, otherwise true is returned. This only checks user changeable data (ie, id is ignored)
     * 
     * @return true if no vehicle fields have data, otherwise false.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (ValidationUtils.isStringEmpty(this.color) && ValidationUtils.isStringEmpty(this.make)
            && ValidationUtils.isStringEmpty(this.model) && ValidationUtils.isStringEmpty(this.registrationNumber)
            && ValidationUtils.isStringEmpty(this.registrationState)
            && ValidationUtils.isStringEmpty(this.vehicleIdentificationNumber)
            && ValidationUtils.isStringEmpty(this.year))
        {
            returnVal = true;
        }

        return returnVal;
    }

    /**
     * Compares two vehicles to determine if the vehicle data is the same. This only checks user changeable data (ie, id
     * is ignored)
     * 
     * @param vehicleToCompare
     *            the vehicle to compare this one to
     * @return true if they are the same, otherwise false
     */
    public boolean isEqualTo(Vehicle vehicleToCompare)
    {
        boolean returnVal = true;

        if (vehicleToCompare == null)
        {
            returnVal = false;
        }
        else if (checkBothEmpty(vehicleToCompare))
        {
            returnVal = true;
        }
        else if (!compareRequiredFields(vehicleToCompare))
        {
            returnVal = false;
        }
        else if (!compareOptionalFields(vehicleToCompare))
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * Checks to see if a string is empty. If the string is null or has length=0, true is returned. Otherwise false is
     * returned.
     * 
     * @param stringToCheck
     *            the string to check for emptyness
     * @return
     */
    private boolean isStringEmpty(String stringToCheck)
    {
        boolean returnVal = false;

        if (stringToCheck == null || stringToCheck.length() < 1)
        {
            returnVal = true;
        }

        return returnVal;
    }

    /**
     * This will reset all attributes on the vehicle
     */
    public void reset()
    {
        setColor("");
        setMake("");
        setModel("");
        setRegistrationNumber("");
        setRegistrationState("");
        setVehicleIdentificationNumber("");
        setYear("");
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(String color)
    {
        this.color = color;
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
     * @param make
     *            the make to set
     */
    public void setMake(String make)
    {
        this.make = make;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(String model)
    {
        this.model = model;
    }

    /**
     * @param registrationNumber
     *            the registrationNumber to set
     */
    public void setRegistrationNumber(String registrationNumber)
    {
        this.registrationNumber = registrationNumber;
    }

    /**
     * @param registrationState
     *            the registrationState to set
     */
    public void setRegistrationState(String registrationState)
    {
        this.registrationState = registrationState;
    }

    /**
     * @param vehicleIdentificationNumber
     *            the vehicleIdentificationNumber to set
     */
    public void setVehicleIdentificationNumber(String vehicleIdentificationNumber)
    {
        this.vehicleIdentificationNumber = vehicleIdentificationNumber;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(String year)
    {
        this.year = year;
    }

    /**
     * Returns the vehicle as a string in the format Year Make Model
     * 
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer returnString = new StringBuffer();

        returnString.append(year);

        if (!isStringEmpty(make) && returnString.length() > 0)
        {
            returnString.append(" ");
        }
        returnString.append(make);

        if (!isStringEmpty(model) && returnString.length() > 0)
        {
            returnString.append(" ");
        }
        returnString.append(model);

        return returnString.toString();
    }
}
