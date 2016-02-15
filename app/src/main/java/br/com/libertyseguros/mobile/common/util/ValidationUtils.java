/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.util;

/**
 * Various utilities for validating fields
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class ValidationUtils
{
    /**
     * The logging tag
     */
    // private static final String TAG = ValidationUtils.class.getName();
	
	/**
	 * Adiciona 'e' no buffer informado.
	 * 
	 * @param buffer
	 */
	public static void addAnd(StringBuffer buffer, boolean addAnd)
	{
		if (addAnd)
		{
			buffer.append(" e ");
		}
	}

    /**
     * Adds a comma and space to the buffer passed in if boolean is true.
     * 
     * @param buffer
     * @param addComma
     */
    public static void addComma(StringBuffer buffer, boolean addComma)
    {
        if (addComma)
        {
            buffer.append(", ");
        }
    }
    
    /**
     * Adds 'valido' or 'v�lidos' to the buffer passed in based on the upperCase boolean.
     * 
     * @param buffer
     * @param plural
     */
    public static void addValidStringLabel(StringBuffer buffer, boolean plural)
    {
        if (plural)
        {
        	buffer.append(" v�lidos");
        }
        else
        {
        	buffer.append(" valido");
        }
    }
    
    /**
     * Adds 'valido' or 'valida' or 'v�lidos' or 'validas' to the buffer passed in based on the upperCase boolean.
     * 
     * @param buffer
     * @param plural
     * @param masculino
     */
    public static void addValidStringLabelWithGender(StringBuffer buffer, boolean plural, boolean masculino)
    {
    	if (plural && masculino)
    	{
    		buffer.append(" válidos");
    	}
    	else if (plural && !masculino)
    	{
    		buffer.append(" válidas");
    	}
    	else if (!plural && masculino)
    	{
    		buffer.append(" valido");
    	}
		else
    	{
			buffer.append(" valida");
    	}
    }

    /**
     * Checks to see if a string is empty. If the string is null or has length=0, true is returned. Otherwise false is
     * returned.
     * 
     * @param stringToCheck
     *            the string to check for emptyness
     * @return
     */
    public static boolean isStringEmpty(String stringToCheck)
    {
        // Log.v(TAG, ">>> isStringEmpty(String stringToCheck)");

        boolean returnVal = false;

        if (stringToCheck == null || (stringToCheck.trim().length() < 1))
        {
            returnVal = true;
        }

        // Log.v(TAG, "<<< isStringEmpty(String stringToCheck)");

        return returnVal;
    }

    /**
     * private constructor to prevent instantiation
     */
    private ValidationUtils()
    {
        super();
    }
}
