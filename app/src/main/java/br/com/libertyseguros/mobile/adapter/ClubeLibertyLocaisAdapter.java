package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;

public class ClubeLibertyLocaisAdapter extends BaseAdapter {

	private Context context;
	private List<Object> itens;

	public ClubeLibertyLocaisAdapter(Context context, List<Object> itens) {
		super();
		this.context = context;
		this.itens = itens;
	}

	@Override
	public int getCount() {
		return itens.size();
	}

	@Override
	public Object getItem(int location) {
		return itens.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		try {
			Map<String, Object> campos = (Map<String, Object>) itens.get(i);

			if (view == null)
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.itemlistview_clubelibertylocais, null);
			}

			String titulo = (String)campos.get("Titulo");
			TextView categoria = (TextView)view.findViewById(R.id.textItemClubeLiberty);
			categoria.setText(titulo);

			
			Bitmap bmp = (Bitmap)campos.get("ImageBmp");
			if (bmp != null) {
				ImageView imgItem = (ImageView)view.findViewById(R.id.imgItemClubeLiberty);
				imgItem.setImageBitmap(bmp);
			}
			
			//--------------------------------------------------------------------------------
			//--------------------------------------------------------------------------------

			return view;

		} catch (Exception e) {
			Util.showException(this.context, e);
			return null;
		}
	}
}
