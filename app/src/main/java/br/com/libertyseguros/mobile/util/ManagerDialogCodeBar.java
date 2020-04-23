package br.com.libertyseguros.mobile.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.BarCodeBeans;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ManagerDialogCodeBar {

    private String linkSantander = "https://www.santander.com.br/br/resolva-on-line/reemissao-de-boleto-vencido";

    private Dialog dialogCodBar;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private TextView tvTitle;

    private TextView tvInstructionsCodeBar;

    private TextView tvInstructionsFullCodeBar;

    private TextView tvNumber;

    private TextView btInstructionsFull;

    private Button btCopyCod;

    private Button btSiteSantander;

    private LinearLayout llInstructionsFull;

    private Activity context;

    private BarCodeBeans barCodeBeans;

    private Toast toast;

    public ManagerDialogCodeBar(Activity contextCurrent) {
        this.context = contextCurrent;

        dialogCodBar = new Dialog(context, R.style.AppThemeDialog);
        dialogCodBar.setCancelable(true);
        dialogCodBar.setContentView(R.layout.dialog_barcode);

        tvTitle = (TextView) dialogCodBar.findViewById(R.id.tv_title_cod_bar);
        tvInstructionsCodeBar = (TextView) dialogCodBar.findViewById(R.id.tv_instructions_cod_bar);
        tvInstructionsFullCodeBar = (TextView) dialogCodBar.findViewById(R.id.tv_instructions_full_cod_bar);
        tvInstructionsFullCodeBar.setVisibility(View.GONE);

        tvNumber = (TextView) dialogCodBar.findViewById(R.id.tv_number);

        btCopyCod = (Button) dialogCodBar.findViewById(R.id.bt_copy_cod);
        btCopyCod.getBackground().clearColorFilter();

        btCopyCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", barCodeBeans.getDigitableLine());
                    clipboard.setPrimaryClip(clip);

                    if (toast != null) {
                        toast.cancel();
                    }

                    toast = Toast.makeText(context, context.getString(R.string.clipboard), Toast.LENGTH_SHORT);
                    toast.show();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btSiteSantander = (Button) dialogCodBar.findViewById(R.id.bt_site_santander);
        btSiteSantander.getBackground().clearColorFilter();
        btSiteSantander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barCodeBeans.getSummaryInstructions() == null) {
                    dialogCodBar.dismiss();
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(linkSantander));
                    context.startActivity(intent);
                }
            }
        });

        btInstructionsFull = (TextView) dialogCodBar.findViewById(R.id.tv_instructions_full);
        btInstructionsFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessageDialog.setText(barCodeBeans.getCompleteInstructions());
                dialogMessage.show();
            }
        });

        llInstructionsFull = (LinearLayout) dialogCodBar.findViewById(R.id.ll_instructions_full);
        llInstructionsFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInstructionsFullCodeBar.setVisibility(View.VISIBLE);
            }
        });

        configDialogMessage();

    }

    public void createDialog(BarCodeBeans barCodeBeans) {
        this.barCodeBeans = barCodeBeans;

        if (barCodeBeans.getSummaryInstructions() == null) {
            tvTitle.setText(context.getString(R.string.title_bar_code_dialog));
            tvInstructionsCodeBar.setText(context.getString(R.string.title_2_bar_code_dialog));
            tvInstructionsFullCodeBar.setText(context.getString(R.string.summary_instructions));
            tvInstructionsFullCodeBar.setVisibility(View.VISIBLE);
            btSiteSantander.setText(context.getResources().getString(R.string.button_ok_understand));
        } else {
            tvTitle.setText(context.getString(R.string.titulo_boleto_vencido));
            tvInstructionsCodeBar.setText(barCodeBeans.getSummaryInstructions());
            tvInstructionsFullCodeBar.setText(context.getString(R.string.sub_titulo_boleto_vencido));
            tvInstructionsFullCodeBar.setVisibility(View.VISIBLE);
            btSiteSantander.setText(context.getResources().getText(R.string.button_site_santander));
        }

        tvNumber.setText(barCodeBeans.getDigitableLine());
        if (barCodeBeans.getCompleteInstructions() != null) {
            btInstructionsFull.setVisibility(View.VISIBLE);

        } else {
            btInstructionsFull.setVisibility(View.GONE);
        }

        dialogCodBar.show();
    }

    private void configDialogMessage() {
        dialogMessage = new Dialog(context, R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message_bar_code);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });
    }

}
