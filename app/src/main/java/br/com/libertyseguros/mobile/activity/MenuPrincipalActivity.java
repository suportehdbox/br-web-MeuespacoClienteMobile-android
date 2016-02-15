package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;

import br.com.MondialAssistance.Liberty.Activities.ScreenRegisterPhoneNumber;
import br.com.MondialAssistance.Liberty.Activities.ScreenSelectLob;
import br.com.MondialAssistance.Liberty.BLL.BLLPhone;
import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.ObscuredSharedPreferences;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.gcm.GcmRegisterInterface;
import br.com.libertyseguros.mobile.gcm.GcmRegisterTask;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class MenuPrincipalActivity extends LibertyMobileApp {

	private Button btnComunicacaoAcidente;
	private Button btnAssistencia;
	private Button btnOficinas;
	private Button btnClubeLiberty;
	private Button btnLogin;
	private Button btnCadastre;
	private Button btnMinhaApolice;
	private Button btnAtendimento;
	private Button btnSetup;
	private Button btnPoliticaPrivacidade;

	private static final int ID_MENU_LOGOUT = 0;
	private static final int ID_MENU_POLITICA_PRIVACIDADE = 1;
	private static final int ID_MENU_NOTIFICACAO_PUSH = 2;

	Context context;
	private boolean isFromNotification = true;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);

			context = getApplicationContext();

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

//			checkPlayServices();

			configScreen();

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String tokenNotificacao) {

		try {

			WebserviceInterface enviarTokenWsInterface = new WebserviceInterface(this) {
				@Override
				public void callBackWebService(String response) {
					// TODO 
					Log.i("callEnviarToken", String.valueOf(CallWebServices.retEnviarToken(response)));
				}
			};

			// Chama o ws  
			CallWebServices.callEnviarToken(this, getDadosLoginSegurado().getCpf(), tokenNotificacao, enviarTokenWsInterface);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	// 		GCM >>


	private void configScreen() {
		try {
			setContentView(R.layout.activity_menu_principal);

			//<< EPO: SN - manter app logado: Ao iniciar o app verifica se o usuário optou por manter app logado.
			setInstanceDadosLoginSegurado();
			//>>

			// Comunicacao de Acidente
			btnComunicacaoAcidente = (Button) findViewById(R.id.btnComunicacaoAcidente);
			btnComunicacaoAcidente.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity de comunicar acidente
					callActivity(ComunicacaoAcidenteActivity.class);
				}
			});

			// Assistencia 24 horas
			btnAssistencia = (Button) findViewById(R.id.btnAssistencia);
			btnAssistencia.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callAssistencia24h();
				}
			});

			// Minhas Ap�lices
			btnMinhaApolice = (Button) findViewById(R.id.btnMinhaApolice);
			btnMinhaApolice.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callActivity(MinhasApolicesActivity.class);
				}
			});

			// Oficinas Referenciadas
			btnOficinas = (Button) findViewById(R.id.btnOficinas);
			btnOficinas.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callActivity(OficinasReferenciadasActivity.class);
				}
			});

			// Clube liberty
			btnClubeLiberty = (Button) findViewById(R.id.btnClubeLiberty);
			btnClubeLiberty.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity do clube liberty
