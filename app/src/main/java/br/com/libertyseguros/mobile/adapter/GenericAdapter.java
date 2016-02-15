package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.libertyseguros.mobile.R;

/**
 * 
 * @author Evandro
 *
 */
public class GenericAdapter extends BaseAdapter {

	private Context context;
	private List<ItemAdapter> itens;

	public GenericAdapter(Context context, List<ItemAdapter> itens) {
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
		
		ItemAdapter itemAdapter = itens.get(i);
		int[] 		icons 		= itemAdapter.getIcon();
//		Integer		number 		= itemAdapter.getNumber();
		
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	view = inflater.inflate(R.layout.itemlistview_generic, null);
        }

        // Seta o texto principal do item
        TextView textViewTitle = (TextView)view.findViewById(R.id.adapter_item_title);
        textViewTitle.setText(itemAdapter.getText());
		
		// Caso possua um icone seta na posição correspondente
		if(null != icons){
			textViewTitle.setCompoundDrawablesWithIntrinsicBounds(icons[0], icons[1], icons[2], icons[3]);
		}else{
			textViewTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}

		// Caso possua texto secundário seta no item
		TextView textViewText = (TextView)view.findViewById(R.id.adapter_item_text);
		if(null != itemAdapter.getText2()){
			textViewText.setVisibility(TextView.VISIBLE);
			textViewText.setText(itemAdapter.getText2());
			textViewTitle.setPadding (textViewTitle.getPaddingLeft(), textViewTitle.getPaddingTop(), textViewTitle.getPaddingRight(), 0);
		}else{
			textViewText.setVisibility(TextView.GONE);
			textViewText.setText("");
			textViewTitle.setPadding (textViewTitle.getPaddingLeft(), textViewTitle.getPaddingTop(), textViewTitle.getPaddingRight(), 15);
		}
		
//		// Caso possua um número
//		if(null != number && number != 0)
//		{
//		}
		
        return view;
	}

}
