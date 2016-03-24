package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.ObscuredSharedPreferences;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.common.util.FileUtils;
import br.com.libertyseguros.mobile.common.util.MailUtils;
import br.com.libertyseguros.mobile.common.util.PhoneNumberUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.database.UserHelper;
import br.com.libertyseguros.mobile.database.VoiceNoteHelper;
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
import br.com.libertyseguros.mobile.model.Event;
import br.com.libertyseguros.mobile.model.Policy;
import br.com.libertyseguros.mobile.model.User;
import br.com.libertyseguros.mobile.model.VoiceNote;

/**
 * Base activity for all other Liberty Mutual Mobile activities. Provides convenience methods for setting up the views
 * with common elements such as the navigation bar and responding to common user actions such as selecting menu options.
 * 
 * @author N0053575 (Heidi Sturm)
 * Update 08/08/2013 @author Evandro
 * @author Fernando Balthazar
 */
//public class LibertyMobileApp extends AppCompatActivity {
public class LibertyMobileApp extends Activity {

    private static Event currentEvent = null;

    private static Policy currentPolicy = null;

	protected static DadosLoginSegurado dadosLoginSegurado;

	/**
	 * Armazena dados do clube liberty de vantagens (beneficios exclusivos p/ segurados)
	 */
	private static List<Object> clubeLiberty;

    protected static final int CONFIRM_DELETE_DIALOG = 0;

    /**
     * email intent id
     */
    protected static final int EMAIL_INFO_INTENT = 1;

    /**
     * The extra to be used when sending the line of business of an event
     */
    protected static final String EXTRA_NAME_LOB = "com.lmig.pm.internet.mobile.android.libertymutual.LOB";

    /**
     * The extra to be used when specifying that an item is new and does not yet exist in the databae
     */
    protected static final String EXTRA_NAME_NEW = "com.lmig.pm.internet.mobile.android.libertymutual.NEW";

    /**
     * The extra to be used when sending the id of an object in the database
     */
    protected static final String EXTRA_NAME_ID = "com.lmig.pm.internet.mobile.android.libertymutual.ID";

    //TODO ANDROID STUDIO

    /**
     * The standard notification id
     */
//    public static final int NOTIFICATION_ID = 0;

    /**
     * The url to the Liberty Mutual web site
     */
//    protected static final String LM_COM_URL = "http://mobile.libertymutual.com";

    /**
     * The url to the privacy policy page
     */
//    protected static final String PRIVACY_POLICY_URL = "http://www.libertymutualgroup.com/omapps/ContentServer?pagename=LMGroup/Views/lmgDisclosure&cid=1138362980102&keyCode=IDMA5600";

    /**
     * The url to the terms and conditions page
     */
//    protected static final String TERMS_AND_CONDITIONS_URL = "http://www.libertymutualgroup.com/omapps/ContentServer?pagename=LMGroup/Views/lmgDisclosure&cid=1138366154501";

//    public static final String ENVIRONMENT_TEST = "TEST";
//    public static final String ENVIRONMENT_PROD = "PROD";
//    public static final String ENVIRONMENT_QA = "QA";
	
    /**
     * The key criptografada para acesso ao preference seguro
     */
    protected static String KEY = null;
    
	/**
     * Returns the current event being displayed or worked in the app.
     * 
     * @return the currentEvent
     */
    protected static Event getCurrentEvent()
    {
        return currentEvent;
    }

    /**
     * Returns the current policy being displayed or worked in the app.
     * 
     * @return the currentEvent
     */
    protected static Policy getCurrentPolicy()
    {
        return currentPolicy;
    }

    /**
     * Sets the current event being displayed or worked in the app. Call this method when creating a new event or when
     * the user selects an existing event to edit or view.
     * 
     * @param currentEvent
     *            the currentEvent to set
     */
    protected static void setCurrentEvent(Event currentEvent)
    {
    	LibertyMobileApp.currentEvent = currentEvent;
    }

