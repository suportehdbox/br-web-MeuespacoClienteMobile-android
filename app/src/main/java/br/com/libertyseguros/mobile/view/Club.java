package br.com.libertyseguros.mobile.view;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ClubController;
import br.com.libertyseguros.mobile.libray.DownloadImageClub;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;

public class Club extends BaseActionBar implements View.OnClickListener{

    private ClubController clubController;

    private TextView tvTitle;

    private TextView tvDescription;

    private TextView tvLogin;

    private LinearLayout llLoginOn;

    private LinearLayout llLoginOff;

    private ButtonViewCustom btLogin;

    private ButtonViewCustom btRegister;

    private ButtonViewCustom btClub;

    private TextViewCustom tvChangePassword;
    private ImageView ivBackground;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        boolean indisponivel = true;
        if (indisponivel) {
            setContentView(R.layout.activity_club_indisponivel);
            setTitle(getResources().getString(R.string.title_action_bar_11));
            mFirebaseAnalytics.setCurrentScreen(this, "Club Liberty Indisponivel", null);
            return;
        }
        setContentView(R.layout.activity_club);

        setTitle(getResources().getString(R.string.title_action_bar_11));

        mFirebaseAnalytics.setCurrentScreen(this, "Club Liberty", null);

        //bg_club
        ivBackground = (ImageView) findViewById(R.id.bg_club);

        clubController = new ClubController(new OnConnectionResult() {
            @Override
            public void onError() {

            }

            @Override
            public void onSucess() {

            }
        },
        new DownloadImageClub.OnClubImageDownloaded() {
            @Override
            public void OnDownloadComplete(String fileName) {
                loadImageFromFile();
            }
        },
        this);


        loadImageFromFile();

        tvTitle = (TextView) findViewById(R.id.tv_title_club);
        tvTitle.setText(Html.fromHtml(getString(R.string.title_club)));

        tvDescription = (TextView) findViewById(R.id.tv_descritption_club);

        tvLogin = (TextView) findViewById(R.id.tv_login_club);
        tvLogin.setText(Html.fromHtml(getString(R.string.login_club)));

        llLoginOn = (LinearLayout) findViewById(R.id.ll_login_on);

        llLoginOff = (LinearLayout) findViewById(R.id.ll_login_off);

        btLogin = (ButtonViewCustom) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        btClub = (ButtonViewCustom) findViewById(R.id.bt_club);
        btClub.setOnClickListener(this);

        tvChangePassword = (TextViewCustom) findViewById(R.id.tv_change_password);
        tvChangePassword.setOnClickListener(this);

        btRegister = (ButtonViewCustom) findViewById(R.id.bt_register);
        btRegister.setOnClickListener(this);


        if(clubController.isloginOn()){
            llLoginOn.setVisibility(View.GONE);
            llLoginOff.setVisibility(View.VISIBLE);
            tvDescription.setText(Html.fromHtml(getString(R.string.descritption_club_on)));

        } else {
            llLoginOn.setVisibility(View.VISIBLE);
            llLoginOff.setVisibility(View.GONE);
            tvDescription.setText(Html.fromHtml(getString(R.string.descritption_club)));

        }
    }
    private void loadImageFromFile(){
        LoadFile lf = new LoadFile();
        Drawable dr = lf.getImage(this, clubController.getNameImage());

        if(dr != null){
            ivBackground.setImageDrawable(dr);
        }


    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_register:
                clubController.openRegister(this);
                break;
            case R.id.bt_login:
                clubController.openLogin(this);
                break;
            case R.id.tv_change_password:
                clubController.openForgotPassword(this);
                break;
            case R.id.bt_club:
                clubController.openClubWebview(this);
                break;
        }
    }
}
