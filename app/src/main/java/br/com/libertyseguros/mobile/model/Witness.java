/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

/**
 * Represents a Witness object from the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Witness
{
    private Contact contact;

    private Long id;

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
     * Checks to see if any of the activity_witness fields have been completed. If any field has been completed, false is
     * returned, otherwise true is returned.
     * 
     * @return true if no activity_witness fields have data, otherwise false.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (this.contact == null || this.contact.isEmpty())
        {
            returnVal = true;
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
}