    /**
     * Sets the current policy being displayed or worked in the app. Call this method when creating a new policy or when
     * the user selects an existing policy to edit or view.
     * 
     * @param currentPolicy
     *            the currentPolicy to set
     */
    protected static void setCurrentPolicy(Policy currentPolicy)
    {
    	LibertyMobileApp.currentPolicy = currentPolicy;
    }

//    TODO ANDROID STUDIO
//    private String environment;

    /**
     * Invokes the appropriate add claim process for the selected claim type, or does nothing if a valid claim type is
     * not selected
     */
    protected void addClaim()
    {
        // Log.v(TAG, ">>> addClaim(int selectedClaimType)");

        // Create an Event with the current line of business and status of draft
        Event event = new Event();
        
        // EPO esta versao so faz aviso de sinistro de auto
        
        event.setEventType(Constants.LOB_AUTO);

        event.setEventStatus(Constants.EVENT_STATUS_DRAFT);

        // TODO EPO verificar user logado!
        
        // Get the current user info to prefill
        User user = UserHelper.getCurrent(getApplicationContext());
        
        if(user != null) {
        	// caso tenha user logado
//            Contact contact = user.getContact();
//            if (contact == null) {
//                contact = new Contact();
//            }

            // seta numero da Apolice
//            if (Constants.LOB_AUTO.equals(lineOfBusiness)) {
                event.setClaimNumber(user.getAutoPolicy().getPolicyNumber());
//            } else {
//                event.setPolicyNumber(user.getHomePolicy().getPolicyNumber());
//            }
        }

        // Store the event in the database
        Long id = EventHelper.insert(getApplicationContext(), event);
        event.setId(id);

        // Set the event as the current working event
        setCurrentEvent(event);

        //EPO:
//        // Go to the detail page and tell it that this is the first view
//        NotificationUtil.showClaimNotification(getApplicationContext());

		// Go to the claim detail view and tell it that this is not the initial view
		Intent i = new Intent(getApplicationContext(), ComunicacaoAcidenteDetalheActivity.class);
		i.putExtra(ComunicacaoAcidenteDetalheActivity.EXTRA_NAME_INITIAL_VIEW, false);
		startActivity(i);
    }

    /**
     * Alerts the user that the minimum requirements of the form have not been met.
     * 
     * @param context
     *            the context within which to work
     * @param street
     *            whether or not the street address field is empty
     * @param city
     *            whether or not the city field is empty
     * @param state
     *            whether or not the state field is empty
     * @param zip
     *            whether or not the zip field is empty
     * @param listener
     *            the listener which will react to the dialog
     */
    protected void alertMinAddressReqs(Context context, boolean street, boolean city, boolean state, boolean zip,
        DialogInterface.OnClickListener listener)
    {
        // Log.v( TAG,
        // ">>> alertMinAddressReqs(Context context, boolean street, boolean city, boolean state, boolean zip, OnClickListener listener, String message)");

        StringBuffer missingInfo = getMinAddressReqsList(street, city, state, zip);

        // Tell the user they haven't met the minimum requirements
		displayInfoAlert(	context,
							getString(R.string.por_favor_insira),
							missingInfo.toString(), 
							getString(R.string.btn_ok),
							listener);
        // Log.v( TAG,
        // "<<< alertMinAddressReqs(Context context, boolean street, boolean city, boolean state, boolean zip, OnClickListener listener, String message)");
    }

