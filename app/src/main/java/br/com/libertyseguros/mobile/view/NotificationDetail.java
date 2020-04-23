package br.com.libertyseguros.mobile.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;

public class NotificationDetail extends BaseActionBar {

    private static String KEY_MESSAGE = "key_message";

    private TextView messageTextView;

    public static Intent newIntent(Context context, String message) {
        Intent intent = new Intent(context, NotificationDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_MESSAGE, message);
        LoadFile loadFile = new LoadFile();
        return intent;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_notification_detail);

        String message = getIntent().getExtras().getString(KEY_MESSAGE);

        getSupportActionBar().setTitle(getString(R.string.title_action_bar_10));

        messageTextView = (TextView) findViewById(R.id.message_text_view);
        messageTextView.setText(message);
    }
}
