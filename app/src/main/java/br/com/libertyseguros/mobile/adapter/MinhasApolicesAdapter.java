package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
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

public class MinhasApolicesAdapter  extends BaseAdapter {

	private Context context;
	private List<Object> itens;

	public MinhasApolicesAdapter(Context context, List<Object> itens) {
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
		
		Map<String, Object> campos = (Map<String, Object>) itens.get(i);
		
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.itemlistview_minhas_apolices, null);
        }

        
        String tipoSeguro = (String)campos.get("TipoSeguro");
        String descricao = (String)campos.get("DescricaoItem");
        String nomeProduto = (String)campos.get("NomeProduto");
        
        nomeProduto = Util.formataMinusculaMenosPrimeira(nomeProduto);
        
        TextView textItemDestaqueMinhasApolices = (TextView)view.findViewById(R.id.textItemNroApoliceMinhasApolices);
        if (tipoSeguro.compareTo("AUTO") == 0) {
        	textItemDestaqueMinhasApolices.setText(nomeProduto + " (Placa: " + descricao +")");        	
        }
        else if (tipoSeguro.compareTo("RESIDENCIA") == 0) {
        	textItemDestaqueMinhasApolices.setText(nomeProduto + " (CEP: " + descricao +")");        	
        }
        else if (tipoSeguro.compareTo("PESSOAL") == 0) {
        	textItemDestaqueMinhasApolices.setText(nomeProduto);        	
        }
        else if (tipoSeguro.compareTo("OUTROS") == 0) {
        	textItemDestaqueMinhasApolices.setText(nomeProduto);        	
        }
        else if (tipoSeguro.compareTo("EMPRESARIAL") == 0) {
        	textItemDestaqueMinhasApolices.setText(nomeProduto);        	
        }
        
        
        String apolice = (String)campos.get("NumeroApolice");
        TextView textItemNroApoliceMinhasApolices = (TextView)view.findViewById(R.id.textItemDescricaoMinhasApolices);
        textItemNroApoliceMinhasApolices.setText(apolice);
        
        String situacao = (String)campos.get("SituacaoApolice");
        ImageView imgItem = (ImageView)view.findViewById(R.id.imgItemParcelaVencidaMinhasApolices);
        if (situacao.compareTo("ApoliceAtiva") == 0) {
        	imgItem.setVisibility(View.INVISIBLE);        	
        }else {
        	imgItem.setVisibility(View.VISIBLE);        	
        }

        return view;
	}
	
}
