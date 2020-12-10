package br.com.libertyseguros.mobile.view.custom;

import android.content.Context;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;

public class EditTextCustom {

    private Context context;

    private View view;

    private TextView tvMsgError;

    private TextInputEditText etContent;

    private TextInputLayout textInputLayout;

    private LinearLayout llClear;

    private ImageViewCustom ivClear;

    private boolean hide;

    private String typeMaskDefault;

    private int typeFieldDefault;

    private LinearLayout llLine;

    public EditTextCustom(Context context){
        this.context = context;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.edit_text_custom, null);

        llLine = (LinearLayout) view.findViewById(R.id.ll_line);

        tvMsgError = (TextView) view.findViewById(R.id.tv_msg_error);

        llClear = (LinearLayout) view.findViewById(R.id.ll_clear);

        ivClear = (ImageViewCustom) view.findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordMask();
            }
        });

        etContent = (TextInputEditText) view.findViewById(R.id.et_custom);
        etContent.setFilterTouchesWhenObscured(true);

        textInputLayout  = (TextInputLayout) view.findViewById(R.id.user_text_input_layout);

        llLine = (LinearLayout) view.findViewById(R.id.ll_line);

    }
    public void setMaxLength(int maxLength){

        etContent.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

    }
    public void setLines(){
        etContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etContent.setSingleLine(false);
    }

    public void removeLine(){
        llLine.setVisibility(View.GONE);
    }

    public View config(String textDefault, String hint, String typeMask,  int typeField){
        etContent.setText(textDefault);
        textInputLayout.setHint(hint);
        this.typeMaskDefault = typeMask;
        this.typeFieldDefault = typeField;

        switch(typeField){
            case 2:
                etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 3:
                etContent.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                hide = true;
                etContent.setTransformationMethod(new EditTextPasswordTransformationMethod());
                llClear.setVisibility(View.VISIBLE);
                break;
        }

        etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(typeFieldDefault == 3) {
                    if (hasFocus) {
                        llClear.setVisibility(View.VISIBLE  );
                    } else {
                        llClear.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                llLine.setBackgroundColor(context.getResources().getColor(R.color.line_default_1));
                tvMsgError.setVisibility(View.INVISIBLE);

/*
                if(!typeMaskDefault.equals("")) {
                    String str = MaskEditText.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : typeMaskDefault.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
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
                    isUpdating = true;
                    etContent.setText(mascara);
                    etContent.setSelection(mascara.length());

                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    /**
     * Get text etContent
     * @return
     */
    public String getText(){
        return etContent.getText().toString();
    }

    /**
     * Show Message Error
     */
    public void showMessageError(String msg){
        llLine.setBackgroundColor(context.getResources().getColor(R.color.line_default_4));
        tvMsgError.setText(msg);
        tvMsgError.setVisibility(View.VISIBLE);
    }

    /**
     * Unmask
     * @param s
     * @return
     */
    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }


    public class EditTextPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

    private void changePasswordMask(){
        if(hide){
            hide = false;
            etContent.setTransformationMethod(null);
            ivClear.setImageDrawable(context.getResources().getDrawable(R.drawable.password_on));
        }else {
            hide = true;
            etContent.setTransformationMethod(new EditTextPasswordTransformationMethod());
            ivClear.setImageDrawable(context.getResources().getDrawable(R.drawable.password_off));

        }
    }

    /**getEditText
     * Get EditText
     * @return
     */
    public EditText getEditText(){
        return etContent;
    }


    /**getEditText
     * Get EditText
     * @return
     */
    public TextInputLayout getTextInputLayout(){
        return textInputLayout;
    }
}
