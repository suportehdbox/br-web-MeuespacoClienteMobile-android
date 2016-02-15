package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.OficinasReferenciadasListaAdapter;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class OficinasReferenciadasListaActivity extends Activity {

	private ListView lvwList;
	private List<Object> oficinas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_oficinas_referenciadas_lista);
			Util.setTitleNavigationBar(this, R.string.title_activity_oficinas_referenciadas);

			Intent it = getIntent();
			if (it == null) return;		

			Bundle parms = it.getExtras();
			oficinas = (List<Object>) parms.getSerializable(Constants.LM_EXTRA_OFICINAS);

			lvwList = (ListView) findViewById(R.id.lvwOficinas);

			if (oficinas != null) {
				// associa o array � listview
				OficinasReferenciadasListaAdapter adapter = new OficinasReferenciadasListaAdapter(this, oficinas);
				lvwList.setAdapter(adapter);

				lvwList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
						Map<String, Object> itemSel = (Map<String, Object>)lvwList.getAdapter().getItem(position);

						Bundle params = new Bundle();

						params.putSerializable(Constants.LM_EXTRA_ITEM, (Serializable) itemSel);

//						TODO ANDROID STUDIO
//						Intent it = new Intent(OficinasReferenciadasListaActivity.this, OficinasReferenciadasDetalheActivity.class);
//						it.putExtras(params);
//						startActivity(it);
					}
				});
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.oficinas_referenciadas_lista, menu);
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
		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  	super.onPause();
	  	CustomApplication.activityPaused();
	}
}
