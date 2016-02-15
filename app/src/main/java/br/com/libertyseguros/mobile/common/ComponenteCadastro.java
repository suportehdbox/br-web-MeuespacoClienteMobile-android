package br.com.libertyseguros.mobile.common;

import android.app.Activity;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import br.com.libertyseguros.mobile.R;

/**
 * Created by evandro on 10/14/15.
 */
public class ComponenteCadastro {

    private final Activity activity;

    // UI references.
    private LinearLayout componente;
    private EditText txt_cadastro;
    private TextView tvw_cadastro;
    private View linha;
    private ToggleButton btn_img_olho;
    private FrameLayout btn_img_apagar;
    private final boolean isPwd;


    public ComponenteCadastro(Activity activity, int id, int type, int hint, final boolean isPwd)
    {
        this.activity = activity;

        componente   = (LinearLayout) activity.findViewById(id);

        txt_cadastro    = (EditText) componente.findViewById(R.id.txt_cadastro);
        txt_cadastro.setHint(hint);
        txt_cadastro.setText("");
        txt_cadastro.setInputType(type);
        if(type == InputType.TYPE_TEXT_VARIATION_PASSWORD){
            txt_cadastro.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        tvw_cadastro    = (TextView) componente.findViewById(R.id.tvw_cadastro);

        linha           = componente.findViewById(R.id.linha);
        btn_img_olho    = (ToggleButton) componente.findViewById(R.id.btn_olho);
        btn_img_apagar  = (FrameLayout) componente.findViewById(R.id.btn_apagar);

        this.isPwd = isPwd;

        //Configurar a visibilidade
        setupVisibility();

        //Configurar os eventos:
        setListeners();

    }

    private void setListeners() {

        // Clicar para apagar o texto
        btn_img_apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_cadastro.setText("");
            }
        });

        // Tipo Senha: Botão exibe oculta Texto
        View.OnClickListener showText = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start=txt_cadastro.getSelectionStart();
                int end=txt_cadastro.getSelectionEnd();
                if(txt_cadastro.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD){
                    txt_cadastro.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    txt_cadastro.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    //txt_cadastro.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //txt_cadastro.setTransformationMethod(null);
                    //txt_cadastro.setTransformationMethod(new DoNothingTransformation());
                } else {
                    txt_cadastro.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txt_cadastro.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //txt_cadastro.setSelection(txt_cadastro.length());
                txt_cadastro.setSelection(start,end);
                //txt_cadastro.invalidate();
                //componente.invalidate();
                componente.getRootView().invalidate();
            }
        };
        btn_img_olho.setOnClickListener(showText);

        txt_cadastro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // Quando ganhar o Foco
                    linha.setBackgroundColor(activity.getResources().getColor(R.color.foco));
                    linha.invalidate();

                    //Tipo Senha
                    if (isPwd) {
                        btn_img_olho.setVisibility(View.VISIBLE);
                        btn_img_olho.invalidate();
                    }else{
                        btn_img_apagar.setVisibility(View.VISIBLE);
                        btn_img_apagar.invalidate();
                    }

                } else {
                    // Quando perder o foco
                    linha.setBackgroundColor(activity.getResources().getColor(R.color.nofoco));
                    linha.invalidate();

                    // Tipo Senha
                    if (isPwd) {
                        btn_img_olho.setVisibility(View.GONE);
                        btn_img_olho.invalidate();
                    }else{
                        btn_img_apagar.setVisibility(View.GONE);
                        btn_img_apagar.invalidate();
                    }
                }
            }
        });

    }

    // Visibilidade inicial
    private void setupVisibility()
    {
        tvw_cadastro.setVisibility(View.GONE);
        btn_img_olho.setVisibility(View.GONE);
        btn_img_apagar.setVisibility(View.GONE);

        linha.setBackgroundColor(activity.getResources().getColor(R.color.nofoco));
    }

    public void showErro(String errorMsg)
    {
        this.linha.setBackgroundColor(activity.getResources().getColor(R.color.erro));
        this.linha.invalidate();

        this.tvw_cadastro.setText(errorMsg);
        this.tvw_cadastro.setVisibility(View.VISIBLE);
        this.tvw_cadastro.invalidate();
    }

    public void clearErro()
    {
        this.linha.setBackgroundColor(activity.getResources().getColor(R.color.nofoco));
        this.linha.invalidate();

        this.tvw_cadastro.setText("");
        this.tvw_cadastro.setVisibility(View.GONE);
        this.tvw_cadastro.invalidate();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        txt_cadastro.addTextChangedListener(textWatcher);
    }

    public String getText() {
        return txt_cadastro.getText().toString();
    }

    // set the max number of digits the user can enter
    public void setMaxLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        txt_cadastro.setFilters(FilterArray);
    }

    class DoNothingTransformation implements TransformationMethod {

        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }

        @Override
        public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

        }
    }
}
