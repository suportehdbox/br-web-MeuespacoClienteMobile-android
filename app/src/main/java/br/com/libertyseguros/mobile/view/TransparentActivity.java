package br.com.libertyseguros.mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class TransparentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
