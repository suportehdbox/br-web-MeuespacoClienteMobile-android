package br.com.libertyseguros.mobile.common.util;

public class CPFUtils {

    /**
     * Formats a CPF as ###-####. 
     * If the CPF is not complete, it will attempt partial formatting once it has enough characters
     * 
     * @param CPF
     *            the CPF to format
     * @return the formatted CPF
     * 
     * refactor: @author Fernando
     */
    public static String formatCPFForDisplay(String cpf)
    {
        // Log.v(TAG, ">>> formatCPFForDisplay(String cpf)");

        String formattedCPF = cpf.replaceAll("\\D", "");

        if (cpf != null)
        {	
            // Figure out how many characters have been typed so we can format appropriately
            int length = formattedCPF.length();
            
            //###.###
            if (length > 2) {
                formattedCPF = String.format("%s.%s", formattedCPF.substring(0, 3), formattedCPF.substring(3));
            }
            //###.###.###
            if (length > 5) {
                formattedCPF = String.format("%s.%s", formattedCPF.substring(0, 7), formattedCPF.substring(7));
            }
            //###.###.###-
            if (length > 8) {
                formattedCPF = String.format("%s-%s", formattedCPF.substring(0, 11), formattedCPF.substring(11));
            }

            // if there are 12 characters, format will be ###.###.###-##
            if (length == 11)
            {
            	formattedCPF =
            			String.format("%s.%s.%s-%s", 
            					formattedCPF.substring(0, 3),
            					formattedCPF.substring(4, 7),
            					formattedCPF.substring(8, 11),
            					formattedCPF.substring(12)
            					);
            }
        }

        // Log.v(TAG, "<<< formatCPFForDisplay(String CPF)");

        return formattedCPF;
    }

    /**
     * private constructor to prevent instantiation
     */
    private CPFUtils()
    {
        super();
    }
}
