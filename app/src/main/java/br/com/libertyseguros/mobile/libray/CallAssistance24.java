package br.com.libertyseguros.mobile.libray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;





public class CallAssistance24 {

    private Activity activity;

    public void call(Activity context) {
        this.activity = context;
/*
        try {
            System.gc();
            Runtime.getRuntime().gc();

            BLLPhone phone = new BLLPhone(activity);
            String phoneNumber = phone.getPhone();

            if (phoneNumber == null) {
                Intent intent = new Intent(activity, ScreenRegisterPhoneNumber.class);
                intent.putExtra("openLob", true);

                activity.startActivityForResult(intent, 2);

            } else if (!phone.getReadMessageModifyPhone() && phone.modifyPhoneNumberDigit9(phoneNumber)) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                        dialog.setTitle(R.string.Atention);
                        dialog.setMessage(R.string.ModifyPhoneNumber);
                        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(activity, ScreenRegisterPhoneNumber.class);
                                intent.putExtra("openLob", true);

                                activity.startActivityForResult(intent, 2);
                                activity.finish();
                            }
                        });
                        dialog.show();
                    }
                });
            } else {
                activity.startActivityForResult(new Intent(activity, ScreenSelectLob.class), 2);
                //				finish();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }*/

//        Intent intent = new Intent(activity, TesteActivity.class);
//        activity.startActivity(intent);

    }

}
