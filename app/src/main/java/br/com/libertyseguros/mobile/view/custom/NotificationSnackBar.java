package br.com.libertyseguros.mobile.view.custom;


import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.view.NotificationDetail;

public class NotificationSnackBar {

    public static void showNotification(final Context context, View view, final String message) {
        Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(context.getText(R.string.button_detail_notification), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        context.startActivity(NotificationDetail.newIntent(context, message));
                    }
                });
        mSnackbar.setDuration(4000).show();

    }
}
