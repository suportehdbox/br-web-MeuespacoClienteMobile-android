/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.constants.Constants;

/**
 * Represents a User object in the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class User
{
    private Contact contact;

    private Long id;

    // EPO esta vers�o apenas sinistro de auto    
//    private Policy homePolicy;

    private Policy autoPolicy;

    private boolean current;

    /**
     * Creates a new user object, initializing the necessary contact and policy objects as well.
     */
    public User()
    {
        super();

        Contact newContact = new Contact();
        newContact.reset();
        newContact.setAddress(new Address());
        setContact(newContact);

        Policy auto = new Policy();
        auto.setPolicyLOB(Constants.LOB_AUTO);
        setAutoPolicy(auto);

//        Policy home = new Policy();
//        home.setPolicyLOB(Constants.LOB_HOME);
//        setHomePolicy(home);

        setCurrent(true);
    }

    /**
     * @return the autoPolicy
     */
    public Policy getAutoPolicy()
    {
        return autoPolicy;
    }

    /**
     * @return the contact
     */
    public Contact getContact()
    {
        return contact;
    }

//    /**
//     * @return the homePolicy
//     */
//    public Policy getHomePolicy()
//    {
//        return homePolicy;
//    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the current
     */
    public boolean isCurrent()
    {
        return current;
    }

    /**
     * @param autoPolicy
     *            the autoPolicy to set
     */
    public void setAutoPolicy(Policy autoPolicy)
    {
        this.autoPolicy = autoPolicy;
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
     * @param current
     *            the current to set
     */
    public void setCurrent(boolean current)
    {
        this.current = current;
    }

//    /**
//     * @param homePolicy
//     *            the homePolicy to set
//     */
//    public void setHomePolicy(Policy homePolicy)
//    {
//        this.homePolicy = homePolicy;
//    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }
}
