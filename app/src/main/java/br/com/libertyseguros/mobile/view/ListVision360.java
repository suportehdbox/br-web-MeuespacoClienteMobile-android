package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListAssistanceVisionAdapter;
import br.com.libertyseguros.mobile.adapter.ListVisionAdapter;
import br.com.libertyseguros.mobile.controller.ListVision360Controller;
import br.com.libertyseguros.mobile.util.LocaleUtils;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;

public class ListVision360 extends BaseActionBar{

    private ListVision360Controller listVision360Controller;

    private ListView lvVision;

    private ListView lvAssistanceVision;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;
    private TextView tvHeader;
    private TextView tvSum;

    private CardView cardView;

    private LinearLayout llProgress;

    private ViewGroup llContent;

    private String policy;


    public void onResume(){
        super.onResume();
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_vision_360);

        mFirebaseAnalytics.setCurrentScreen(this, "Lista Visão 360", null);

        llProgress = findViewById(R.id.ll_loading);
        lvVision = findViewById(R.id.lv_vision);
        lvAssistanceVision = findViewById(R.id.lv_assistance_vision);
        llContent = findViewById(R.id.content_linear_layout);
        tvSum = findViewById(R.id.tv_sum);
        tvHeader = findViewById(R.id.tv_header);

        cardView = findViewById(R.id.value_card_view);

        listVision360Controller = new ListVision360Controller(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {

                            if (listVision360Controller.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {
                                tvMessageDialog.setText(listVision360Controller.getMessage().getMessage());
                                dialogMessage.show();
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        }
                    });
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if(listVision360Controller.getListVision360Beans().getEventos() != null){
                                    lvVision.setAdapter(new ListVisionAdapter(ListVision360.this, listVision360Controller.getListVision360Beans().getEventos()));
                                }


                                lvAssistanceVision.setAdapter(new ListAssistanceVisionAdapter(ListVision360.this, listVision360Controller.getListVision360Beans().getAssistencia()));

                                setListViewHeightBasedOnChildren(lvVision);
                                setListViewHeightBasedOnChildren(lvAssistanceVision);

                                BigDecimal sum = new BigDecimal(Double.parseDouble(listVision360Controller.getListVision360Beans().getTotalDesconto()));
                                BigDecimal header = new BigDecimal(Double.parseDouble(listVision360Controller.getListVision360Beans().getTotalPremio()));

                                tvSum.setText(getString(R.string.sum_detail_vision, toBrazilianRealCurrency(sum)));

                                if(sum.signum() == 0) {
                                    cardView.setVisibility(View.GONE);
                                }

                                tvHeader.setText(getString(R.string.header_detail_vision, toBrazilianRealCurrency(header)));


                                showLoading(false);
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }

                    });
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        configDialog();


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            policy =   extras.getString("policy");
        }

        showLoading(true);
        listVision360Controller.getList(ListVision360.this, policy);

    }

    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                finish();

            }
        });


        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogMessageTwoButton.setCancelable(false);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();

                finish();
            }
        });

        TextView tvTryAgain = dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);
                listVision360Controller.getList(ListVision360.this, policy);

            }
        });

    }

    /* Show progress loading
    *
    * @param v
    * @param m
    */
    private void showLoading(final boolean v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            if (v) {
                llProgress.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
            } else {
                llProgress.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
            }
            }
        });

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String toBrazilianRealCurrency(BigDecimal value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(
                LocaleUtils.getPortugueseBrazilianLocale());
        return numberFormat.format(value);
    }

}
