/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.common.util.ValidationUtils;

/**
 * Represents a Policy object in the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Policy
{
    private Long id;

    private String policyLOB;

    private String policyNumber;

    private ArrayList<Vehicle> policyVehicles;

    private Address propertyAddress;

    /**
     * Public Constructor
     * 
     */
    public Policy()
    {
        this("");
    }
    
    /**
     * Parameterized Public Constructor
     * 
     * @param policyLOB
     *            the policyLOB to set
     */
    public Policy(String policyLOB)
    {
        setPolicyNumber("");
        this.policyLOB = policyLOB;
    }
    
    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the policyLOB
     */
    public String getPolicyLOB()
    {
        return policyLOB;
    }

    /**
     * @return the policyNumber
     */
    public String getPolicyNumber()
    {
        return policyNumber;
    }

    /**
     * @return the policyVehicles
     */
    public ArrayList<Vehicle> getPolicyVehicles()
    {
        return policyVehicles;
    }

    /**
     * @return the propertyAddress
     */
    public Address getPropertyAddress()
    {
        return propertyAddress;
    }

    /**
     * Checks to see if any of the policy fields have been completed. If any field has been completed, false is
     * returned, otherwise true is returned. This only checks user changeable data (ie, id is ignored)
     * 
     * @return true if no policy fields have data, otherwise false.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (ValidationUtils.isStringEmpty(this.policyNumber)
            && (this.propertyAddress == null || this.propertyAddress.isEmpty()))
        {
            returnVal = true;

            // check each vehicle too
            ArrayList<Vehicle> vehicles = this.getPolicyVehicles();
            if (vehicles != null)
            {
                int size = vehicles.size();

                for (int i = 0; i < size; i++)
                {
                    Vehicle vehicle = vehicles.get(i);

                    if (vehicle != null && !vehicle.isEmpty())
                    {
                        returnVal = false;

                        break;
                    }
                }
            }
        }

        return returnVal;
    }

    /**
     * Compares two policies to determine if the policy data is the same. This only checks user changeable data (ie, id
     * is ignored)
     * 
     * @param policyToCompare
     *            the policy to compare this one to
     * @return true if they are the same, otherwise false
     */
    public boolean isEqualTo(Policy policyToCompare)
    {
        boolean returnVal = true;
        if (policyToCompare == null || (policyToCompare.getPolicyLOB() == null)
            || (policyToCompare.getPolicyNumber() == null))
        {
            returnVal = false;
        }
        else if (this.isEmpty() && policyToCompare.isEmpty())
        {
            returnVal = true;
        }
        else if (!policyToCompare.getPolicyLOB().equals(this.getPolicyLOB()))
        {
            returnVal = false;
        }
        else if (!policyToCompare.getPolicyNumber().equals(this.getPolicyNumber()))
        {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * This method will remove a vehicle from the policy. First it will check to see if any existing vehicles have the
     * same id, after that it will use the vehicle's isEqualTo to find a match.
     * 
     * @param vehicle
     */
    public void removeVehicle(Vehicle vehicle)
    {
        if (getPolicyVehicles() != null)
        {
            getPolicyVehicles().remove(vehicle);
        }
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
     * @param policyLOB
     *            the policyLOB to set
     */
    public void setPolicyLOB(String policyLOB)
    {
        this.policyLOB = policyLOB;
    }

    /**
     * @param policyNumber
     *            the policyNumber to set
     */
    public void setPolicyNumber(String policyNumber)
    {
        this.policyNumber = policyNumber;
    }

    /**
     * @param policyVehicles
     *            the policyVehicles to set
     */
    public void setPolicyVehicles(ArrayList<Vehicle> policyVehicles)
    {
        this.policyVehicles = policyVehicles;
    }

    /**
     * @param propertyAddress
     *            the propertyAddress to set
     */
    public void setPropertyAddress(Address propertyAddress)
    {
        this.propertyAddress = propertyAddress;
    }
}
