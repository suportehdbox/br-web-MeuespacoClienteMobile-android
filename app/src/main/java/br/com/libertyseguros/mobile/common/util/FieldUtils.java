/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.util;

import android.text.util.Linkify;
import android.widget.EditText;

/**
 * Utility methods for working with form fields
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class FieldUtils
{
    /**
     * Disables editing of the given field
     * 
     * @param editText
     *            the field to disable
     */
    public static void disableEditText(EditText editText)
    {
        editText.setFocusable(false);
        editText.setHint(null);
    }

    /**
     * Disables editing of the given field text in the field clickable if it looks like an email. Clicking the phone
     * number will start the email client.
     * 
     * @param editText
     *            the field to disable and make clickable
     */
    public static void disableEditTextEmail(EditText editText)
    {
        editText.setFocusable(false);
        editText.setHint(null);
        editText.setClickable(true);
        editText.setLinksClickable(true);
        editText.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
    }

    /**
     * Disables editing of the given field text in the field clickable if it looks like a phone number. Clicking the
     * phone number will start the dialer.
     * 
     * @param editText
     *            the field to disable and make clickable
     */
    public static void disableEditTextPhone(EditText editText)
    {
        editText.setFocusable(false);
        editText.setHint(null);
        //TODO:  right now the linkify makes the phone number invisible until you click on it.
        //Removing this ability until we have time to figure out why.  (Probably have to look into themes).
        //editText.setClickable(true);
        //editText.setLinksClickable(true);
        //editText.setAutoLinkMask(Linkify.PHONE_NUMBERS);
    }

    /**
     * private constructor to prevent instantiation
     */
    private FieldUtils()
    {
        super();
    }
}
