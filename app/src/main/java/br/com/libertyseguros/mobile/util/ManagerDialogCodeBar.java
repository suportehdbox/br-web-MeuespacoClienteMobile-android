package br.com.libertyseguros.mobile.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.BarCodeBeans;

public class ManagerDialogCodeBar {
    private final String linkSantander = "https://www.santander.com.br/br/resolva-on-line/reemissao-de-boleto-vencido";
    private final Dialog dialogCodBar;
    private final TextView tvTitle;
    private final TextView tvInstructionsCodeBar;
    private final TextView tvInstructionsFullCodeBar;
    private final TextView tvNumber;
    private final TextView btInstructionsFull;
    private final ImageView imgQrCode;
    private final Button btSiteSantander;
    private final Activity context;
    private BarCodeBeans barCodeBeans;
    private Toast toast;
    private Dialog dialogMessage;
    private TextView tvMessageDialog;
    private final Button btCopyCod;

    public ManagerDialogCodeBar(Activity contextCurrent) {
        this.context = contextCurrent;

        dialogCodBar = new Dialog(context, R.style.AppThemeDialog);
        dialogCodBar.setCancelable(true);
        dialogCodBar.setContentView(R.layout.dialog_barcode);
        imgQrCode = dialogCodBar.findViewById(R.id.qrcodeView);
        tvTitle = dialogCodBar.findViewById(R.id.tv_title_cod_bar);
        tvInstructionsCodeBar = dialogCodBar.findViewById(R.id.tv_instructions_cod_bar);
        tvInstructionsFullCodeBar = dialogCodBar.findViewById(R.id.tv_instructions_full_cod_bar);
        tvInstructionsFullCodeBar.setVisibility(View.GONE);
        tvNumber = dialogCodBar.findViewById(R.id.tv_number);
        dialogCodBar.findViewById(R.id.qrcodeBackdrop).setOnClickListener(v -> {
            dialogCodBar.dismiss();
        });
        btCopyCod = dialogCodBar.findViewById(R.id.bt_copy_cod);
        btCopyCod.getBackground().clearColorFilter();
        btCopyCod.setOnClickListener(v -> {
            try {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
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
        });
        btSiteSantander = dialogCodBar.findViewById(R.id.bt_site_santander);
        btSiteSantander.getBackground().clearColorFilter();
        btSiteSantander.setOnClickListener(v -> {
            if (barCodeBeans.getSummaryInstructions() == null) {
                dialogCodBar.dismiss();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(linkSantander));
                context.startActivity(intent);
            }
        });
        btInstructionsFull = dialogCodBar.findViewById(R.id.tv_instructions_full);
        btInstructionsFull.setOnClickListener(v -> {
            tvMessageDialog.setText(barCodeBeans.getCompleteInstructions());
            dialogMessage.show();
        });
        LinearLayout llInstructionsFull = dialogCodBar.findViewById(R.id.ll_instructions_full);
        llInstructionsFull.setOnClickListener(v -> tvInstructionsFullCodeBar.setVisibility(View.VISIBLE));
        configDialogMessage();
    }

    public void createDialog(BarCodeBeans barCodeBeans) {
        this.barCodeBeans = barCodeBeans;
//        if (barCodeBeans.get)
        String digitableLine = barCodeBeans.getDigitableLine();
        if (digitableLine.contains("gov.bcb.pix")) {
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(digitableLine, BarcodeFormat.QR_CODE, 256, 256);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                // FIXME: Make this async (too much work on main thread)
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                tvTitle.setText(context.getString(R.string.SegundaViaPix));
                imgQrCode.setVisibility(View.VISIBLE);
                tvInstructionsCodeBar.setVisibility(View.GONE);
                btSiteSantander.setVisibility(View.GONE);
                imgQrCode.setImageBitmap(bmp);
                btCopyCod.setText(R.string.CopiarPix);
                tvNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                tvNumber.getLayoutParams().height = 60;
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else if (barCodeBeans.getSummaryInstructions() == null) {
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
        tvNumber.setText(digitableLine);
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
        tvMessageDialog = dialogMessage.findViewById(R.id.tv_dialog_message);
        TextView tvOk = dialogMessage.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> dialogMessage.dismiss());
    }
}
