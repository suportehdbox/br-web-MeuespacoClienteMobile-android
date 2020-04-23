package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ChangeEmailOffController;
import br.com.libertyseguros.mobile.model.ChangeEmailOffModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ChangeEmailStepTwo extends BaseActionBar implements View.OnClickListener{

    private ChangeEmailOffController changeEmailController;

    private TextView tvMessageDialog;

    private ButtonViewCustom btNext;

    private boolean value;

    private LinearLayout llLoading;

    private ScrollView svContent;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private LinearLayout llContent;

    private EditTextCustom editText;

    private int index;

    private ImageView ivSteps;

    public void onResume(){
        super.onResume();

        if(ChangeEmailOffModel.isExit()){
            finish();
        }
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_email_2);

        setTitle(getString(R.string.title_action_bar_4));

        mFirebaseAnalytics.setCurrentScreen(this, "Troca Email", null);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            index = Integer.parseInt(extras.getString("index"));
        }

        ivSteps = (ImageView) findViewById(R.id.iv_step);

        switch(index){
            case 0:
                ivSteps.setImageDrawable(getResources().getDrawable(R.drawable.step_two));
                break;
            case 1:
                ivSteps.setImageDrawable(getResources().getDrawable(R.drawable.step_three));
                break;
            case 2:
                ivSteps.setImageDrawable(getResources().getDrawable(R.drawable.step_four));
                break;
        }

        changeEmailController = new ChangeEmailOffController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                showLoading(false);
                                if (changeEmailController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {

                                    if (changeEmailController.getMessage() == null) {
                                        dialogMessageTwoButton.show();
                                    } else {
                                        tvMessageDialog.setText(changeEmailController.getMessage().getMessage());
                                        dialogMessage.show();
                                    }
                                }

                                changeEmailController.openChangeEmailStepThree(ChangeEmailStepTwo.this);
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
                try{
                    showLoading(false);
                    changeEmailController.openChangeEmailStepThree(ChangeEmailStepTwo.this);
                }catch(Exception ex){
                    ex.printStackTrace();
                }


            }
        });


        btNext = (ButtonViewCustom) findViewById(R.id.bt_next);
        btNext.setOnClickListener(this);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        svContent = (ScrollView) findViewById(R.id.sv_content);

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        configDialog();

        createQuestion();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_next:

                boolean error = false;

                    if(editText.getText().length() < 1){
                        editText.showMessageError(getString(R.string.error_change_email_answer));
                        error = true;
                    }


                if(!error){
                    if(index < 2){
                        changeEmailController.openPartTwo(ChangeEmailStepTwo.this, index + 1);
                    } else {
                        //showLoading(true);
                       // changeEmailController.forgotEmailSendAnswers(ChangeEmailStepTwo.this);
                        changeEmailController.openChangeEmailStepThree(ChangeEmailStepTwo.this);
                    }
                }

                break;
        }
    }


    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
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
                ChangeEmailOffController.setExit(true);
                finish();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);

            }
        });

    }


    private void createQuestion(){
            View question =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.question_email_change, null, false);

            TextView tvQuestion = (TextView) question.findViewById(R.id.tv_question);
            tvQuestion.setText(changeEmailController.getQuestionBeans().getQuestion().getQuestionArray()[index].getDescricao());

            EditTextCustom etQuestion = new EditTextCustom(ChangeEmailStepTwo.this);

            ChangeEmailOffController.addEditText(etQuestion, index);

            editText = etQuestion;


            LinearLayout linearLayout = (LinearLayout) question.findViewById(R.id.ll_edittext);
            linearLayout.addView(etQuestion.config("", "", "", 1));

            llContent.addView(question);

    }


    /*Show progress loading
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
                    svContent.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    svContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
