package br.com.libertyseguros.mobile.beans;


public class VerifyBeans {

    private String message;
    private Boolean sucesso;
    private String nomeSegurado;
    private String numeroApolice;
    private Integer codigoCIF;


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

    public String getNomeSegurado() {
        return nomeSegurado;
    }

    public void setNomeSegurado(String nomeSegurado) {
        this.nomeSegurado = nomeSegurado;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    public Integer getCodigoCIF() {
        return codigoCIF;
    }

    public void setCodigoCIF(Integer codigoCIF) {
        this.codigoCIF = codigoCIF;
    }
}
