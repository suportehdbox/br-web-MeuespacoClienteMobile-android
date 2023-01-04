package br.com.libertyseguros.mobile.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