    /**
     * Creates a button with one line of text, bold and black
     * 
     * @param context
     *            The context within which to work
     * @param button
     *            The button to which the text will be applied
     * @param firstLine
     *            The first line of text to apply to the button
     */
    protected void createButton(Context context, TextView button, String firstLine)
    {
        // Log.v(TAG, ">>> createButton(Context context, Button button, String firstLine)");

        // Create the spans with the two different styles
        TextAppearanceSpan normalSpan = new TextAppearanceSpan(context, R.style.button_list_item);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        // Build the text with the two lines of text
        builder.append(firstLine);

        // Apply the styles to the text
        builder.setSpan(normalSpan, 0, firstLine.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the text to the button
        button.setText(builder);

        // Log.v(TAG, "<<< createButton(Context context, Button button, String firstLine)");
    }

    /**
     * Creates a button with two lines of text, the top line being bold and black and the bottom line being normal and
     * gray.
     * 
     * @param context
     *            The context within which to work
     * @param button
     *            The button to which the text will be applied
     * @param firstLine
     *            The first line of text to apply to the button
     * @param secondLine
     *            The second line of text to apply to the button
     */
    protected void createCompoundButton(Context context, TextView button, String firstLine, String secondLine)
    {
        // Log.v(TAG, ">>> createCompoundButton(Context context, Button button, String firstLine, String secondLine)");

        // Create the spans with the two different styles
        TextAppearanceSpan normalSpan = new TextAppearanceSpan(context, R.style.button_list_item);
        TextAppearanceSpan detailSpan = new TextAppearanceSpan(context, R.style.detail_button_text_detail);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        // Build the text with the two lines of text
        builder.append(firstLine);
        builder.append("\n");
        builder.append(secondLine);

        // Apply the styles to the text
        builder.setSpan(normalSpan, 0, firstLine.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(detailSpan, firstLine.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the text to the button
        button.setText(builder);

        // Log.v(TAG, "<<< createCompoundButton(Context context, Button button, String firstLine, String secondLine)");
    }

    /**
     * Displays an alert to the user asking them to confirm an action before proceeding
     * 
     * @param context
     *            the context within which to display the alert
     * @param title
     *            the title to display on the alert
     * @param message
     *            the message to display on the alert
     * @param positiveButton
     *            the text for the positive button
     * @param negativeButton
     *            the text for the negative button
     * @param listener
     *            the listener which will be listening for the user's selection
     */
    protected void displayConfirmAlert(Context context, String title, String message, String positiveButton,
        String negativeButton, DialogInterface.OnClickListener listener)
    {
        // Log.v( TAG,
        // ">>> displayConfirmAlert(Context context, String title, String message, String positiveButton, String negativeButton, OnClickListener listener)");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, listener);
        builder.setNegativeButton(negativeButton, listener);
        AlertDialog alert = builder.create();
        alert.show();

        // Log.v( TAG,
        // "<<< displayConfirmAlert(Context context, String title, String message, String positiveButton, String negativeButton, OnClickListener listener)");
    }

    /**
     * Displays an info alert to the user
     * 
     * @param context
     *            the context within which to display the alert
     * @param title
     *            the title to display on the alert
     * @param message
     *            the message to display on the alert
     * @param neutralButtonText
     *            the text for the neutral button
     * @param listener
     *            the listener which will be listening for the user's selection
     */
    protected void displayInfoAlert(Context context, String title, String message, String neutralButtonText,
        DialogInterface.OnClickListener listener)
    {
        // Log.v( TAG,
        // ">>> displayInfoAlert(Context context, String title, String message, String neutralButtonText, OnClickListener listener)");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNeutralButton(neutralButtonText, listener);
        AlertDialog alert = builder.create();
        alert.show();

        // Log.v( TAG,
        // "<<< displayInfoAlert(Context context, String title, String message, String neutralButtonText, OnClickListener listener)");
    }

    /**
     * This method will get the application specific portion of the current cache file name. This will exclude the API
     * call, that may change in future versions of the Android API.
     * 
     * @return The filename
     */
    public String getApplicationSpecificCacheDirectory()
    {
        String directory = "/Android/data/" + getApplicationContext().getPackageName() + "/cache/";
        // Make sure directory exists and is writeable
        FileUtils.dirExistsWriteable(Environment.getExternalStorageDirectory() + directory);
        return directory;
    }

    /**
     * This method will get the application specific portion of the current file name. This will exclude the API call,
     * that may change in future versions of the Android API. This is the value that should be persisted in the
     * database, so no changes have to be made in the future.
     * 
     * @return The filename
     */
    public String getApplicationSpecificFileDirectory()
    {
        String directory = "/Android/data/" + getApplicationContext().getPackageName() + "/files/";
        // Make sure directory exists and is writeable
        FileUtils.dirExistsWriteable(Environment.getExternalStorageDirectory() + directory);
        return directory;
    }

//  TODO ANDROID STUDIO
//    /**
//     * @return the environment
//     */
//    public String getEnvironment()
//    {
//        return environment;
//    }

    /**
     * Gets the text to display for a list of missing address fields
     * 
     * @param street
     *            whether or not the street address field is empty
     * @param city
     *            whether or not the city field is empty
     * @param state
     *            whether or not the state field is empty
     * @param zip
     *            whether or not the zip field is empty
     * @return
     */
    public StringBuffer getMinAddressReqsList(boolean street, boolean city, boolean state, boolean zip)
    {
        StringBuffer missingInfo = new StringBuffer();

        boolean needComma = false;

        if (street)
        {
            missingInfo.append(getString(R.string.address_label));

            needComma = true;
        }
        if (city)
        {
            if (needComma)
            {
                missingInfo.append(", ");
            }

            missingInfo.append(getString(R.string.city_label));

            needComma = true;
        }
        // EPO estado
//        if (state)
//        {
//            if (needComma)
//            {
//                missingInfo.append(", ");
//            }
//
//            missingInfo.append(getString(R.string.state_label));
//
//            needComma = true;
//        }
        if (zip)
        {
            if (needComma)
            {
                missingInfo.append(", ");
            }

            missingInfo.append(getString(R.string.zip_code_label));
        }
        return missingInfo;
    }

    /**
     * Checks to see if the minimum requirements are met and, if not, alerts the user as to what needs to be completed
     * 
     * @param context
     *            the context within which to work
     * @param errorOnEmpty
     *            whether or not to error if all fields are empty
     * @param address
     *            the address to check for minimum requirements
     * @param listener
     *            the listener which will react to the dialog
     * @return
     */
    protected boolean minAddressRequirementsMet(Context context, boolean errorOnEmpty, Address address,
        boolean zipRequired, DialogInterface.OnClickListener listener)
    {
        // Log.v( TAG,
        // ">>> minAddressRequirementsMet(Context context, boolean errorOnEmpty, Address address, OnClickListener listener, String message)");

        boolean returnVal = true;

        // Check for required fields

        boolean missingStreet = false;
        boolean missingCity = false;
        boolean missingState = false;
        boolean missingZip = false;

        if (ValidationUtils.isStringEmpty(address.getStreetAddress()))
        {
            missingStreet = true;
            returnVal = false;
        }
        if (ValidationUtils.isStringEmpty(address.getCity()))
        {
            missingCity = true;
            returnVal = false;
        }
        if (ValidationUtils.isStringEmpty(address.getState()))
        {
            missingState = true;
            returnVal = false;
        }
        if (zipRequired && address.getZipCode().length() < 5)
        {
            missingZip = true;
            returnVal = false;
        }

        if (!returnVal)
        {
            alertMinAddressReqs(context, missingStreet, missingCity, missingState, missingZip, listener);
        }
        // Log.v( TAG,
        // "<<< minAddressRequirementsMet(Context context, boolean errorOnEmpty, Address address, OnClickListener listener, String message)");

        return returnVal;
    }

    /**
     * All child classes should override this method to initialize themselves.
     * 
     * @see Activity#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	// Log.v(TAG, ">>> onCreate()");
		try {
			
	        super.onCreate(savedInstanceState);

	        if (savedInstanceState != null)
	        {
	            long eventId = savedInstanceState.getLong("eventId", -1);
	            if (eventId != -1)
	            {
	                setCurrentEvent(EventHelper.get(getApplicationContext(), eventId));
	            }
	        }

//          TODO ANDROID STUDIO
//	        String env = getString(R.string.environment);
//	        if ("Test".equals(env))
//	        {
//	            environment = ENVIRONMENT_TEST;
//	        }
//	        else if ("QA".equals(env))
//	        {
//	            environment = ENVIRONMENT_QA;
//	        }
//	        else if ("Production".equals(env))
//	        {
//	            environment = ENVIRONMENT_PROD;
//	        }
//	        else
//	        {
//	            environment = "Unknown";
//	        }

		} catch (Exception e) {
			Util.showException(this, e);
		}	
        // Log.v(TAG, "<<< onCreate()");
    }
    
    /**
     * Ação do botão Cancelar da barra de botões (button_bar.xml).
     * Fecha a activity atual.
     * @param v
     */
    public void cancelar(View v)
    {
    	finish();
    }

    /**
     * @param outState
     * @see Activity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        if (getCurrentEvent() != null)
        {
            outState.putLong("eventId", getCurrentEvent().getId());
        }
        super.onSaveInstanceState(outState);

    }


    /**
     * Gives the user a list of options to choose from for adding a claim and sets up the listener to respond to the
     * selection
     * 
     * @param context
     *            The context within which to work
     * @param listener
     *            The listener that will respond to the user's selection
     */
    protected void selectAddClaim(Context context, DialogInterface.OnClickListener listener)
    {
        // Log.v(TAG, ">>> selectAddClaim(Context context, DialogInterface.OnClickListener listener)");

        // Create the list of options for adding a claim
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(getString(R.string.add_claim_title));
        builder.setItems( new String[]{getString(R.string.add_claim_title)}, listener);

        // Ask the user which type of claim they want to start
        AlertDialog alert = builder.create();
        alert.show();

        // Log.v(TAG, "<<< selectAddClaim(Context context, DialogInterface.OnClickListener listener)");
    }

    /**
     * Sets the title of the screen to the given text and hides the left and right buttons.
     * 
     * @param title
     */
    protected void setUpNavigationBarTitleOnly(String title)
    {
    	
        // Log.v(TAG, ">>> setUpNavigationBarTitleOnly(String title)");

    	TextView textView = (TextView) findViewById(R.id.nav_bar_text);
			  
    	if (title != null)
    	{
    		textView.setText(title);
    	}
    	else
    	{
    		textView.setVisibility(View.INVISIBLE);
    	}

        // Log.v(TAG, "<<< setUpNavigationBarTitleOnly(String title)");
    }

    /**
     * This method will start the intent to send an email to liberty mutual.
     */
    protected void startEventEmail(Event event)
    {
        // Log.v(TAG, ">>> startEventEmail()");

    	// EPO: 
//        NotificationUtil.clearClaimNotification(getApplicationContext());

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("*/*");

//      << ANDROID STUDIO
//      emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {LibertyEnvironment.getEmailAddress(getEnvironment(), getResources())});
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.email_sinistro)});
//      >>

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, MailUtils.getSubjectLine(event));
        emailIntent.putExtra(Intent.EXTRA_TEXT, MailUtils.getBodyText(event, UserHelper.getCurrent(getApplicationContext()),
                DeviceUtils.getVersionName(getApplicationContext()), getString(R.string.build_number)));


//      << ADD AUDIO
        ArrayList<Uri> uris = new ArrayList<Uri>();

