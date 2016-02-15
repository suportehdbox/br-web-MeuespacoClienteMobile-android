package br.com.libertyseguros.mobile.common.util;

public class CEPUtils {


    /**
     * Formats a CEP as ###-####. 
     * If the CEP is not complete, it will attempt partial formatting once it has enough characters
     * 
     * @param CEP
     *            the CEP to format
     * @return the formatted CEP
     * 
     * refactor: @author Fernando
     */
    public static String formatCEPForDisplay(String cep)
    {
        // Log.v(TAG, ">>> formatCEPForDisplay(String cep)");

        String formattedCEP = cep.replaceAll("\\D", "");

        if (cep != null)
        {	
            // Figure out how many characters have been typed so we can format appropriately
            int length = formattedCEP.length();
            
            if (length >= 5) {
                formattedCEP =
                String.format("%s-%s", formattedCEP.substring(0, 5), formattedCEP.substring(5));
            }
            
        }

        // Log.v(TAG, "<<< formatCEPForDisplay(String cep)");

        return formattedCEP;
    }

    /**
     * private constructor to prevent instantiation
     */
    private CEPUtils()
    {
        super();
    }
}
