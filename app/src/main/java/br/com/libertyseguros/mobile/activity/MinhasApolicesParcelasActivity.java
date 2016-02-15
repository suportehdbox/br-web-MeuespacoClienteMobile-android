package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class MinhasApolicesParcelasActivity extends LibertyMobileApp {

	private Map<String, Object> itemSel;
	private List<Object> parcelas;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_minhas_apolices_parcelas);
			Util.setTitleNavigationBar(this, R.string.title_activity_minhas_apolices_parcelas);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			Intent it = getIntent();
			if (it == null) return;

			Bundle parms = it.getExtras();
			itemSel = (Map<String, Object>) parms.getSerializable(Constants.LM_EXTRA_ITEM);
			parcelas = (List<Object>)itemSel.get("Parcelas");

			TableLayout table = (TableLayout)findViewById(R.id.tableParcelas);  
			table.setStretchAllColumns(true);
			table.setShrinkAllColumns(true);
			table.setBackgroundColor(getResources().getColor(R.color.gray_title));

			table.addView(addTitleRow());

			for (Object obj: parcelas) {
				Map<String, Object> objItem = (Map<String, Object>)obj;
				String parcela = (String)objItem.get("NumeroParcela");

				String vencimento = (String)objItem.get("DataVencimento");
				String fmtVencimento = vencimento.substring(8, 10) + "/" +
						vencimento.substring(5, 7) + "/" +
						vencimento.substring(0, 4);

				String valor = (String)objItem.get("ValorParcela");

				boolean quitada = Boolean.parseBoolean((String)objItem.get("Quitada"));
				String status = (quitada ? getString(R.string.quitada) : getString(R.string.aberta));

				table.addView(addLineRow(parcela, fmtVencimento, valor, status));
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private TableRow addTitleRow() {
		try {
			TableRow rowTitle = new TableRow(this);
			rowTitle.setBackgroundColor(getResources().getColor(R.color.white));

			TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			tableRowParams.setMargins(0, 0, 0, 2);
			rowTitle.setLayoutParams(tableRowParams);

			rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);  
			rowTitle.addView(addTextViewTitleRow("Parcela"));
			rowTitle.addView(addTextViewTitleRow("Vencimento"));
			rowTitle.addView(addTextViewTitleRow("Valor"));
			rowTitle.addView(addTextViewTitleRow("Status"));
			return rowTitle;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	private TableRow addLineRow(String parcela, String vencimento, String valor, String status) {
		try {
			TableRow rowLine = new TableRow(this); 
			rowLine.setBackgroundColor(getResources().getColor(R.color.white));

			TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			tableRowParams.setMargins(0, 0, 0, 2);
			rowLine.setLayoutParams(tableRowParams);

			rowLine.setGravity(Gravity.CENTER_HORIZONTAL);  
			rowLine.addView(addTextViewRow(parcela));
			rowLine.addView(addTextViewRow(vencimento));
			rowLine.addView(addTextViewRow(valor));
			rowLine.addView(addTextViewRow(status));
			return rowLine;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	private TextView addTextViewTitleRow(String text) {
		try {
			TextView textView = new TextView(this);

			textView.setPadding(0, 20, 0, 20);
			textView.setText(text);  
			textView.setGravity(Gravity.CENTER_HORIZONTAL);
			textView.setTextColor(getResources().getColor(R.color.gray_text));
			textView.setTypeface(null, Typeface.BOLD);
			return textView;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	private TextView addTextViewRow(String text) {
		try {
			TextView textView = new TextView(this);

			textView.setPadding(0, 20, 0, 20);
			textView.setText(text);  
			textView.setGravity(Gravity.CENTER_HORIZONTAL);
			textView.setTextColor(getResources().getColor(R.color.black));
			return textView;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_minhas_apolices_parcelas, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
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

		Log.i("Google Analytics: ", "Minhas Apólices: Parcelas");
		mTracker.setScreenName("Minhas Apólices: Parcelas");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

//	  CustomApplication.activityResumed();
	}

}
