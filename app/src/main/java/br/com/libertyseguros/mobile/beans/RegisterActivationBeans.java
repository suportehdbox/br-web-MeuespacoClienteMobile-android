package br.com.libertyseguros.mobile.beans;

public class RegisterActivationBeans {

    private boolean Sucesso;

    private String message;

    public boolean isSucesso() {
        return Sucesso;
    }

    public void setSucesso(boolean sucesso) {
        Sucesso = sucesso;
    }

    public String getMensagem() {
        return message;
    }

    public void setMensagem(String mensagem) {
        message = mensagem;
    }
}
