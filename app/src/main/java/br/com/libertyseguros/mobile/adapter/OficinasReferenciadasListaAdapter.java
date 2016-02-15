package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.libertyseguros.mobile.R;

public class OficinasReferenciadasListaAdapter extends BaseAdapter{

	private Context context;
	private List<Object> itens;

	public OficinasReferenciadasListaAdapter(Context context, List<Object> itens) {
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

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> campos = (Map<String, Object>) itens.get(i);
		
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.itemlistview_oficinas_referenciadas_lista, null);
        }

		String nome = (String)campos.get("Nome");
		String endereco = (String)campos.get("Endereco");
		String numero = (String)campos.get("Numero");
		String logradouro = (String)campos.get("Logradouro");
		String bairro = (String)campos.get("Bairro");
		String cidade = (String)campos.get("Cidade");
		String uf = (String)campos.get("UF");
		String cep = (String)campos.get("CEP");
		String distancia = (String)campos.get("Distancia");
		
		TextView textItemTituloOficina = (TextView)view.findViewById(R.id.textItemTituloOficina);
		textItemTituloOficina.setText(nome);

		TextView textItemEnderecoOficina = (TextView)view.findViewById(R.id.textItemEnderecoOficina);
		textItemEnderecoOficina.setText(logradouro + ": " + endereco + "," + numero + " - " + bairro + "/" + cep);
		
		TextView textItemDistanciaOficina = (TextView)view.findViewById(R.id.textItemDistanciaOficina);
		textItemDistanciaOficina.setText(distancia + "Km");

        return view;
	}
	
}
