/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.common.util.ValidationUtils;

/**
 * Represents a Contact object from the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Contact
{
    private Address address;

    private String emailAddress;

    private String firstName;

    private String homePhone;

    private Long id;

    private String lastName;

    private String mobilePhone;

    private String notes;

    private Long typeId;

    /**
     * Checks this contact with another to see if both are empty
     * 
     * @param contactToCompare
     *            the contact to compare to this one
     * @return
     */
    private boolean checkBothEmpty(Contact contactToCompare)
    {
        return (this.isEmpty() && contactToCompare.isEmpty());
    }

    /**
     * Compares two contacts to determine if the optional fields are the same.
     * 
     * @param contactToCompare
     *            the contact to compare this one to
     * @return true if they are the same, otherwise false
     */
    private boolean compareOptionalFields(Contact contactToCompare)
    {
        boolean returnVal = true;

        if (contactToCompare.getMobilePhone() != null
            && (!contactToCompare.getMobilePhone().equals(this.getMobilePhone())))
        {
            returnVal = false;
        }
        else if (contactToCompare.getEmailAddress() != null
            && (!contactToCompare.getEmailAddress().equals(this.getEmailAddress())))
        {
            returnVal = false;
        }
        else if (contactToCompare.getNotes() != null && (!contactToCompare.getNotes().equals(this.getNotes())))
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * This method will check to see if any of the three required fields are null. This can happen if the user info is
     * not completely entered.
     * 
     * @param contactToCompare
     * @return
     */
    private boolean compareRequiredFields(Contact contactToCompare)
    {
        boolean returnVal = true;

        if (contactToCompare.getFirstName() != null && (!contactToCompare.getFirstName().equals(this.getFirstName())))
        {
            returnVal = false;
        }
        else if (contactToCompare.getLastName() != null && (!contactToCompare.getLastName().equals(this.getLastName())))
        {
            returnVal = false;
        }
        else if (contactToCompare.getHomePhone() != null
            && (!contactToCompare.getHomePhone().equals(this.getHomePhone())))
        {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * @return the address
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @return the homePhone
     */
    public String getHomePhone()
    {
        return homePhone;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @return the mobilePhone
     */
    public String getMobilePhone()
    {
        return mobilePhone;
    }

    /**
     * @return the activity_notes
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * @return the typeId
     */
    public Long getTypeId()
    {
        return typeId;
    }

    /**
     * Checks to see if any of the contact fields have been completed. If any field has been completed, false is
     * returned, otherwise true is returned. This only checks user changeable data (ie, id and type are ignored)
     * 
     * @return true if no contact fields have data, otherwise false.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (ValidationUtils.isStringEmpty(this.emailAddress) && ValidationUtils.isStringEmpty(this.firstName)
            && ValidationUtils.isStringEmpty(this.homePhone) && ValidationUtils.isStringEmpty(this.lastName)
            && ValidationUtils.isStringEmpty(this.mobilePhone) && ValidationUtils.isStringEmpty(this.notes)
            && (this.address == null || this.address.isEmpty()))
        {
            returnVal = true;
        }

        return returnVal;
    }

    /**
     * Compares two contacts to determine if the contact data is the same. This only checks user changeable data (ie, id
     * and type are ignored)
     * 
     * @param contactToCompare
     *            the contact to compare this one to
     * @return true if they are the same, otherwise false
     */
    public boolean isEqualTo(Contact contactToCompare)
    {
        boolean returnVal = true;

        if (contactToCompare == null)
        {
            returnVal = false;
        }
        else if (checkBothEmpty(contactToCompare))
        {
            returnVal = true;
        }
        else if (!compareRequiredFields(contactToCompare))
        {
            returnVal = false;
        }
        else if (!compareOptionalFields(contactToCompare))
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * Checks to see if the minimum requirements for a contact have been met
     * 
     * @return true if the minimum requirements for a contact have been met, otherwise false
     */
    public boolean minRequirementsMet()
    {
        boolean returnVal = true;

        if (this.firstName == null || this.firstName.length() < 1)
        {
            returnVal = false;

        }
        else if (this.lastName == null || this.lastName.length() < 1)
        {
            returnVal = false;

        }
        else if (this.homePhone == null || this.homePhone.length() < 1)
        {
            returnVal = false;
        }

        return returnVal;
    }

    /**
     * This method will reset the contact...just the required my info fields (first, last and phone).
     */
    public void reset()
    {
        setFirstName("");
        setLastName("");
        setHomePhone("");
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * @param emailAddress
     *            the emailAddress to set
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * @param homePhone
     *            the homePhone to set
     */
    public void setHomePhone(String homePhone)
    {
        this.homePhone = homePhone;
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
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @param mobilePhone
     *            the mobilePhone to set
     */
    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @param notes
     *            the activity_notes to set
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }
}
