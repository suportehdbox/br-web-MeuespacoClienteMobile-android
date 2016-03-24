package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.util.EncodingUtils;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

/**
 * Created by evandro on 2/16/16.
 */
public class ClubeBeneficioActivity extends LibertyMobileApp {

    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_clube_beneficios);

            Util.setTitleNavigationBar(this, R.string.title_activity_clube_liberty);

            AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
            mTracker = analyticsApplication.getDefaultTracker(getApplication());

            //ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
            //progressDialog.setMessage(getString(R.string.aguarde));
            //progressDialog.show();

            WebView webview = (WebView) findViewById(R.id.wvClubeLiberty);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webview.setWebViewClient(new WebViewClient());

            Intent it = getIntent();
            if (it == null) return;
            Bundle parms = it.getExtras();

            String postData = "sessionid=" + parms.getString(Constants.LM_EXTRA_SESSIONID);

            //String url = "http://libertyseguros.homolog.clubeben.proxy.media/auth/libertyseguros";
            String url = "http://libertyseguros.clubeben.com.br/auth/libertyseguros";

            webview.postUrl(url, EncodingUtils.getBytes(postData, "utf-8"));

         //   progressDialog.dismiss();
        }
        catch (Exception e){
            Util.showException(this, e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // Inflate the menu; this adds items to the action bar if it is present.
            //getMenuInflater().inflate(R.menu.activity_clube_liberty_locais, menu);
            return true;
        } catch (Exception e) {
            Util.showException(this, e);
            return false;
        }
    }

    /**
     * A��o do bot�o Voltar da barra de navega��o (navigation_bar.xml)
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
    protected void onDestroy() {
        super.onDestroy();
        Util.callGB();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("Google Analytics: ", "Clube Liberty");
        mTracker.setScreenName("Clube Liberty");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        CustomApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomApplication.activityPaused();
    }

}
