/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.common.util.ValidationUtils;

/**
 * Represents a Driver object from the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Driver
{
    private Contact contact;

    private Long id;

    private String insuranceCompany;

    private Policy policy;

    /**
     * @return the contact
     */
    public Contact getContact()
    {
        return contact;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the insuranceCompany
     */
    public String getInsuranceCompany()
    {
        return insuranceCompany;
    }

    /**
     * @return the policy
     */
    public Policy getPolicy()
    {
        return policy;
    }

    /**
     * Checks to see if any of the driver fields have been completed. If any field has been completed, false is
     * returned, otherwise true is returned. This only checks user changeable data (ie, id is ignored)
     * 
     * @return true if no driver fields have data, otherwise false.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (ValidationUtils.isStringEmpty(this.insuranceCompany) && (this.policy == null || this.policy.isEmpty())
            && (this.contact == null || this.contact.isEmpty()))
        {
            returnVal = true;
        }

        return returnVal;
    }

    /**
     * Compares two drivers to determine if the driver data is the same. This only checks user changeable data (ie, id
     * is ignored)
     * 
     * @param driverToCompare
     *            the driver to compare this one to
     * @return true if they are the same, otherwise false
     */
    public boolean isEqualTo(Driver driverToCompare)
    {
        boolean returnVal = true;

        if (this.isEmpty() && driverToCompare.isEmpty())
        {
            returnVal = true;
        }
        else if (!driverToCompare.getInsuranceCompany().equals(this.getInsuranceCompany()))
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * @param contact
     *            the contact to set
     */
    public void setContact(Contact contact)
    {
        this.contact = contact;
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
     * @param insuranceCompany
     *            the insuranceCompany to set
     */
    public void setInsuranceCompany(String insuranceCompany)
    {
        this.insuranceCompany = insuranceCompany;
    }

    /**
     * @param policy
     *            the policy to set
     */
    public void setPolicy(Policy policy)
    {
        this.policy = policy;
    }
}