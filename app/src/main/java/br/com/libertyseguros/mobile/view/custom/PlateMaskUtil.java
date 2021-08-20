package br.com.libertyseguros.mobile.view.custom;

import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

public abstract class PlateMaskUtil {

    private static final String mask = "###-####";

    public static String unmask(String s) {
        return s.replaceAll("[^a-zA-Z0-9]*", "");
    }

    public static TextWatcher insert(final EditText editText) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = PlateMaskUtil.unmask(s.toString());

                boolean applymaks = true;

                try {
                    if (editText.getText().length() > 0) {
                        int max = editText.getText().toString().length();
                        if (max >= 3) {
                            max = 3;
                        }
                        int num = Integer.parseInt(editText.getText().toString().substring(0, max));
                    } else {
                        int num = Integer.parseInt(str);
                    }

                    applymaks = false;
                    // is an integer!
                } catch (NumberFormatException e) {
                    //nothing to do

                }

                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                if (applymaks) {
                    for (char m : mask.toCharArray()) {

                        if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                            mascara += m;
                            continue;
                        }


                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                } else {
                    mascara = str;
                    if (str.length() > 15) {
                        mascara = str.substring(0, 15);
                    }
                }
                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }


}