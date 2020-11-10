package br.com.libertyseguros.mobile.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.RegisterBeans;
import br.com.libertyseguros.mobile.controller.ProfileController;
import br.com.libertyseguros.mobile.controller.RegisterController;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends BaseActionBar implements View.OnClickListener {

    public static Activity activityBefore;

    private final int RC_SIGN_IN = 998;

    private ProfileController profileController;

    private RegisterController registerController;

    private TextView tvChangePassword;

    private LinearLayout llChangePassword;

    private TextView tvChangeEmail;

    private LinearLayout llChangeEmail;

    private TextView tvName;

    private TextView tvEmail;

    private TextView tvAbout;

    private LinearLayout llAbout;

    private LinearLayout ll_dados;

    private TextView tvExit;

    private LinearLayout llExit;

    private TextView tvDocuments;

    private LinearLayout llDocuments;

    private TextView tvChangePhone;

    private LinearLayout llChangePhone;

    private Toast toast;

    private VehicleAccidentController vehicleAccidentController;

    private Uri outputUri;

    private CallbackManager callbackManager;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private boolean value;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private String numberPolicy;

    private LoginButton loginButton;

    private String idFacebook;

    private GoogleSignInClient mGoogleSignInClient;

    private String photoGoole;

    private SignInButton signInButton;

    private boolean verified = false;

    public void onResume() {
        super.onResume();

        if (tvEmail != null && profileController != null) {
            profileController.reCreateInfoUser(this);
            tvEmail.setText(profileController.getInfoUser().getEmail());
        }
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_profile);

        setTitle(getString(R.string.title_action_bar_5));

        llContent = (LinearLayout) findViewById(R.id.ll_content);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        vehicleAccidentController = new VehicleAccidentController(null);

        mFirebaseAnalytics.setCurrentScreen(Profile.this, "Meus Dados", null);

        profileController = new ProfileController(this);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(profileController.getInfoUser().getUserName());

        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvEmail.setText(profileController.getInfoUser().getEmail());

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(profileController.getInfoUser().getUserName().substring(0, 1), getResources().getColor(R.color.background_status_bar));

        ImageView ivLetter = (ImageView) findViewById(R.id.iv_letter);
        ivLetter.setImageDrawable(drawable1);


        CircleImageView ci = (CircleImageView) findViewById(R.id.profile_image);

        ci.setOnClickListener(this);

        ivLetter.setOnClickListener(this);

        if (profileController.getImageUser(this, ci)) {
            ci.setVisibility(View.VISIBLE);
            ivLetter.setVisibility(View.GONE);
        } else {
            ci.setVisibility(View.GONE);
            ivLetter.setVisibility(View.VISIBLE);
        }

        tvChangePassword = (TextView) findViewById(R.id.tv_change_password);
        llChangePassword = (LinearLayout) findViewById(R.id.ll_change_password);
        tvChangePassword.setOnClickListener(this);
        llChangePassword.setOnClickListener(this);

        tvChangeEmail = (TextView) findViewById(R.id.tv_change_email);
        llChangeEmail = (LinearLayout) findViewById(R.id.ll_change_email);
        tvChangeEmail.setOnClickListener(this);

        tvAbout = (TextView) findViewById(R.id.tv_change_email);
        llAbout = (LinearLayout) findViewById(R.id.ll_about);
        tvAbout.setOnClickListener(this);
        llAbout.setOnClickListener(this);

        ll_dados = findViewById(R.id.ll_dados);
        ll_dados.setOnClickListener(this);

        tvDocuments = (TextView) findViewById(R.id.tv_documents);
        llDocuments = (LinearLayout) findViewById(R.id.ll_documents);
        tvDocuments.setOnClickListener(this);
        llDocuments.setOnClickListener(this);

        tvChangePhone = (TextView) findViewById(R.id.tv_update_phone);
        llChangePhone = (LinearLayout) findViewById(R.id.ll_update_phone);
        tvChangePhone.setOnClickListener(this);
        llChangePhone.setOnClickListener(this);

        tvAbout = (TextView) findViewById(R.id.tv_about);
        llAbout = (LinearLayout) findViewById(R.id.ll_about);

        tvExit = (TextView) findViewById(R.id.tv_exit);
        llExit = (LinearLayout) findViewById(R.id.ll_exit);
        tvExit.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setBackgroundResource(R.drawable.bt_login_facebook);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setText("Login");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = (SignInButton) findViewById(R.id.login_google);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText(getString(R.string.google_button_profile));

        if (!profileController.getInfoUser().isHasFacebook()) {
            loginButton.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.GONE);
        }

        if (!profileController.getInfoUser().isHasGooglePlus()) {
            signInButton.setVisibility(View.VISIBLE);
        } else {
            signInButton.setVisibility(View.GONE);
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        showLoading(true);

                        //Log.i(Config.TAG, "success set Register facebook");

                        //loginController.setIdFacebook(loginResult.getAccessToken().getUserId() + "");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        showLoading(true);

                                        try {
                                            photoGoole = "";
                                            setRegister(object.getString("id"), true);


                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        //Log.i(Config.TAG, exception.toString());

                    }
                });


        configDialog();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_change_password:
            case R.id.ll_change_password:
                profileController.openScreen(Profile.this, 1);
                break;
            case R.id.tv_change_email:
            case R.id.ll_change_email:
                profileController.openScreen(Profile.this, 2);
                break;
            case R.id.tv_about:
            case R.id.ll_about:
                profileController.openSupport(Profile.this);
                break;
            case R.id.ll_dados:
                profileController.openLgpd(Profile.this);
                break;
            case R.id.tv_exit:
            case R.id.ll_exit:
                profileController.logout(Profile.this);
                break;
            case R.id.tv_documents:
            case R.id.ll_documents:
                profileController.openScreen(Profile.this, 3);
                break;
            case R.id.tv_update_phone:
            case R.id.ll_update_phone:
                profileController.openScreen(Profile.this, 4);
                break;
            case R.id.profile_image:
            case R.id.iv_letter:
                //captureImage();
                break;
            case R.id.login_google:
                profileController.signInGoogle(this, mGoogleSignInClient, RC_SIGN_IN);
                break;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.i(Config.TAG, "onActivityResult");
        Uri uriImage = null;
        String path = "";
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Log.i(Config.TAG, "OK Camera");

            if (data != null) {
                uriImage = data.getData();

                if (uriImage == null) {

                    try {

                        uriImage = vehicleAccidentController.getUri();
                        vehicleAccidentController.getArrayOutPutfileUri().add(uriImage.toString());

                        path = vehicleAccidentController.getRealPathFromURI(uriImage, Profile.this);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        vehicleAccidentController.setUri(uriImage);
                        vehicleAccidentController.getArrayOutPutfileUri().add(uriImage.toString());

                        path = vehicleAccidentController.getRealPathFromURI(uriImage, Profile.this);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Intent cropIntent = new Intent("com.android.camera.action.CROP");

                cropIntent.setDataAndType(uriImage, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("return-data", true);


                Crop.of(Uri.fromFile(new File(path)), outputUri).asSquare().start(Profile.this);
                ;
            }

        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {

        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            handleResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String photo = "";

            if (account.getPhotoUrl() != null) {
                photo = account.getPhotoUrl().toString();
            }

            String id = account.getId();
            photoGoole = photo;
            setRegister(id, false);
            //Log.i(Config.TAG, "Google ok: " + name + " - " + email + " - " + id);
        } else {
            //Log.i(Config.TAG, "Google Error");
            Toast toast = Toast.makeText(this, getString(R.string.google_sign_error), Toast.LENGTH_SHORT);
            toast.show();
        }

        /*//Remover - apenas para teste pois nao funcionado não assinado
        photoGoole = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeGYabOnW8fIZNTjACJfzgS82zLD92t7jZhYnILf7yRjgcFI6a";
        setRegister("111178265146499421297", false);*/
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        VehicleAccidentModel.CAMERA_PIC_REQUEST_CODE);
            } else {
                vehicleAccidentController.openMakePhotoIntent(getString(R.string.choose_photo), Profile.this);
            }
        } else {
            vehicleAccidentController.openMakePhotoIntent(getString(R.string.choose_photo), Profile.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VehicleAccidentModel.CAMERA_PIC_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                vehicleAccidentController.openMakePhotoIntent(getString(R.string.choose_photo), Profile.this);

            } else {
                toast = Toast.makeText(Profile.this, getString(R.string.error_camera), Toast.LENGTH_SHORT);
                toast.show();

            }
        }
    }

    /* Show progress loading
  * @param v
  * @param m
  */
    private void showLoading(boolean v) {
        this.value = v;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llLoading.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    /**
     * Config Dialogs
     */
    private void configDialog() {
        dialogMessage = new Dialog(this, R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();

                if (tvMessageDialog.getText().toString().equals(getString(R.string.register_ok))) {
                    finish();
                }
            }
        });

        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogMessageTwoButton.setCancelable(false);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);

                registerController.register(Profile.this, numberPolicy, "", profileController.getInfoUser().getCpfCnpj(), profileController.getInfoUser().getEmail());


            }
        });

    }


    public void setRegister(final String idFacebook, boolean isFacebook) {
        this.idFacebook = idFacebook;
        verified = false;

        //Log.i(Config.TAG, "Start set Register");

        registerController = new RegisterController(new OnConnectionResult() {
            @Override
            public void onError() {

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                showLoading(false);
                                //Log.i(Config.TAG, "error set Register");
                                LoginManager.getInstance().logOut();
                                if (verified) {
                                    RegisterBeans message = registerController.getRegisterMessage();
                                    tvMessageDialog.setText(message.getMessage());
                                    dialogMessage.show();

                                } else {
                                    if (registerController.getTypeError() == 1) {
                                        dialogMessageTwoButton.show();
                                    } else {

                                        if (registerController.getMessage().getMessage() == null) {
                                            dialogMessageTwoButton.show();
                                        } else {
                                            tvMessageDialog.setText(registerController.getMessage().getMessage());
                                            dialogMessage.show();
                                        }
                                    }
                                }


                                showLoading(false);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess() {


                try {

                    if (verified) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Log.i(Config.TAG, "success set Register");
                                LoginManager.getInstance().logOut();

                                showLoading(false);
                                if (registerController.isFacebook()) {
                                    tvMessageDialog.setText(getString(R.string.message_sucess_login_before_facebook));
                                } else {
                                    tvMessageDialog.setText(getString(R.string.message_sucess_login_before_google));
                                }
                                loginButton.setVisibility(View.GONE);
                                signInButton.setVisibility(View.GONE);

                                profileController.saveIdFacebook(idFacebook, registerController.isFacebook(), Profile.this);
                                dialogMessage.show();

                            }
                        });
                    } else {
                        verified = true;
                        registerController.registerSTEP2(Profile.this, "", profileController.getInfoUser().getCpfCnpj(), profileController.getInfoUser().getEmail(), photoGoole);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        }, idFacebook, true);

        LoadFile lf = new LoadFile();

        showLoading(true);
        registerController.setFacebook(isFacebook);
        numberPolicy = lf.loadPref(Config.TAG, Profile.this, Config.TAGPOLICYNUMBER);
        registerController.validRegister(Profile.this, 0, numberPolicy, profileController.getInfoUser().getCpfCnpj());
        //registerController.register(Profile.this, numberPolicy, "", profileController.getInfoUser().getCpfCnpj(), profileController.getInfoUser().getEmail());

    }
}
