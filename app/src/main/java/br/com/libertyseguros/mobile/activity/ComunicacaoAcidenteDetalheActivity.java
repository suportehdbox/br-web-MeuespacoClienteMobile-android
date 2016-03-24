package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.GenericAdapter;
import br.com.libertyseguros.mobile.adapter.ItemAdapter;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.BoletimOcorrenciaHelper;
import br.com.libertyseguros.mobile.database.DriverHelper;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.database.EventPhotoHelper;
import br.com.libertyseguros.mobile.database.UserHelper;
import br.com.libertyseguros.mobile.database.VoiceNoteHelper;
import br.com.libertyseguros.mobile.database.WitnessHelper;
import br.com.libertyseguros.mobile.model.BoletimDeOcorrencia;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.EventPhoto;
import br.com.libertyseguros.mobile.model.User;
import br.com.libertyseguros.mobile.model.VoiceNote;

/**
 * 
 * @author Evandro
 *
 */
public class ComunicacaoAcidenteDetalheActivity extends LibertyMobileApp implements OnClickListener {
	
	static final int CLAIM_NUMBER 		= 0;
	static final int MY_INFO 			= 1;
	static final int CHOOSE_CLAIM 		= 2;
	static final int TIME_CLAIM 		= 3;
	static final int LOCATION_CLAIM 	= 4;
	static final int CONTACT_INFO		= 5;
	static final int PICTURES			= 6;
	static final int NOTES_CLAIM 		= 7;
	static final int SHARE_DATA 		= 8;
	static final int SEND_EMAIL_INTENT 	= 9;

	public static final String EXTRA_NAME_INITIAL_VIEW = "com.lmig.pm.internet.mobile.android.libertymutual.INITIAL";
	
	private static final String DELETE = "DELETE";


	
	private ListView lvwList;
	private List<ItemAdapter> listMenuItens;
	private GenericAdapter adapter;
	
	private String currentAction;
	
	private User user;
	
