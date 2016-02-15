/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common;

/**
 * Utility class for working with phone numbers
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class PhoneNumberUtils
{
    // private static final String TAG = PhoneNumberUtils.class.getName();

    /**
     * Strips all formatting characters from a phone number
     * 
     * @param phoneNumber
     *            the phone number to unformat
     * @return the unformatted phone number
     */
    public static String formatPhoneNumberForDatabase(String phoneNumber)
    {
        // Log.v(TAG, ">>> formatPhoneNumberForDatabase(String phoneNumber)");

        // Get rid of any special characters
        String formattedPhoneNumber = phoneNumber.replaceAll("\\D", "");

        // Log.v(TAG, "<<< formatPhoneNumberForDatabase(String phoneNumber)");

        return formattedPhoneNumber;
    }

    /**
     * Formats a phone number as (###) ###-####, or (###) # ###-####. 
     * If the phone number is not complete, it will attempt partial formatting once it has enough characters
     * 
     * @param phoneNumber
     *            the phone number to format
     * @return the formatted phone number
     * 
     * refactor: @author Evandro 
     */
    public static String formatPhoneNumberForDisplay(String phoneNumber)
    {
        // Log.v(TAG, ">>> formatPhoneNumberForDisplay(String phoneNumber)");

        String formattedPhoneNumber = phoneNumber;

        if (phoneNumber != null)
        {
            // Get rid of any special characters
            formattedPhoneNumber = phoneNumber.replaceAll("\\D", "");

            // Figure out how many characters have been typed so we can format appropriately
            int length = formattedPhoneNumber.length();

            // If there are between 1 and 3 characters (inclusive) format will be (###
            if (length > 0)
            {
                formattedPhoneNumber = String.format("(%s", formattedPhoneNumber);
            }
            // If there are between 4 and 6 characters (inclusive), format will be (###) ###
            if (length > 3)
            {
                formattedPhoneNumber =
                    String.format("%s)%s", formattedPhoneNumber.substring(0, 4), formattedPhoneNumber.substring(4));
            }
            // if there are 8 or more characters, format will be (###) ####-####
            if (length > 7 && length < 12)
            {
                formattedPhoneNumber =
                    String.format("%s-%s", formattedPhoneNumber.substring(0, 9), formattedPhoneNumber.substring(9));
            }
            // if there are 12 characters, format will be (###) # ####-####
            if (length == 12)
            {
            	formattedPhoneNumber =
            			String.format("%s %s %s-%s", 
            					formattedPhoneNumber.substring(0, 5),
            					formattedPhoneNumber.substring(5, 6),
            					formattedPhoneNumber.substring(6, 10),
            					formattedPhoneNumber.substring(10)
            					);
            }
        }

        // Log.v(TAG, "<<< formatPhoneNumberForDisplay(String phoneNumber)");

        return formattedPhoneNumber;
    }

    /**
     * private constructor to prevent instantiation
     */
    private PhoneNumberUtils()
    {
        super();
    }
}
