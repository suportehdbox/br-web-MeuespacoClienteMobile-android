package br.com.libertyseguros.mobile.constants;

/**
 * Created by evandro on 8/31/15.
 */
public class Constants {

    public static final String ERROR = "ERRO" ;

    /**
     * The Auto line of business
     */
    public static final String LOB_AUTO = "AUTO";

    /**
     * The Draft event status
     */
    public static final String EVENT_STATUS_DRAFT = "DRAFT";
    /**
     * Event photo id
     */
    public static final String EVENT_PHOTO_ID = "EVENT_PHOTO_ID";

    public static final String LM_EXTRA_CEP = "cep";
    public static final String LM_EXTRA_OFICINAS = "oficinas";
    public static final String LM_EXTRA_ITEM = "itemSel";
    public static final String LM_EXTRA_CATEGORIA = "categoria";
    public static final String LM_EXTRA_PUSH = "push";
    public static final String LM_EXTRA_SESSIONID = "sessionId";

    public static final String LM_WS_USUARIO = "mobile";
    public static final String LM_RESTORINGSTATE = "restoringState";
    public static final String LMUrlInterna = "vwkipbr-spaap01";
    public static final String LMUrlInterna2 = "vwkidbr-spaap01";
    public static final String LMUrlExterna = "www.libertyseguros.com.br";

    /**
     * The extra to be used to tell the claim detail activity whether or not this is the user's the first view of a
     * claim
     */
    public static final String EXTRA_NAME_INITIAL_VIEW = "com.lmig.pm.internet.mobile.android.libertymutual.INITIAL";

    /**
     * The Submitted event status
     */
    public static final String EVENT_STATUS_SUBMITTED = "SUBMITTED";


    public static final int RESULT_ACTION_LOCATION_SOURCE_SETTINGS = 999;

    /**
     * Constante para sinalizar o tipo de execução (Setar de acordo com o ambiente de execução!)
     */
    public static final LMTipoExecucao execucao = LMTipoExecucao.LMTipoExecucaoProducaoExterno;

    public enum LMTipoExecucao {
        LMTipoExecucaoDesenv,
        LMTipoExecucaoAceiteInterno,
        LMTipoExecucaoAceiteExterno,
        LMTipoExecucaoProducaoInterno,
        LMTipoExecucaoProducaoExterno;
    }
}
