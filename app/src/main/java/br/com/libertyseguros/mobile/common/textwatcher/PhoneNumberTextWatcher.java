/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.textwatcher;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import br.com.libertyseguros.mobile.common.util.PhoneNumberUtils;

/**
 * Watches a phone number field for changes and formats the phone number in place
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class PhoneNumberTextWatcher implements TextWatcher
{
    //private static final String TAG = PhoneNumberTextWatcher.class.getName();

    private boolean deletingBack = false;

    private boolean deletingSpecChar = false;

    private boolean isFormatting = false;

    private int specCharStart;

    /**
     * If the user is deleting a special character, the character before or after the special character is also deleted.
     * The field is then formatted in-line.
     * 
     * @see TextWatcher#afterTextChanged(Editable)
     */
    @Override
    public void afterTextChanged(Editable s)
    {
        // Log.v(TAG, ">>> afterTextChanged(Editable s)");

        // Any change to the Editable will result in a call to this method, so make sure we're not in the middle for
        // formatting to
        // avoid getting caught in a loop
        if (!isFormatting)
        {
            isFormatting = true;

            // If deleting a special character, also delete the char before or after that
            if (deletingSpecChar && specCharStart > 0)
            {
                if (deletingBack)
                {
                    if (specCharStart - 1 < s.length())
                    {
                        s.delete(specCharStart - 1, specCharStart);
                    }
                }
                else if (specCharStart < s.length())
                {
                    s.delete(specCharStart, specCharStart + 1);
                }
            }

            // Format the number
            String formattedString = s.toString();

            formattedString = PhoneNumberUtils.formatPhoneNumberForDisplay(formattedString);

            // Replace the field with the formatted string
            s.replace(0, s.length(), formattedString);
        }

        isFormatting = false;

        // Log.v(TAG, "<<< afterTextChanged(Editable s)");
    }

    /**
     * Prepares the field for formatting. Determines if the user is deleting one of the formatting characters and if so,
     * sets the watcher up to remember this so that it can also delete the character before or after the formatting
     * character.
     * 
     * @see TextWatcher#beforeTextChanged(CharSequence, int, int, int)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // Log.v(TAG, ">>> beforeTextChanged(CharSequence s, int start, int count, int after)");

        // Any change to the Editable will result in a call to this method, so make sure we're not in the middle for
        // formatting to
        // avoid getting caught in a loop
        if (!isFormatting)
        {
            // Check if the user is deleting a special character so we can also delete the character right before or
            // after the special character. Do not delete extra characters if the user is deleting a selection of text
            // because presumably they've selected exactly what they want to delete
            final int selectionStart = Selection.getSelectionStart(s);
            final int selectionEnd = Selection.getSelectionEnd(s);
            if (s.length() > 1 && count == 1 && after == 0 && isSpecialCharacter(s.charAt(start))
                && selectionStart == selectionEnd)
            {
                deletingSpecChar = true;
                specCharStart = start;

                // Check if the user is deleting forward or backward so we know if we want to delete the character after
                // or before the special character
                if (selectionStart == start + 1)
                {
                    deletingBack = true;
                }
                else
                {
                    deletingBack = false;
                }
            }
            else
            {
                deletingSpecChar = false;
            }
        }

        // Log.v(TAG, "<<< beforeTextChanged(CharSequence s, int start, int count, int after)");
    }

    /*
     * (non-Javadoc)
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // Log.v(TAG, ">>> onTextChanged(CharSequence s, int start, int before, int count)");

        // Do nothing

        // Log.v(TAG, "<<< onTextChanged(CharSequence s, int start, int before, int count)");
    }

    /**
     * Looks for formatting characters [(, ), or -]
     * 
     * @param c
     *            the character to check
     * @return true if the character is a special character, else false
     */
    private boolean isSpecialCharacter(char c)
    {
        // Log.v(TAG, ">>> isSpecialCharacter(char c)");

        boolean isSpecialCharacter = (c == '-' || c == '(' || c == ')');

        // Log.v(TAG, "<<< isSpecialCharacter(char c)");

        return isSpecialCharacter;
    }
}