	private int[] icons;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_comunicacao_acidente_detalhe);
			
			Util.setTitleNavigationBar(this, R.string.add_claim_title);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());
			
			setUpUser();
			
			setUpButtons();
			
			// icone de alerta exibido caso tenha: Parcelas vencidas; Sinistros Pendentes ou Renovações 
			icons = new int[4];
			icons[2] = R.drawable.concluido;
			
			// Preenche a lista de menus:
			listMenuItens = new ArrayList<ItemAdapter>();
			
			ItemAdapter itemAdapterClaimNumber = new ItemAdapter(getString(R.string.auto_policy_number_label));
			if (checkIfHasClaimNumber()){
				itemAdapterClaimNumber.setText2(getCurrentEvent().getClaimNumber());
				itemAdapterClaimNumber.setIcon(icons);
			}			
			listMenuItens.add(itemAdapterClaimNumber);
			
			ItemAdapter itemAdapterMyInfo = new ItemAdapter(getString(R.string.my_info_title));
			if (checkIfYourInfoComplete()){
				itemAdapterMyInfo.setIcon(icons);
			}
			listMenuItens.add(itemAdapterMyInfo);
			
			ItemAdapter itemAdapterChooseClaim = new ItemAdapter(getString(R.string.choose_claim_type));
			if (checkIfClaimTypeExists()){
				itemAdapterChooseClaim.setIcon(icons);
			}
			listMenuItens.add(itemAdapterChooseClaim);
			
			ItemAdapter itemAdapterTimeClaim = new ItemAdapter(getString(R.string.add_time));
			if (checkIfTimeExist()){
				SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.simpledateformat));
				itemAdapterTimeClaim.setText2(formatter.format(getCurrentEvent().getEventDateTime()));
				itemAdapterTimeClaim.setIcon(icons);
			}
			listMenuItens.add(itemAdapterTimeClaim);
			
			ItemAdapter itemAdapterlocationClaim = new ItemAdapter(getString(R.string.add_location));
			if (checkIfLocationExist()){
				itemAdapterlocationClaim.setIcon(icons);
			}
			listMenuItens.add(itemAdapterlocationClaim);
			
			ItemAdapter itemAdapterContactInfo = new ItemAdapter(getString(R.string.collect_contact_info));
			if (checkIfContactsExist()){
				itemAdapterContactInfo.setIcon(icons);
			}
			listMenuItens.add(itemAdapterContactInfo);
			
			ItemAdapter itemAdapterPictures = new ItemAdapter(getString(R.string.take_pictures));
			if (checkIfHasPhotos()){
				itemAdapterPictures.setIcon(icons);
			}
			listMenuItens.add(itemAdapterPictures);
			
			ItemAdapter itemAdapterNotesClaim = new ItemAdapter(getString(R.string.add_your_notes));
			boolean hasVoiceNote = checkIfHasVoiceNote();
			boolean hasNote = !ValidationUtils.isStringEmpty(getCurrentEvent().getNotes());
			if (hasVoiceNote || hasNote){
				itemAdapterNotesClaim.setText2(getNoteButtonDisplay(hasVoiceNote, hasNote));
				itemAdapterNotesClaim.setIcon(icons);
			}
			listMenuItens.add(itemAdapterNotesClaim);
			
			ItemAdapter itemAdapterShareData = new ItemAdapter(getString(R.string.compartilhar_dados));
			if (null != getCurrentEvent().getSubmittedUser()
					&& null != getCurrentEvent().getSubmittedUser().getContact()
					&& null != getCurrentEvent().getSubmittedUser().getAutoPolicy()) 
			{
				itemAdapterShareData.setIcon(icons);
			}
			listMenuItens.add(itemAdapterShareData);
			
			// associa o array ao listview
			adapter = new GenericAdapter(this, listMenuItens);
			
			lvwList = (ListView) findViewById(R.id.lvwClaimDetail);
			lvwList.setAdapter(adapter);

			lvwList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
					callActivity(position);
				}
			});

		}
		catch (Exception e) 
		{
			Util.showException(this, e);
		}

	}
	
	private void callActivity(int position) {
		
		try {

//			TODO ANDROID STUDIO

			Intent it = null;

			switch(position) {
				case CLAIM_NUMBER:
					// Numero da Apolice
					it = new Intent(getApplicationContext(), ComunicacaoAcidenteApolicesActivity.class);
					startActivityForResult(it, CLAIM_NUMBER);
					break;
				case MY_INFO:
					// Dados de contato do segurado
					it = new Intent(getApplicationContext(), MyInfoActivity.class);
					it.putExtra(EXTRA_NAME_LOB, getCurrentEvent().getEventType());
					startActivityForResult(it, MY_INFO);
					break;
				case CHOOSE_CLAIM:
					// Tipo do Sinistro
					it = new Intent(getApplicationContext(), ClaimTypeActivity.class);
					startActivityForResult(it, CHOOSE_CLAIM);
					break;
				case TIME_CLAIM:
					// Data e Hora
					if(!isInSubmittedClaim()){
						it = new Intent(getApplicationContext(), DateTimePickerActivity.class);
						startActivityForResult(it, TIME_CLAIM);
					}
					break;
				case LOCATION_CLAIM:
					// Local
					it = new Intent(getApplicationContext(), AddressLocationActivity.class);
					startActivityForResult(it, LOCATION_CLAIM);
					break;
				case CONTACT_INFO:
					// Contato dos envolvidos
					it = new Intent(getApplicationContext(), CollectContactInfoActivity.class);
					startActivityForResult(it, CONTACT_INFO);
					break;
				case PICTURES:
					// Tirar fotos
					it = new Intent(getApplicationContext(), PicturesActivity.class);
					startActivityForResult(it, PICTURES);
					break;
				case NOTES_CLAIM:
					// Observações
					it = new Intent(getApplicationContext(), NotesActivity.class);
					startActivityForResult(it, NOTES_CLAIM);
					break;
				case SHARE_DATA:
					// Compartilhar dados
					it = new Intent(getApplicationContext(), CompartilharDadosActivity.class);
					startActivityForResult(it, SHARE_DATA);
					break;
			}
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	/**
	 * Checks to see if we're in the context of a submitted claimO
	 * 
	 * @return true if the context is a submitted claim, false if not
	 */
	private boolean isInSubmittedClaim(){
		try {
			boolean inSubmittedClaim = /*inClaim &&*/ (getCurrentEvent() != null) && (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()));
			return inSubmittedClaim;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	/**
	 * Método do retorno das telas chamadas nos menus do listView
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// Atualiza o user pois nas activitys chamadas altera os valores do objeto atual.
		setUpUser();
		
		if (resultCode == RESULT_OK){
			switch(requestCode){
				case CLAIM_NUMBER:{
					if (checkIfHasClaimNumber()){
						listMenuItens.get(CLAIM_NUMBER).setText2(getCurrentEvent().getClaimNumber());
						listMenuItens.get(CLAIM_NUMBER).setIcon(icons);
					}
					break;
				} case MY_INFO:{
					if (checkIfYourInfoComplete()){
						listMenuItens.get(MY_INFO).setIcon(icons);
					}
					break;
				} case CHOOSE_CLAIM:{
					if (checkIfClaimTypeExists()){
						listMenuItens.get(CHOOSE_CLAIM).setIcon(icons);						
					}
					break;
				} case TIME_CLAIM:{
					if (checkIfTimeExist()){
						SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.simpledateformat));
						listMenuItens.get(TIME_CLAIM).setText2(formatter.format(getCurrentEvent().getEventDateTime()));
						listMenuItens.get(TIME_CLAIM).setIcon(icons);						
					}
					break;
				} case LOCATION_CLAIM:{
					if (checkIfLocationExist()){
						listMenuItens.get(LOCATION_CLAIM).setIcon(icons);						
					}
					break;
				} case CONTACT_INFO:{
					if (checkIfContactsExist()){
						listMenuItens.get(CONTACT_INFO).setIcon(icons);						
					}
					break;
				} case PICTURES:{
					if (checkIfHasPhotos()){
						listMenuItens.get(PICTURES).setIcon(icons);						
					}
					break;
				} case NOTES_CLAIM:{
					boolean hasVoiceNote = checkIfHasVoiceNote();
					boolean hasNote = !ValidationUtils.isStringEmpty(getCurrentEvent().getNotes());
					if (hasVoiceNote || hasNote){
						listMenuItens.get(NOTES_CLAIM).setText2(getNoteButtonDisplay(hasVoiceNote, hasNote));
						listMenuItens.get(NOTES_CLAIM).setIcon(icons);						
					}
					break;
				} case SHARE_DATA:{
					if (null != getCurrentEvent().getSubmittedUser()
							&& null != getCurrentEvent().getSubmittedUser().getContact()
							&& null != getCurrentEvent().getSubmittedUser().getAutoPolicy()) 
					{
						listMenuItens.get(SHARE_DATA).setIcon(icons);
					}
					break;
				}case SEND_EMAIL_INTENT:{
					// retorno do envio da comuniação deve tornar a tela no modo leitura caso tenha enviado
					setUpButtons();
				}
			}
			
			// Para que a listView seja atualiza com os dados dos itens!
//			adapter.notifyDataSetChanged();
			lvwList.invalidateViews();

		}
	}
	
	/**
	 * Gets the text to display for the voice note button depending on whether or not voice or text activity_notes are present
	 * 
	 * @param hasVoiceNote
	 *            true if a voice note is present on the claim
	 * @param hasNote
	 *            true if a text note is present on the claim
	 * @return
	 */
	private String getNoteButtonDisplay(boolean hasVoiceNote, boolean hasNote){
		
		try {
			StringBuffer buffer = new StringBuffer();

			if (hasVoiceNote && hasNote){
				buffer.append(getString(R.string.voice_text_notes));
			}
			else if (hasNote){
				buffer.append(getString(R.string.text_note));
			}
			else if (hasVoiceNote){
				buffer.append(getString(R.string.voice_note));
			}

			return buffer.toString();
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_clube_liberty, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
//	
//
//	/**
//	 * Refreshes the view when the activity is resumed. Because activities that open from this activity can update the
//	 * data in this activity, it is necessary to update itself each time it displays to make sure it has the most recent
//	 * data.
//	 * 
//	 * @see android.app.Activity#onResume()
//	 */
//	@Override
//	protected void onResume() {
//		
//		try {
//
//			super.onResume();
//
//			setUpUser();
//			
//		} catch (Exception e) {
//			Util.showException(this, e);
//		}
//	}


	@Override
	protected void onResume() {
		super.onResume();

		String screenName;
		if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus())) {
			screenName = "Comunicação de Acidente: Detalhe";
		} else {
			screenName = "Comunicação de Acidente: Novo Sinistro";
		}

		Log.i("Google Analytics: ", screenName);
		mTracker.setScreenName(screenName);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());


	}

	/**
	 * Caso seja um  
	 * 		comunicado Não enviado: Habilita botão SEND;
	 * 		comunicado Enviado: Habilita botão DELETE;
	 */
	private void setUpButtons() {
		
		if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
		{
			// Hide the send info button
			findViewById(R.id.delete_claim_button).setVisibility(View.VISIBLE);
			findViewById(R.id.send_info_button).setVisibility(View.GONE);
		}
		else
		{
			findViewById(R.id.delete_claim_button).setVisibility(View.GONE);
			findViewById(R.id.send_info_button).setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Sets up the user
	 */
	private void setUpUser(){
		
		try {
			
			user = UserHelper.getCurrent(getApplicationContext());
			if (user == null){
				// No user exists, so create one
				user = new User();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Ação do botão Voltar da barra de navegação (navigation_bar.xml)
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
	
    /**
     * Verifica se possui o numero da Apolice
     * @return true if has, otherwise false
     */
    private boolean checkIfHasClaimNumber() {
    	
    	try {
    		
    		boolean returnVal = true;
    		
    		if(ValidationUtils.isStringEmpty( getCurrentEvent().getClaimNumber())){
    			returnVal = false;
    		}
    		return returnVal;
    		
    	} catch (Exception e) {
    		Util.showException(this, e);
    		return false;
    	}
    }

	/**
	 * Checks to see if the user's personal info is complete
	 * 
	 * @return true if complete, otherwise false
	 */
	private boolean checkIfYourInfoComplete() {
		try {
			boolean returnVal = true;

			Contact userContact = user.getContact();

			if (ValidationUtils.isStringEmpty(userContact.getFirstName()))
			{
				returnVal = false;
			}
			else if (ValidationUtils.isStringEmpty(userContact.getLastName()))
			{
				returnVal = false;
			}
			//        else if (ValidationUtils.isStringEmpty(userContact.getHomePhone()) || userContact.getHomePhone().length() < 10)
			else if ((ValidationUtils.isStringEmpty(userContact.getHomePhone()) || userContact.getHomePhone().length() < 11) 
					&& (ValidationUtils.isStringEmpty(userContact.getMobilePhone()) || userContact.getMobilePhone().length() < 11) )
			{
				returnVal = false;
			}

			return returnVal;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Gets the list of missing info if the user has not completed the user info section
	 * @return missingInfo list for display to user
	 */
	private StringBuffer getMissingInfoUserInfo() {
		
		try {
			StringBuffer missingInfo = new StringBuffer();

			Contact userContact = user.getContact();

			if (userContact.getFirstName() == null || userContact.getFirstName().length() < 1)
			{
				missingInfo.append(getString(R.string.first_name_label));
				missingInfo.append("\n");
			}
			if (userContact.getLastName() == null || userContact.getLastName().length() < 1)
			{
				missingInfo.append(getString(R.string.last_name_label));
				missingInfo.append("\n");
			}
			if (userContact.getHomePhone() == null || userContact.getHomePhone().length() < 1)
			{
				missingInfo.append(getString(R.string.home_phone_label));
				missingInfo.append("\n");
			}

			return missingInfo;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	/**
	 * Checks to see if a claim type has been set for the current event
	 * 
	 * @return true if a claim type exists for the current event, otherwise false
	 */
	private boolean checkIfClaimTypeExists() {
		
		try {
			
			String subType = getCurrentEvent().getEventSubType();
			if (subType != null && subType.length() > 0)
			{
				return true;
			}
			return false;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	/**
	 * Checks to see if a time and activity_location have been set for the current event
	 * 
	 * @return true if time and activity_location exist for the current event, otherwise false
	 */
	private boolean checkIfTimeExist(){
		
		try {
			
			boolean returnVal = true;

			if (getCurrentEvent().getEventDateTime() == null)
			{
				returnVal = false;
			}
			return returnVal;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if a time and activity_location have been set for the current event
	 * 
	 * @return true if time and activity_location exist for the current event, otherwise false
	 */
	private boolean checkIfLocationExist(){
		
		try {
			boolean returnVal = true;

			if (!getCurrentEvent().hasValidLocation())
			{
				returnVal = false;
			}
			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	/**
	 * Checks to see if a contact (driver, activity_witness, police info) has been set for the current event
	 * 
	 * @return true if a contact exists for the current event, otherwise false
	 */
	private boolean checkIfContactsExist(){
		
		try {

			boolean returnVal = false;

			long eventId = getCurrentEvent().getId();
			ArrayList<Contact> drivers = DriverHelper.getContactByEvent(getApplicationContext(), eventId);
			if (drivers.size() > 0){
				// No need to keep checking for other contact types if we found drivers
				returnVal = true;
			} else {
				ArrayList<BoletimDeOcorrencia> policeInfos = BoletimOcorrenciaHelper.getByEvent(getApplicationContext(), eventId);
				if (policeInfos.size() > 0){
					// No need to keep checking for other contact types if we found police info
					returnVal = true;
				} else {
					ArrayList<Contact> witnesses = WitnessHelper.getContactByEvent(getApplicationContext(), eventId);
					if (witnesses.size() > 0){
						// We found a activity_witness
						returnVal = true;
					}
				}
			}

			return returnVal;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if photos have been taken or imported for the current event
	 * 
	 * @return true if photos exist, otherwise false
	 */
	private boolean checkIfHasPhotos(){
		
		try {
			
			boolean hasPhotos = false;
			List<EventPhoto> eventPhotos = EventPhotoHelper.getByEvent(getApplicationContext(), getCurrentEvent().getId());
			if (eventPhotos.size() != 0)
			{
				hasPhotos = true;
			}
			return hasPhotos;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	/**
	 * Checks to see if a voice note has been recorded for the current event
	 * 
	 * @return true if a voice note exists for the current event, otherwise false
	 */
	private boolean checkIfHasVoiceNote()
	{
		try {
			boolean hasVoiceNote = false;
			ArrayList<VoiceNote> voiceNotes =
					VoiceNoteHelper.getByEvent(getApplicationContext(), getCurrentEvent().getId());
			if (voiceNotes != null && voiceNotes.size() > 0)
			{
				hasVoiceNote = true;
			}
			return hasVoiceNote;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * This method will start up the send event email activity
	 * 
	 * @param v
	 */
	public void sendInfoButtonPressed(View v)
	{
		try {
			StringBuffer missingInfo = new StringBuffer();
			boolean minReqsMet = true;

	        
	        if (!checkIfHasClaimNumber())
	        {
	            minReqsMet = false;
	            missingInfo.append(getString(R.string.auto_policy_number_label));
	            missingInfo.append("\n");
	        }
			if (!checkIfYourInfoComplete())
			{
				minReqsMet = false;
				missingInfo.append(getMissingInfoUserInfo());
			}
			if (!checkIfClaimTypeExists())
			{
				minReqsMet = false;
				missingInfo.append(getString(R.string.choose_claim_type));
				missingInfo.append("\n");
			}
			if (getCurrentEvent().getEventDateTime() == null)
			{
				minReqsMet = false;
				missingInfo.append(getString(R.string.add_time));
				missingInfo.append("\n");
			}
			if (Constants.LOB_AUTO.equals(getCurrentEvent().getEventType()))
			{
				if (!getCurrentEvent().hasValidLocation())
				{
					minReqsMet = false;
					missingInfo.append(getString(R.string.add_location));
					missingInfo.append("\n");
				}
			}
			if (minReqsMet)
			{
//				saveForm();

				Intent intent = new Intent(getApplicationContext(), SendEventEmailActivity.class);
				startActivityForResult(intent, SEND_EMAIL_INTENT);
			}
			else
			{
				// If they set the claim subtype, and then get this popup, we will get an index out of bounds error, if the
				// current action is still subtype
				currentAction = null;

				// Tell the user they haven't met the minimum requirements
				displayInfoAlert(	ComunicacaoAcidenteDetalheActivity.this,
									getString(R.string.por_favor_insira),
									missingInfo.toString(), 
									getString(R.string.btn_ok),
									ComunicacaoAcidenteDetalheActivity.this);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	/**
	 * Prompts the user to see if they want to delete the claim
	 * @param v
	 */
	public void deleteClaimButtonPressed(View v) {
		try {
			this.currentAction = DELETE;
			displayConfirmAlert(ComunicacaoAcidenteDetalheActivity.this,
								getString(R.string.delete),
								getString(R.string.delete_submitted_claim_confirmation),
								getString(R.string.btn_ok), getString(android.R.string.no),
								ComunicacaoAcidenteDetalheActivity.this);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to cancel their
	 * changes or delete a activity_witness.
	 * 
	 * @param dialog
	 *            the dialog to which the user responded
	 * @param which
	 *            the selection the user made
	 * @see OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {

		try {

			// The user selected yes
			if (which == -1){
				// If the user clicked delete, delete the activity_witness
				if (DELETE.equals(currentAction)){
					EventHelper.delete(getApplicationContext(), getCurrentEvent().getId());
					finish();
				}
//				else{
//					wantSave = true;
//					salvaEfecha();
//				}
			}else if (which == -2 &&  !DELETE.equals(currentAction)){
				// Finish the activity
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
}