//					callActivity(ClubeLibertyActivity.class);

					// SN153250 Novo Clube Liberty: Caso esteja logado :Chamar o serviço no retorno chamar o clube Liberty
					goClubeLiberty();
				}
			});

			// Atendimento
			btnAtendimento = (Button) findViewById(R.id.btnAtendimento);
			btnAtendimento.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity do clube liberty
					callActivity(AtendimentoActivity.class);
				}
			});

			// Login
			btnLogin = (Button) findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity do Login
					callLogin();
				}
			});

			// Cadastre
			btnCadastre = (Button) findViewById(R.id.btnCadastre);
			btnCadastre.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (DeviceUtils.isConnected(MenuPrincipalActivity.this)) {
						// chama activity do cadastro do segurado
						callActivity(CadastroSeguradoActivity.class);

					} else {
						Util.showToast(MenuPrincipalActivity.this, getString(R.string.NotConnection));
					}
				}
			});

			// Setup :  optins (politica privacidade e logout)
			btnSetup = (Button) findViewById(R.id.btnPolPrivacidade);
			btnSetup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Chamar o menu de opções:
					openOptionsMenu();
				}
			});

			// Politica de Privacidade
			btnPoliticaPrivacidade = (Button) findViewById(R.id.btnPoliticaPrivacidade);
			btnPoliticaPrivacidade.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Chamar o termo da Pol. Privac.:
					callActivity(PoliticaPrivacidadeActivity.class);
				}
			});

			if (super.getDadosLoginSegurado().getLogado()) {
				setControlsLogin(View.INVISIBLE);
			} else {
				setControlsLogin(View.VISIBLE);
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * Metodo que configura a unica instancia de DadosLoginSegurado para aplicação
	 */
	protected void setInstanceDadosLoginSegurado() {

		try {
			System.gc();
			Runtime.getRuntime().gc();

			// Somente na primeira execucao do app fara a leitura do preferences e seta no DadosSegurado 
			if (dadosLoginSegurado == null) {
				dadosLoginSegurado = DadosLoginSegurado.getInstance();

				// preferences nao criptografado:
				SharedPreferences sharedPreferences = getSharedPreferences("LibertySeguros", MODE_PRIVATE);
				KEY = sharedPreferences.getString("nome", "");

				if (ValidationUtils.isStringEmpty(KEY)) {
					KEY = ObscuredSharedPreferences.generateKey();
					Editor editor = sharedPreferences.edit();
					editor.putString("nome", KEY);
					editor.commit();
				}

				// preferences criptografado
				final SharedPreferences obscuredSP = new ObscuredSharedPreferences(this, sharedPreferences, KEY);

				String tokenAutenticacao = obscuredSP.getString("tokenAutenticacao", "");
				String usuarioId = obscuredSP.getString("usuarioId", "");
				String tokenNotificacao = obscuredSP.getString("tokenNotificacao", "");
				int appVersion = null != tokenNotificacao ? obscuredSP.getInt("appVersion", 0) : 0;

				dadosLoginSegurado.setCpf(usuarioId);
				dadosLoginSegurado.setTokenAutenticacao(tokenAutenticacao);
				dadosLoginSegurado.setTokenNotificacao(tokenNotificacao);
				dadosLoginSegurado.setAppVersion(appVersion);

				if (ValidationUtils.isStringEmpty(dadosLoginSegurado.getTokenAutenticacao())) {
					dadosLoginSegurado.setLogado(false);
				} else {
					dadosLoginSegurado.setLogado(true);
					if (DeviceUtils.isConnected(this)) {
						// chama login por token em background
						callLogonSeguradoToken();
					} else {
						Util.showToast(this, getString(R.string.NotConnection));
					}
				}
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void callLogonSeguradoToken() {
		try {
			// Inst???ncia que trabalha o retorno do webservice
			WebserviceInterface loginTokenWsInterface = new WebserviceInterface(this) {
				@Override
				public void callBackWebService(String response) {

					DadosLoginSegurado dadosLoginSegurado = CallWebServices.retLogonSeguradoToken(response);

					if (null != dadosLoginSegurado) {
						dadosLoginSegurado.setLogado(true);
						setControlsLogin(View.INVISIBLE);

						//atualizar dados segurado "manter logado"
						atualizaDadosSegurado("");

						//  -- após realizar o login em background, verifica se houve update da versão. Caso positivo registra novamente no GCM:
						if (!ValidationUtils.isStringEmpty(getDadosLoginSegurado().getTokenNotificacao())) {

							int registeredVersion = getDadosLoginSegurado().getAppVersion();
							int currentVersion = getAppVersion(context);

							if (registeredVersion != currentVersion) {

								// -- registra novo tokenPush no GCM
								registerTokeNotification();
							}
						}
					} else {
						logout();
//						getDadosLoginSegurado().setLogado(false);
//						setControlsLogin(View.VISIBLE);
					}
				}

				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					// EPO: Exibe menssagem de erro para o usuário
					logout();
					Util.showException(context, error);
				}
			};

			// Chama o ws  
			CallWebServices.callLogonSeguradoToken(this, getDadosLoginSegurado().getTokenAutenticacao(), getDadosLoginSegurado().getCpf(), loginTokenWsInterface);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	protected void registerTokeNotification() {

//		// Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
//		Intent intent = new Intent(this, RegistrationIntentService.class);
//		startService(intent);


		GcmRegisterInterface registerInterface = new GcmRegisterInterface() {

			@Override
			public void callBackGcmRegister(String tokenNotificacao) {

				// -- send the device token to Provider Liberty
				sendRegistrationIdToBackend(tokenNotificacao);

				atualizaDadosSegurado(tokenNotificacao);
			}

			@Override
			public void callBackGcmRegisterFailWithError(LibertyException error) {
				// EPO: Exibe menssagem de erro para o usuário
				//Util.showException(context, error);
				atualizaDadosSegurado("");
			}
		};

		new GcmRegisterTask(context, registerInterface).execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			System.gc();
			Runtime.getRuntime().gc();

			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_menu_principal, menu);

			//cria o menu e submenus
			//-- verifica se possui token de notificação
			if (super.getDadosLoginSegurado().getLogado()) {
				menu.add(0, ID_MENU_NOTIFICACAO_PUSH, 0, "Notificações");
				menu.add(1, ID_MENU_LOGOUT, 0, "LOGOUT");
				menu.add(2, ID_MENU_POLITICA_PRIVACIDADE, 0, "Política de Privacidade");
			} else {
				menu.add(0, ID_MENU_NOTIFICACAO_PUSH, 0, "Notificações");
				menu.add(1, ID_MENU_POLITICA_PRIVACIDADE, 0, "Política de Privacidade");
			}

			return super.onCreateOptionsMenu(menu);

		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	//	Não compativel com suport v7
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		try {
			System.gc();
			Runtime.getRuntime().gc();

			switch (item.getItemId()) {
				case ID_MENU_LOGOUT:
					logout();
					break;
				case ID_MENU_NOTIFICACAO_PUSH:
					//chama activity de Notificação Push
					callActivity(NotificacaoConsultaActivity.class);
					break;
				case ID_MENU_POLITICA_PRIVACIDADE:
					//chama activity de pol. privacidade
					callActivity(PoliticaPrivacidadeActivity.class);
					break;
			}

			return super.onMenuItemSelected(featureId, item);
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Respons�vel por chamar os activitys
	 *
	 * @param activity
	 */
	private void callActivity(Class activity) {
		try {
			System.gc();
			Runtime.getRuntime().gc();

			Bundle parms = new Bundle();
			Intent it;
			it = new Intent(MenuPrincipalActivity.this, activity);
			it.putExtras(parms);
			startActivityForResult(it, 2);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void callAssistencia24h() {
		try {
			System.gc();
			Runtime.getRuntime().gc();

			BLLPhone phone = new BLLPhone(getApplicationContext());
			String phoneNumber = phone.getPhone();

			if (phoneNumber == null) {
				Intent intent = new Intent(MenuPrincipalActivity.this, ScreenRegisterPhoneNumber.class);
				intent.putExtra("openLob", true);

				startActivityForResult(intent, 2);

			} else if (!phone.getReadMessageModifyPhone() && phone.modifyPhoneNumberDigit9(phoneNumber)) {
				runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog.Builder dialog = new AlertDialog.Builder(MenuPrincipalActivity.this);
						dialog.setIcon(android.R.drawable.ic_dialog_alert);
						dialog.setTitle(R.string.Atention);
						dialog.setMessage(R.string.ModifyPhoneNumber);
						dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								Intent intent = new Intent(MenuPrincipalActivity.this, ScreenRegisterPhoneNumber.class);
								intent.putExtra("openLob", true);

								startActivityForResult(intent, 2);
								finish();
							}
						});
						dialog.show();
					}
				});
			} else {
				startActivityForResult(new Intent(this, ScreenSelectLob.class), 2);
				//				finish();				
			}
		} catch (final Exception e) {
			Util.showException(this, e);
		}

	}

	private void callLogin() {
		try {
			System.gc();
			Runtime.getRuntime().gc();

//	        initiateSkypeUri(this, "skype:Liberty.Matriz?call");

			Intent it = new Intent(this, LoginActivity.class);
			startActivityForResult(it, 1);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Realiza o logout do segurado ou corretor.
	 * Desabilida o botão "Minhas apolices"; Remove os cookies; e clubeliberty de beneficios exclusivos.
	 */
	private void logout() {
		try {
			System.gc();
			Runtime.getRuntime().gc();

			setControlsLogin(View.VISIBLE);

			// EPO: reset nos dados do cookie
			CallWebServices.headerListHttp = null;

			//EPO: remove os dados do clube libertu 
			setClubeLiberty(new ArrayList<Object>());

			//EPO: remove dados do Segurado
			getDadosLoginSegurado().reset();

			//SN12136 - manter logado - reset nos dados de login do segurado
			SharedPreferences sharedPreferences = getSharedPreferences("LibertySeguros", MODE_PRIVATE);
			final SharedPreferences obscuredSP = new ObscuredSharedPreferences(this, sharedPreferences, KEY);
			Editor editor = obscuredSP.edit();
			editor.clear();
			editor.commit();

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			//Retorno do login do Segurado
			if (requestCode == 1) {
				if (resultCode == Activity.RESULT_OK) {
					setControlsLogin(View.INVISIBLE);

					//atuaizar dados segurado "manter logado"
					atualizaDadosSegurado("");

					//  -- após realizar o login. Registra tokenPush no GCM:
					registerTokeNotification();

				} else {
					logout();
				}
			}
			isFromNotification = false;

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	private void atualizaDadosSegurado(String tokenNotificacao) {
		// -- atualiza dados do segurado
		getDadosLoginSegurado().setTokenNotificacao(tokenNotificacao);
		getDadosLoginSegurado().setAppVersion(getAppVersion(this));
		setDadosLoginSegurado(dadosLoginSegurado);
	}


	/**
	 * Seta a visibilidade dos botões. Quando logado não deve exibir.
	 *
	 * @param visibility
	 */
	private void setControlsLogin(int visibility) {
		try {

			if (View.INVISIBLE == visibility) {
				btnLogin.setText(getString(R.string.btn_logout));
				btnMinhaApolice.setBackgroundResource(R.drawable.selector_home_btn_minhas_apolices);
				btnMinhaApolice.setEnabled(true);
				btnMinhaApolice.getBackground().setAlpha(255);
				btnClubeLiberty.setBackgroundResource(R.drawable.selector_home_btn_clubeliberty);
				btnClubeLiberty.setEnabled(true);
				btnClubeLiberty.getBackground().setAlpha(255);
			} else {
				btnLogin.setText(getString(R.string.btn_login));
				btnMinhaApolice.setBackgroundResource(R.drawable.selector_home_btn_minhas_apolices);
				btnMinhaApolice.setEnabled(false);
				btnMinhaApolice.getBackground().setAlpha(45);
				btnClubeLiberty.setBackgroundResource(R.drawable.selector_home_btn_clubeliberty);
				btnClubeLiberty.setEnabled(false);
				btnClubeLiberty.getBackground().setAlpha(45);
			}
			btnLogin.setVisibility(visibility);
			btnCadastre.setVisibility(visibility);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		configScreen();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//logout();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// -- Caso venha de acesso pela notificação vai para activity de notificação:
		if (null != getIntent().getExtras()
				&& getIntent().getExtras().getBoolean(Constants.LM_EXTRA_PUSH)
				&& isFromNotification) {
			callActivity(NotificacaoConsultaActivity.class);
		}

		//<< ANDROID STUDIO
		Log.i("Google Analytics: ", "Menu Principal");
		mTracker.setScreenName("Menu Principal");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		//>>

//		checkPlayServices();
	}

	private void goClubeLiberty(){
		try {

			// Instância que trabalha o retorno do webservice
			WebserviceInterface criarSessaoWsInterface = new WebserviceInterface(this) {

				@Override
				public void callBackWebService(String response) {
					criarSessaoCallback(response);
				}

				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					Util.showException(context, error);
				}
			};

			// Chama o ws
			CallWebServices.callCriarSessao(this, getDadosLoginSegurado().getCpf(), criarSessaoWsInterface);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	public void criarSessaoCallback(String sessionId){

		try {
//			String url = "http://libertyseguros.homolog.clubeben.proxy.media/auth/libertyseguros";
			String url = "http://libertyseguros.clubeben.com.br/auth/libertyseguros";
			WebView webview = new WebView(this);
			setContentView(webview);
			byte[] post = EncodingUtils.getBytes("sessionid=" + sessionId, "BASE64");
			webview.postUrl(url, post);
		}
		catch (Exception e){
			Util.showException(this, e);
		}
	}


//    /**
//     * Check the device to make sure it has the Google Play Services APK. If
//     * it doesn't, display a dialog that allows users to download the APK from
//     * the Google Play Store or enable it in the device's system settings.
//     */
//    private boolean checkPlayServices() {
//
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
//
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (!GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
////	            GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
////	        } else {else
//                Log.i("CheckPlayServices", "This device is not supported.");
//            }
//            return false;
//        }
//        return true;
//    }


}
