package br.com.libertyseguros.mobile.beans;

import br.com.libertyseguros.mobile.R;

public class PaymentPriceBeans {

    private String value;

    private boolean sucesso;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        sucesso = sucesso;
    }

    public String getFormattedValue(){
        String money = value.replace(".", ",");
        return "R$ " + money;

    }
}
