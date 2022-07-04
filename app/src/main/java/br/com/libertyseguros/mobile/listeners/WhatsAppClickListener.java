package br.com.libertyseguros.mobile.listeners;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class WhatsAppClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri url = Uri.parse("https://api.whatsapp.com/send").buildUpon()
                .appendQueryParameter("phone", "551132061414")
                .appendQueryParameter("text", "Oi liberty, tudo bom?")
                .build();
        i.setData(url);
        v.getContext().startActivity(i);
    }
}
