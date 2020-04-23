package br.com.libertyseguros.mobile.beans;


public class RegisterBeans {

    private String message;
    private String messageCode;
    private Boolean sucesso;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        messageCode = messageCode;
    }
}