        // Case has photos
        if (event.getPhotos() != null){
            //emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, MailUtils.getPhotoUris(event, getApplicationSpecificCacheDirectory()));
            uris = MailUtils.getPhotoUris(event, getApplicationSpecificCacheDirectory());
        }

        // Case has audio
        ArrayList<VoiceNote> voiceNoteList = VoiceNoteHelper.getByEvent(getApplicationContext(), getCurrentEvent().getId());

        if (!voiceNoteList.isEmpty()) {
            File file = new File(voiceNoteList.get(0).getPathToVoiceNote());
            file.setReadable(true, false);
            file.setReadable(true, false);
            //String theMIMEcategory = getMIMEcategory(aFile);
            Uri uri = Uri.fromFile(file);
            uris.add(uri);
        }
//      >>

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        startActivityForResult(Intent.createChooser(emailIntent, getString(R.string.enviando_email)), EMAIL_INFO_INTENT);
        // Log.v(TAG, ">>> startEventEmail()");
    }

    /**
     * Starts an e-mail with the user's insurance info in the body
     * 
     * @param userContactInfo
     *            the user info to send in the email
     */
    protected void startUserInfoEmail(Contact userContactInfo, String policyNumber)
    {
        // Log.v(TAG, ">>> startUserInfoEmail(Contact userContactInfo, String policyNumber)");

        LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
        properties.put(getString(R.string.first_name_label), userContactInfo.getFirstName());
        properties.put(getString(R.string.last_name_label), userContactInfo.getLastName());
        properties.put(getString(R.string.auto_policy_number_label), policyNumber);
        properties.put(getString(R.string.home_phone_label),
            PhoneNumberUtils.formatPhoneNumberForDisplay(userContactInfo.getHomePhone()));

        properties.put("VERSION_NAME", DeviceUtils.getVersionName(getApplicationContext()));
        properties.put("VERSION_NUMBER", getString(R.string.build_number));

        SpannableStringBuilder stringBuilder = MailUtils.buildEmailBody(null, properties);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html");
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_info_email_subject));
        i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(stringBuilder.toString()));
        startActivity(Intent.createChooser(i, getString(R.string.enviando_email)));

        // Log.v(TAG, "<<< startUserInfoEmail(Contact userContactInfo, String policyNumber)");
    }

    /**
     * Updates the title in the navigation bar
     * 
     * @param title
     *            the new title
     */
    protected void updateNavigationBarTitle(String title)
    {
        // Log.v(TAG, ">>> updateNavigationBarTitle(String title)");
        TextView textView = (TextView) findViewById(R.id.nav_bar_text);
        textView.setText(title);
        // Log.v(TAG, "<<< updateNavigationBarTitle(String title)");
    }

	public DadosLoginSegurado getDadosLoginSegurado() {
		try {
			return dadosLoginSegurado;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	protected void callLogonSeguradoToken() {
		// implementado no MenuPrincipalActivity.java
	}
	
	/**
	 * Atualiza os dados de login do segurado
	 * @param dadosLoginSegurado
	 */
	public void setDadosLoginSegurado(DadosLoginSegurado dadosLoginSegurado) {
	
		try {
			
			LibertyMobileApp.dadosLoginSegurado = dadosLoginSegurado;
	    	
			/*  No momento que aplicação sai persiste os dados do segurado caso tenha escolhido manter logado
			 *  A leitura e escrita do preferences precisa estar na mesma activity
			 */
			//http://stackoverflow.com/questions/785973/what-is-the-most-appropriate-way-to-store-user-settings-in-android-application
			
			SharedPreferences sharedPreferences = getSharedPreferences("LibertySeguros", MODE_PRIVATE);			
			final SharedPreferences obscuredSP = new ObscuredSharedPreferences(this, sharedPreferences, KEY);
	        SharedPreferences.Editor editorObscuredSP = obscuredSP.edit();
	        
	        String 	tokenAutenticacao 	= getDadosLoginSegurado().getTokenAutenticacao();
	        String 	userId 				= getDadosLoginSegurado().getCpf();
	        String 	tokenNotificacao 	= getDadosLoginSegurado().getTokenNotificacao();
	        int 	appVersion 			= getDadosLoginSegurado().getAppVersion();
	        
	        if (null != tokenAutenticacao) {
	        	editorObscuredSP.putString("tokenAutenticacao", tokenAutenticacao);
	        	
	        	if (null != userId) {
	        		editorObscuredSP.putString("usuarioId", userId);
	        	}
	        	
	        	if (null != tokenNotificacao) {
	        		editorObscuredSP.putString("tokenNotificacao", tokenNotificacao);
	        		editorObscuredSP.putInt("appVersion", appVersion);
	        	}
			} 
	        else {
				editorObscuredSP.clear();
			}
	        editorObscuredSP.commit();
	        
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	public static List<Object> getClubeLiberty() {
		return clubeLiberty;
	}

	public static void setClubeLiberty(List<Object> clubeLiberty) {
		LibertyMobileApp.clubeLiberty = clubeLiberty;
	}

    /**
     * Ação do botão Voltar da barra de navegação (navigation_bar.xml).
     * @param v
     */
	public void voltar(View v)
	{
		try {
			onBackPressed();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
	  CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  CustomApplication.activityPaused();
	}

}
